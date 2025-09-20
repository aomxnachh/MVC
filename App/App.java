package App;

import Model.*;
import Controller.*;
import View.LoginView;
import util.SessionManager;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Set Look and Feel to match the system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                startApplication();
            }
        });
    }
    
    private static void startApplication() {
        System.out.println("Initializing Student Registration System...");

        DataManager dataManager = DataManager.getInstance();
        
        SessionManager sessionManager = SessionManager.getInstance();
        
        if (dataManager != null) {
            System.out.println("Data loaded successfully.");
            
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            
            System.out.println("Login screen displayed. Please enter your credentials.");
        } else {
            JOptionPane.showMessageDialog(null, 
                "Failed to initialize data. Please check data files.", 
                "Initialization Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}