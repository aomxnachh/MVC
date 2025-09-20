package View;

import Controller.AdminController;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Admin view for managing grades for a specific subject
 */
public class GradeManagementView extends JFrame {
    private AdminController adminController;
    private String subjectId;
    private String subjectName;
    
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JTable registrationsTable;
    private DefaultTableModel registrationsTableModel;
    
    public GradeManagementView(String subjectId, String subjectName) {
        this.adminController = new AdminController();
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        
        // Check if the current user is an admin
        if (!adminController.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                    "You are not authorized to access the grade management panel.",
                    "Authorization Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Set up the frame
        setTitle("Grade Management: " + subjectName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Create components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Subject Information"));
        
        JLabel subjectIdLabel = new JLabel("Subject ID: " + subjectId);
        JLabel subjectNameLabel = new JLabel("Subject Name: " + subjectName);
        
        infoPanel.add(subjectIdLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(subjectNameLabel);
        
        // Table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Registered Students"));
        
        // Create table model for students
        String[] columnNames = {"Student ID", "Student Name", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        
        // Create table
        studentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        // View registrations button
        JButton viewRegistrationsButton = new JButton("View All Registrations for Selected Student");
        viewRegistrationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStudentRegistrations();
            }
        });
        
        // Save button
        JButton saveButton = new JButton("Save Grades");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGrades();
            }
        });
        
        // Back button
        JButton backButton = new JButton("Back to Subjects");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close this window and return to subject list
            }
        });
        
        buttonPanel.add(viewRegistrationsButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        
        // Add components to main panel
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Load students
        loadStudents();
    }
    
    // Load students into the table
    private void loadStudents() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get students registered for this subject
        List<Student> students = adminController.getStudentsForSubject(subjectId);
        
        // Add each student to the table
        for (Student student : students) {
            Object[] rowData = {
                student.getStudentId(),
                student.getFullName(),
                ""  // Empty grade initially
            };
            
            tableModel.addRow(rowData);
        }
        
        // Set the combo box as the editor for the grade column
        studentsTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(
            new JComboBox<>(new String[]{"", "A", "B+", "B", "C+", "C", "D+", "D", "F"})
        ));
    }
    
    // Save grades for all students
    private void saveGrades() {
        int rowCount = tableModel.getRowCount();
        boolean success = true;
        
        for (int i = 0; i < rowCount; i++) {
            String studentId = (String) tableModel.getValueAt(i, 0);
            String grade = (String) tableModel.getValueAt(i, 2);
            
            // Skip empty grades
            if (grade == null || grade.isEmpty()) {
                continue;
            }
            
            try {
                adminController.setGrade(studentId, subjectId, grade);
            } catch (Exception e) {
                success = false;
                JOptionPane.showMessageDialog(this,
                        "Error setting grade for student " + studentId + ": " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Grades saved successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // View all registrations for the selected student
    private void viewStudentRegistrations() {
        int selectedRow = studentsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student to view registrations.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Create a new dialog to show registrations
        JDialog dialog = new JDialog(this, "Registrations for " + studentName, true);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create table model for registrations
        String[] regColumnNames = {"Subject ID", "Subject Name", "Credits", "Instructor", "Grade"};
        registrationsTableModel = new DefaultTableModel(regColumnNames, 0);
        
        // Create table
        registrationsTable = new JTable(registrationsTableModel);
        JScrollPane regScrollPane = new JScrollPane(registrationsTable);
        
        // Get all registrations for this student
        List<Object[]> registrations = adminController.getRegisteredSubjectsWithDetailsForStudent(studentId);
        
        // Add each registration to the table
        for (Object[] registration : registrations) {
            registrationsTableModel.addRow(registration);
        }
        
        // Close button
        JButton closeButton = new JButton("Back");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.add(closeButton);
        
        dialogPanel.add(new JLabel("All Registered Subjects for " + studentName + " (ID: " + studentId + ")"), 
                        BorderLayout.NORTH);
        dialogPanel.add(regScrollPane, BorderLayout.CENTER);
        dialogPanel.add(closePanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(dialogPanel);
        dialog.setVisible(true);
    }
}