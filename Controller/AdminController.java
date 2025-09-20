package Controller;

import Model.*;
import util.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Controller for handling admin-related operations
 */
public class AdminController {
    private DataManager dataManager;
    private SessionManager sessionManager;
    
    public AdminController() {
        dataManager = DataManager.getInstance();
        sessionManager = SessionManager.getInstance();
    }
    
    // Check if the current user is an admin
    public boolean isAdmin() {
        return sessionManager.isAdmin();
    }
    
    // Get all students (excluding admin)
    public List<Student> getAllStudents() {
        List<Student> allStudents = dataManager.getAllStudents();
        List<Student> result = new ArrayList<>();
        
        for (Student student : allStudents) {
            if (!student.isAdmin()) {
                result.add(student);
            }
        }
        
        return result;
    }
    
    // Get students filtered by school
    public List<Student> getStudentsBySchool(String school) {
        return dataManager.getStudentsBySchool(school);
    }
    
    // Get all available schools
    public List<String> getAllSchools() {
        return dataManager.getAllSchools();
    }
    
    // Sort students by name (ascending)
    public List<Student> sortStudentsByName(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        Collections.sort(sorted, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return s1.getFullName().compareTo(s2.getFullName());
            }
        });
        return sorted;
    }
    
    // Sort students by age (ascending)
    public List<Student> sortStudentsByAge(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        Collections.sort(sorted, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Integer.compare(s1.getAge(), s2.getAge());
            }
        });
        return sorted;
    }
    
    // Get all subjects
    public List<Subject> getAllSubjects() {
        return dataManager.getAllSubjects();
    }
    
    // Get students registered for a subject
    public List<Student> getStudentsForSubject(String subjectId) {
        return dataManager.getStudentsForSubject(subjectId);
    }
    
    // Set grade for a student's subject
    public void setGrade(String studentId, String subjectId, String grade) {
        if (!RegisteredSubject.isValidGrade(grade)) {
            throw new IllegalArgumentException("Invalid grade: " + grade);
        }
        
        dataManager.setGrade(studentId, subjectId, grade);
    }
    
    // Get the registration count for a subject
    public int getRegistrationCountForSubject(String subjectId) {
        return dataManager.getRegistrationCountForSubject(subjectId);
    }
    
    // Search students by name or ID
    public List<Student> searchStudents(String query) {
        List<Student> allStudents = getAllStudents();
        List<Student> result = new ArrayList<>();
        
        if (query == null || query.isEmpty()) {
            return allStudents;
        }
        
        String lowerQuery = query.toLowerCase();
        
        for (Student student : allStudents) {
            if (student.getStudentId().contains(lowerQuery) ||
                student.getFullName().toLowerCase().contains(lowerQuery)) {
                result.add(student);
            }
        }
        
        return result;
    }
    
    // Get all registered subjects with details for a student
    public List<Object[]> getRegisteredSubjectsWithDetailsForStudent(String studentId) {
        List<Object[]> result = new ArrayList<>();
        List<RegisteredSubject> registrations = dataManager.getRegisteredSubjectsForStudent(studentId);
        
        for (RegisteredSubject reg : registrations) {
            Subject subject = dataManager.getSubjectById(reg.getSubjectId());
            if (subject != null) {
                Object[] details = {
                    subject.getSubjectId(),
                    subject.getSubjectName(),
                    subject.getCredits(),
                    subject.getInstructor(),
                    reg.getGrade() == null || reg.getGrade().isEmpty() ? "Not graded" : reg.getGrade()
                };
                result.add(details);
            }
        }
        
        return result;
    }
}