package View;

import Controller.AdminController;
import Controller.AuthController;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Admin view for displaying and managing the list of students
 */
public class AdminStudentListView extends JFrame {
    private AdminController adminController;
    private AuthController authController;
    
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> schoolFilter;
    private JComboBox<String> sortOptions;
    private JButton viewProfileButton;
    private JButton subjectsButton;
    private JButton logoutButton;
    
    public AdminStudentListView() {
        this.adminController = new AdminController();
        this.authController = new AuthController();
        
        // Check if the current user is an admin
        if (!adminController.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                    "You are not authorized to access the admin panel.",
                    "Authorization Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Set up the frame
        setTitle("Admin Dashboard - Student List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Create components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search and Filter"));
        
        // Search field
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudents();
            }
        });
        
        // School filter
        JLabel schoolLabel = new JLabel("School:");
        schoolFilter = new JComboBox<>();
        schoolFilter.addItem("All Schools");
        
        // Add available schools to the filter
        List<String> schools = adminController.getAllSchools();
        for (String school : schools) {
            schoolFilter.addItem(school);
        }
        
        schoolFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudents();
            }
        });
        
        // Sort options
        JLabel sortLabel = new JLabel("Sort by:");
        sortOptions = new JComboBox<>(new String[]{"Name", "Age"});
        sortOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudents();
            }
        });
        
        // Add components to filter panel
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(searchButton);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(schoolLabel);
        filterPanel.add(schoolFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(sortLabel);
        filterPanel.add(sortOptions);
        
        // Table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Students"));
        
        // Create table model
        String[] columnNames = {"Student ID", "Name", "Age", "School", "Curriculum"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        
        // Create table
        studentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        // View profile button
        viewProfileButton = new JButton("View Selected Student Profile");
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSelectedStudentProfile();
            }
        });
        
        // Subjects button
        subjectsButton = new JButton("Manage Subjects");
        subjectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSubjectManagementView();
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
        
        buttonPanel.add(viewProfileButton);
        buttonPanel.add(subjectsButton);
        buttonPanel.add(logoutButton);
        
        // Add components to main panel
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Load students
        loadStudents();
    }
    
    // Load students into the table based on filters and sort options
    private void loadStudents() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get students based on search and filter
        List<Student> students;
        
        String searchQuery = searchField.getText().trim();
        String selectedSchool = (String) schoolFilter.getSelectedItem();
        
        if (selectedSchool != null && !selectedSchool.equals("All Schools")) {
            // Filter by school
            students = adminController.getStudentsBySchool(selectedSchool);
            
            // Apply search within school filter
            if (!searchQuery.isEmpty()) {
                students = adminController.searchStudents(searchQuery);
            }
        } else {
            // No school filter, just search
            if (!searchQuery.isEmpty()) {
                students = adminController.searchStudents(searchQuery);
            } else {
                // No filters at all
                students = adminController.getAllStudents();
            }
        }
        
        // Sort students
        String sortBy = (String) sortOptions.getSelectedItem();
        if (sortBy != null) {
            if (sortBy.equals("Name")) {
                students = adminController.sortStudentsByName(students);
            } else if (sortBy.equals("Age")) {
                students = adminController.sortStudentsByAge(students);
            }
        }
        
        // Add each student to the table
        for (Student student : students) {
            Object[] rowData = {
                student.getStudentId(),
                student.getFullName(),
                student.getAge(),
                student.getSchool(),
                student.getCurriculumId()
            };
            
            tableModel.addRow(rowData);
        }
    }
    
    // View the profile of the selected student
    private void viewSelectedStudentProfile() {
        int selectedRow = studentsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student to view profile.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        
        SwingUtilities.invokeLater(() -> {
            new StudentProfileView(studentId).setVisible(true);
        });
    }
    
    // Open the subject management view
    private void openSubjectManagementView() {
        SwingUtilities.invokeLater(() -> {
            new SubjectManagementView().setVisible(true);
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