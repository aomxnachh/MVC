package Controller;

import Model.*;
import util.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling student-related operations
 */
public class StudentController {
    private DataManager dataManager;
    private SessionManager sessionManager;
    
    public StudentController() {
        dataManager = DataManager.getInstance();
        sessionManager = SessionManager.getInstance();
    }
    
    // Get a student by ID
    public Student getStudentById(String studentId) {
        return dataManager.getStudentById(studentId);
    }
    
    // Get all registered subjects for a student
    public List<RegisteredSubject> getRegisteredSubjects(String studentId) {
        return dataManager.getRegisteredSubjectsForStudent(studentId);
    }
    
    // Get all available subjects for a student to register
    public List<Subject> getAvailableSubjects(String studentId) {
        return dataManager.getAvailableSubjectsForStudent(studentId);
    }
    
    // Register a student for a subject
    public boolean registerSubject(String studentId, String subjectId) {
        // Check if the student can register for this subject
        Subject subject = dataManager.getSubjectById(subjectId);
        
        if (subject == null) {
            return false;
        }
        
        // Check prerequisite
        if (subject.hasPrerequisite() && 
            !dataManager.hasCompletedPrerequisite(studentId, subject.getPrerequisiteId())) {
            return false;
        }
        
        // Register the subject
        dataManager.registerSubject(studentId, subjectId);
        return true;
    }
    
    // Get full subject details for a registered subject
    public Subject getSubjectDetails(String subjectId) {
        return dataManager.getSubjectById(subjectId);
    }
    
    // Get curriculum details for a student
    public SubjectStructure getCurriculumForStudent(String studentId) {
        Student student = dataManager.getStudentById(studentId);
        if (student != null) {
            return dataManager.getCurriculumById(student.getCurriculumId());
        }
        return null;
    }
    
    // Check if the current user is authorized to view a student's profile
    public boolean canViewStudentProfile(String studentId) {
        if (!sessionManager.isLoggedIn()) {
            return false;
        }
        
        // Admins can view any profile
        if (sessionManager.isAdmin()) {
            return true;
        }
        
        // Students can only view their own profile
        Student currentUser = sessionManager.getCurrentUser();
        return currentUser.getStudentId().equals(studentId);
    }
    
    // Get a list of subject details for registered subjects
    public List<Subject> getRegisteredSubjectDetails(String studentId) {
        List<RegisteredSubject> registrations = getRegisteredSubjects(studentId);
        List<Subject> subjects = new ArrayList<>();
        
        for (RegisteredSubject reg : registrations) {
            Subject subject = dataManager.getSubjectById(reg.getSubjectId());
            if (subject != null) {
                subjects.add(subject);
            }
        }
        
        return subjects;
    }
    
    // Check if the current user is an admin
    public boolean isAdmin() {
        return sessionManager.isAdmin();
    }
    
    // Get the grade for a specific subject
    public String getGradeForSubject(String studentId, String subjectId) {
        List<RegisteredSubject> registrations = getRegisteredSubjects(studentId);
        
        for (RegisteredSubject reg : registrations) {
            if (reg.getSubjectId().equals(subjectId)) {
                return reg.getGrade();
            }
        }
        
        return null; // Not registered
    }
}