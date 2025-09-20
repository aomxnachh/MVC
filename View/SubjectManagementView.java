package View;

import Controller.AdminController;
import Model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Admin view for managing subjects and viewing registration statistics
 */
public class SubjectManagementView extends JFrame {
    private AdminController adminController;
    
    private JTable subjectsTable;
    private DefaultTableModel tableModel;
    private JButton gradeButton;
    private JButton backButton;
    
    public SubjectManagementView() {
        this.adminController = new AdminController();
        
        // Check if the current user is an admin
        if (!adminController.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                    "You are not authorized to access the subject management panel.",
                    "Authorization Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Set up the frame
        setTitle("Subject Management");
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
        tablePanel.setBorder(BorderFactory.createTitledBorder("Subjects"));
        
        // Create table model
        String[] columnNames = {"Subject ID", "Subject Name", "Credits", "Instructor", "Registered Students"};
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
        
        // Grade button
        gradeButton = new JButton("Manage Grades for Selected Subject");
        gradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGradeManagementView();
            }
        });
        
        // Back button
        backButton = new JButton("Back to Student List");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close this window and return to student list
            }
        });
        
        buttonPanel.add(gradeButton);
        buttonPanel.add(backButton);
        
        // Add components to main panel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Load subjects
        loadSubjects();
    }
    
    // Load subjects into the table
    private void loadSubjects() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get all subjects
        List<Subject> subjects = adminController.getAllSubjects();
        
        // Add each subject to the table
        for (Subject subject : subjects) {
            int registeredCount = adminController.getRegistrationCountForSubject(subject.getSubjectId());
            
            Object[] rowData = {
                subject.getSubjectId(),
                subject.getSubjectName(),
                subject.getCredits(),
                subject.getInstructor(),
                registeredCount
            };
            
            tableModel.addRow(rowData);
        }
    }
    
    // Open the grade management view for the selected subject
    private void openGradeManagementView() {
        int selectedRow = subjectsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a subject to manage grades.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String subjectId = (String) tableModel.getValueAt(selectedRow, 0);
        String subjectName = (String) tableModel.getValueAt(selectedRow, 1);
        
        SwingUtilities.invokeLater(() -> {
            new GradeManagementView(subjectId, subjectName).setVisible(true);
        });
    }
}