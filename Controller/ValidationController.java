package Controller;

import Model.Student;
import Model.Subject;
import Model.RegisteredSubject;
import Model.DataManager;

/**
 * Controller for handling validation of business rules
 */
public class ValidationController {
    private DataManager dataManager;
    
    public ValidationController() {
        dataManager = DataManager.getInstance();
    }
    
    // Validate student age (must be at least 15)
    public boolean validateStudentAge(Student student) {
        return student != null && student.getAge() >= 15;
    }
    
    // Validate student ID format (8 digits starting with 69)
    public boolean validateStudentId(String studentId) {
        return studentId != null && 
               studentId.matches("^69\\d{6}$");
    }
    
    // Validate subject ID format 
    // (8 digits, either starting with 0550 for faculty subjects or 9069 for general education)
    public boolean validateSubjectId(String subjectId) {
        return subjectId != null && 
               (subjectId.matches("^0550\\d{4}$") || subjectId.matches("^9069\\d{4}$"));
    }
    
    // Validate curriculum ID format (8 digits not starting with 0)
    public boolean validateCurriculumId(String curriculumId) {
        return curriculumId != null && 
               curriculumId.matches("^[1-9]\\d{7}$");
    }
    
    // Validate credits (must be greater than 0)
    public boolean validateCredits(int credits) {
        return credits > 0;
    }
    
    // Validate if a student can register for a subject (checking prerequisites)
    public boolean canRegisterForSubject(String studentId, String subjectId) {
        Subject subject = dataManager.getSubjectById(subjectId);
        
        if (subject == null) {
            return false;
        }
        
        // Check if the student has already registered for this subject
        for (RegisteredSubject reg : dataManager.getRegisteredSubjectsForStudent(studentId)) {
            if (reg.getSubjectId().equals(subjectId)) {
                return false; // Already registered
            }
        }
        
        // Check prerequisite
        if (subject.hasPrerequisite()) {
            String prerequisiteId = subject.getPrerequisiteId();
            return dataManager.hasCompletedPrerequisite(studentId, prerequisiteId);
        }
        
        return true; // No prerequisite or has completed it
    }
    
    // Validate grade format
    public boolean validateGrade(String grade) {
        return RegisteredSubject.isValidGrade(grade);
    }
}