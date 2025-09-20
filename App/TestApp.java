package App;

import Model.*;
import Controller.*;
import View.LoginView;

import java.time.LocalDate;

/**
 * Class for testing the application
 */
public class TestApp {
    public static void main(String[] args) {
        System.out.println("Starting test application...");
        
        // Test data access
        DataManager dataManager = DataManager.getInstance();
        
        // Test loading students
        System.out.println("Testing student data...");
        for (Student student : dataManager.getAllStudents()) {
            if (!student.isAdmin()) {
                System.out.println(student.getStudentId() + ": " + student.getFullName() + 
                                   ", Age: " + student.getAge() + 
                                   ", School: " + student.getSchool());
            }
        }
        
        // Test loading subjects
        System.out.println("\nTesting subject data...");
        for (Subject subject : dataManager.getAllSubjects()) {
            System.out.println(subject.getSubjectId() + ": " + subject.getSubjectName() + 
                               ", Credits: " + subject.getCredits() + 
                               ", Instructor: " + subject.getInstructor());
        }
        
        // Test loading curriculums
        System.out.println("\nTesting curriculum data...");
        for (SubjectStructure curriculum : dataManager.getAllCurriculums()) {
            System.out.println(curriculum.getCurriculumId() + ": " + curriculum.getCurriculumName() + 
                               ", Department: " + curriculum.getDepartmentName());
            
            System.out.println("  Semester 1 subjects:");
            for (String subjectId : curriculum.getRequiredSubjectsForSemester(1)) {
                Subject subject = dataManager.getSubjectById(subjectId);
                if (subject != null) {
                    System.out.println("    " + subject.getSubjectId() + ": " + subject.getSubjectName());
                }
            }
            
            System.out.println("  Semester 2 subjects:");
            for (String subjectId : curriculum.getRequiredSubjectsForSemester(2)) {
                Subject subject = dataManager.getSubjectById(subjectId);
                if (subject != null) {
                    System.out.println("    " + subject.getSubjectId() + ": " + subject.getSubjectName());
                }
            }
        }
        
        // Test validation
        System.out.println("\nTesting validation...");
        ValidationController validationController = new ValidationController();
        
        // Test age validation
        Student validAgeStudent = new Student("69000099", "Mr.", "Test", "Student", 
                                             LocalDate.now().minusYears(16), "Test School", 
                                             "test@example.com", "10000001", "password", false);
        Student invalidAgeStudent = new Student("69000099", "Mr.", "Test", "Student", 
                                               LocalDate.now().minusYears(14), "Test School", 
                                               "test@example.com", "10000001", "password", false);
        
        System.out.println("Valid age student (16): " + validationController.validateStudentAge(validAgeStudent));
        System.out.println("Invalid age student (14): " + validationController.validateStudentAge(invalidAgeStudent));
        
        // Test ID validation
        System.out.println("Valid student ID (69000001): " + validationController.validateStudentId("69000001"));
        System.out.println("Invalid student ID (59000001): " + validationController.validateStudentId("59000001"));
        
        System.out.println("Valid subject ID (05500001): " + validationController.validateSubjectId("05500001"));
        System.out.println("Valid subject ID (90690001): " + validationController.validateSubjectId("90690001"));
        System.out.println("Invalid subject ID (12345678): " + validationController.validateSubjectId("12345678"));
        
        System.out.println("Valid curriculum ID (10000001): " + validationController.validateCurriculumId("10000001"));
        System.out.println("Invalid curriculum ID (00000001): " + validationController.validateCurriculumId("00000001"));
        
        // Test prerequisite validation
        String studentId = "69000001"; // Use a student from the sample data
        String subjectWithPrereq = "05500002"; // Data Structures (requires Intro to Programming)
        String subjectWithoutPrereq = "05500003"; // Database Systems (no prerequisite)
        
        System.out.println("Can register for subject without prerequisite: " + 
                           validationController.canRegisterForSubject(studentId, subjectWithoutPrereq));
        System.out.println("Can register for subject with prerequisite: " + 
                           validationController.canRegisterForSubject(studentId, subjectWithPrereq));
        
        // Launch the application
        System.out.println("\nTests completed. Launching application...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}