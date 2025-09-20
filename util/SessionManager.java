package util;

import Model.Student;

/**
 * Session manager for handling current user authentication state
 */
public class SessionManager {
    private static SessionManager instance;
    private Student currentUser;
    
    // Singleton pattern
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    // Private constructor
    private SessionManager() {
        currentUser = null;
    }
    
    // Login a user
    public boolean login(Student user) {
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    // Logout the current user
    public void logout() {
        currentUser = null;
    }
    
    // Get the current user
    public Student getCurrentUser() {
        return currentUser;
    }
    
    // Check if a user is logged in
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    // Check if the current user is an admin
    public boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
}