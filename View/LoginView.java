package View;

import Controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login view for user authentication
 */
public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private AuthController authController;
    
    public LoginView() {
        authController = new AuthController();
        
        // Set up the frame
        setTitle("Student Registration System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Student Registration System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // Add components to form panel
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel("")); // Empty label for spacing
        formPanel.add(loginButton);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Set the content pane
        setContentPane(mainPanel);
        
        // Make Enter key submit the form
        getRootPane().setDefaultButton(loginButton);
    }
    
    // Handle login button click
    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both email and password");
            return;
        }
        
        boolean success = authController.authenticate(email, password);
        
        if (success) {
            // Redirect to appropriate page based on user type
            if (authController.isAdmin()) {
                // Open admin dashboard
                SwingUtilities.invokeLater(() -> {
                    new AdminStudentListView().setVisible(true);
                    dispose(); // Close login window
                });
            } else {
                // Open student profile
                SwingUtilities.invokeLater(() -> {
                    new StudentProfileView(authController.getCurrentUser().getStudentId()).setVisible(true);
                    dispose(); // Close login window
                });
            }
        } else {
            statusLabel.setText("Invalid email or password");
        }
    }
}