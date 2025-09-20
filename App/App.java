package App;

import View.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main application class for Student Registration System
 */
public class App {
    
    /**
     * Main method to start the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Failed to set system look and feel: " + e.getMessage());
        }
        
        System.out.println("Starting Student Registration System...");
        
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create and display the login view
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}