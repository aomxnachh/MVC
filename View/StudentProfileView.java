package View;

import Controller.AuthController;
import Controller.StudentController;
import Model.RegisteredSubject;
import Model.Student;
import Model.Subject;
import Model.SubjectStructure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * View for displaying a student's profile and registered subjects
 */
public class StudentProfileView extends JFrame {
    private String studentId;
    private StudentController studentController;
    private AuthController authController;
    
    private JLabel nameLabel;
    private JLabel idLabel;
    private JLabel schoolLabel;
    private JLabel ageLabel;
    private JLabel curriculumLabel;
    private JTable subjectsTable;
    private DefaultTableModel tableModel;
    private JButton registerButton;
    private JButton logoutButton;
    private JButton backButton;
    
    public StudentProfileView(String studentId) {
        this.studentId = studentId;
        this.studentController = new StudentController();
        this.authController = new AuthController();
        
        // Check if the current user is authorized to view this profile
        if (!studentController.canViewStudentProfile(studentId)) {
            JOptionPane.showMessageDialog(this,
                    "You are not authorized to view this profile.",
                    "Authorization Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Set up the frame
        setTitle("Student Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Profile panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new GridLayout(6, 2, 10, 10));
        profilePanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        // Student details
        Student student = studentController.getStudentById(studentId);
        SubjectStructure curriculum = studentController.getCurriculumForStudent(studentId);
        
        // Labels for student information
        nameLabel = new JLabel("Name: " + student.getFullName());
        idLabel = new JLabel("Student ID: " + student.getStudentId());
        schoolLabel = new JLabel("School: " + student.getSchool());
        ageLabel = new JLabel("Age: " + student.getAge() + " years");
        curriculumLabel = new JLabel("Curriculum: " + 
                (curriculum != null ? curriculum.getCurriculumName() : "N/A"));
        
        // Add information to profile panel
        profilePanel.add(nameLabel);
        profilePanel.add(idLabel);
        profilePanel.add(schoolLabel);
        profilePanel.add(ageLabel);
        profilePanel.add(curriculumLabel);
        profilePanel.add(new JLabel("Birth Date: " + 
                student.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        // Register button
        registerButton = new JButton("Register for Courses");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationView();
            }
        });
        
        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        // Back button (only for admin users)
        if (studentController.isAdmin()) {
            backButton = new JButton("Back to Student List");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close this window and return to student list
                }
            });
        }
        
        // Add buttons in proper order
        if (studentController.isAdmin()) {
            buttonPanel.add(backButton);
        } else {
            buttonPanel.add(registerButton);
        }
        buttonPanel.add(logoutButton);
        
        // Table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Registered Subjects"));
        
        // Create table model
        String[] columnNames = {"Subject ID", "Subject Name", "Credits", "Instructor", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        
        // Create table
        subjectsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(subjectsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(profilePanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Load registered subjects
        loadRegisteredSubjects();
    }
    
    // Load registered subjects into the table
    private void loadRegisteredSubjects() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get registered subjects
        List<RegisteredSubject> registrations = studentController.getRegisteredSubjects(studentId);
        
        // Add each subject to the table
        for (RegisteredSubject reg : registrations) {
            Subject subject = studentController.getSubjectDetails(reg.getSubjectId());
            
            if (subject != null) {
                Object[] rowData = {
                    subject.getSubjectId(),
                    subject.getSubjectName(),
                    subject.getCredits(),
                    subject.getInstructor(),
                    reg.getGrade() != null ? reg.getGrade() : "Not graded"
                };
                
                tableModel.addRow(rowData);
            }
        }
    }
    
    // Open registration view
    private void openRegistrationView() {
        SwingUtilities.invokeLater(() -> {
            new SubjectRegistrationView(studentId).setVisible(true);
            // Don't dispose this window, as students should return here after registration
        });
    }
    
    // Logout and return to login screen
    private void logout() {
        authController.logout();
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
            dispose();
        });
    }
}