package Model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Student model representing high school students who can register for courses
 */
public class Student extends BaseModel {
    private String studentId; // 8 digits, starting with "69"
    private String title; // Title/prefix (Mr., Ms., etc.)
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String school;
    private String email;
    private String curriculumId; // Foreign key to SubjectStructure
    private String password; // For authentication
    private boolean isAdmin; // Flag to identify admin users
    
    // Constructor
    public Student(String studentId, String title, String firstName, String lastName, 
                  LocalDate birthDate, String school, String email, String curriculumId,
                  String password, boolean isAdmin) {
        this.studentId = studentId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.school = school;
        this.email = email;
        this.curriculumId = curriculumId;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    
    // Default constructor
    public Student() {
    }
    
    // Calculate age based on birthdate
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    // Check if student meets the minimum age requirement (15 years)
    public boolean isValidAge() {
        return getAge() >= 15;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        this.curriculumId = curriculumId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public String getFullName() {
        return title + " " + firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return studentId + ": " + getFullName();
    }
}