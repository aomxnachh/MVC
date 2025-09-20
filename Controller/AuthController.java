package Controller;

import Model.DataManager;
import Model.Student;
import util.SessionManager;

/**
 * Controller for handling authentication-related operations
 */
public class AuthController {
    private DataManager dataManager;
    private SessionManager sessionManager;
    
    public AuthController() {
        dataManager = DataManager.getInstance();
        sessionManager = SessionManager.getInstance();
    }
    
    // Authenticate a user by email and password
    public boolean authenticate(String email, String password) {
        Student student = dataManager.getStudentByEmail(email);
        
        if (student != null && student.getPassword().equals(password)) {
            sessionManager.login(student);
            return true;
        }
        
        return false;
    }
    
    // Logout the current user
    public void logout() {
        sessionManager.logout();
    }
    
    // Check if a user is logged in
    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }
    
    // Check if the current user is an admin
    public boolean isAdmin() {
        return sessionManager.isAdmin();
    }
    
    // Get the current user
    public Student getCurrentUser() {
        return sessionManager.getCurrentUser();
    }
}