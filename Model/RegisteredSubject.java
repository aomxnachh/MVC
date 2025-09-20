package Model;

/**
 * RegisteredSubject model representing subjects registered by students with their grades
 */
public class RegisteredSubject extends BaseModel {
    private String studentId; // Foreign key to Student
    private String subjectId; // Foreign key to Subject
    private String grade; // A, B+, B, C+, C, D+, D, F, or null if not graded yet
    
    // Constructor
    public RegisteredSubject(String studentId, String subjectId, String grade) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.grade = grade;
    }
    
    // Default constructor
    public RegisteredSubject() {
    }
    
    // Valid grades
    public static final String[] VALID_GRADES = {"A", "B+", "B", "C+", "C", "D+", "D", "F"};
    
    // Check if a grade is valid
    public static boolean isValidGrade(String grade) {
        if (grade == null || grade.isEmpty()) {
            return true; // Allow empty grade (not graded yet)
        }
        
        for (String validGrade : VALID_GRADES) {
            if (validGrade.equals(grade)) {
                return true;
            }
        }
        return false;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        if (!isValidGrade(grade)) {
            throw new IllegalArgumentException("Invalid grade: " + grade);
        }
        this.grade = grade;
    }
    
    // Check if the subject is graded
    public boolean isGraded() {
        return grade != null && !grade.isEmpty();
    }
    
    @Override
    public String toString() {
        return studentId + " - " + subjectId + ": " + (grade == null ? "Not graded" : grade);
    }
}