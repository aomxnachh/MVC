package Model;

/**
 * Subject model representing courses that students can register for
 */
public class Subject extends BaseModel {
    private String subjectId; // 8 digits (0550X or 9069X)
    private String subjectName;
    private int credits; // > 0
    private String instructor;
    private String prerequisiteId; // Foreign key to another Subject, if applicable
    
    // Constructor
    public Subject(String subjectId, String subjectName, int credits, String instructor, String prerequisiteId) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.credits = credits;
        this.instructor = instructor;
        this.prerequisiteId = prerequisiteId;
    }
    
    // Default constructor
    public Subject() {
    }
    
    // Getters and Setters
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getPrerequisiteId() {
        return prerequisiteId;
    }

    public void setPrerequisiteId(String prerequisiteId) {
        this.prerequisiteId = prerequisiteId;
    }
    
    public boolean hasPrerequisite() {
        return prerequisiteId != null && !prerequisiteId.isEmpty();
    }
    
    @Override
    public String toString() {
        return subjectId + ": " + subjectName + " (" + credits + " credits)";
    }
}