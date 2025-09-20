package View;

import Controller.StudentController;
import Model.Subject;
import Model.SubjectStructure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * View for subject registration
 */
public class SubjectRegistrationView extends JFrame {
    private String studentId;
    private StudentController studentController;
    
    private JTable subjectsTable;
    private DefaultTableModel tableModel;
    private JButton registerButton;
    private JButton backButton;
    
    public SubjectRegistrationView(String studentId) {
        this.studentId = studentId;
        this.studentController = new StudentController();
        
        // Set up the frame
        setTitle("Subject Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        // Create components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Available Subjects"));
        
        // Create table model
        String[] columnNames = {"Subject ID", "Subject Name", "Credits", "Instructor", "Prerequisite"};
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
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        // Register button
        registerButton = new JButton("Register for Selected Subject");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerSelectedSubject();
            }
        });
        
        // Back button
        backButton = new JButton("Back to Profile");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close this window and return to profile
            }
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        // Add components to main panel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Curriculum information
        SubjectStructure curriculum = studentController.getCurriculumForStudent(studentId);
        if (curriculum != null) {
            JLabel curriculumLabel = new JLabel("Curriculum: " + curriculum.getCurriculumName() + 
                                               " (" + curriculum.getDepartmentName() + ")");
            curriculumLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            mainPanel.add(curriculumLabel, BorderLayout.NORTH);
        }
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Load available subjects
        loadAvailableSubjects();
    }
    
    // Load available subjects into the table
    private void loadAvailableSubjects() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get available subjects
        List<Subject> subjects = studentController.getAvailableSubjects(studentId);
        
        // Add each subject to the table
        for (Subject subject : subjects) {
            // Get prerequisite name if it exists
            String prerequisite = "None";
            if (subject.hasPrerequisite()) {
                Subject prereq = studentController.getSubjectDetails(subject.getPrerequisiteId());
                if (prereq != null) {
                    prerequisite = prereq.getSubjectName();
                }
            }
            
            Object[] rowData = {
                subject.getSubjectId(),
                subject.getSubjectName(),
                subject.getCredits(),
                subject.getInstructor(),
                prerequisite
            };
            
            tableModel.addRow(rowData);
        }
    }
    
    // Register for the selected subject
    private void registerSelectedSubject() {
        int selectedRow = subjectsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a subject to register.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String subjectId = (String) tableModel.getValueAt(selectedRow, 0);
        String subjectName = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Confirm registration
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to register for " + subjectName + "?",
                "Confirm Registration",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = studentController.registerSubject(studentId, subjectId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Successfully registered for " + subjectName + ".",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the available subjects
                loadAvailableSubjects();
                
                // Return to student profile view
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Please check prerequisites.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}