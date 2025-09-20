package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * SubjectStructure model representing the curriculum structure for each program
 */
public class SubjectStructure extends BaseModel {
    private String curriculumId; // 8 digits, not starting with 0
    private String curriculumName;
    private String departmentName;
    private List<String> requiredSubjectIds = new ArrayList<>(); // List of required subject IDs
    private List<Integer> semesters = new ArrayList<>(); // Corresponding semesters (1 or 2) for each subject
    
    // Constructor
    public SubjectStructure(String curriculumId, String curriculumName, String departmentName) {
        this.curriculumId = curriculumId;
        this.curriculumName = curriculumName;
        this.departmentName = departmentName;
    }
    
    // Default constructor
    public SubjectStructure() {
    }
    
    // Add a required subject to the curriculum with its semester
    public void addRequiredSubject(String subjectId, int semester) {
        requiredSubjectIds.add(subjectId);
        semesters.add(semester);
    }
    
    // Get required subjects for a specific semester
    public List<String> getRequiredSubjectsForSemester(int semester) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < requiredSubjectIds.size(); i++) {
            if (semesters.get(i) == semester) {
                result.add(requiredSubjectIds.get(i));
            }
        }
        return result;
    }
    
    // Get all required subjects regardless of semester
    public List<String> getAllRequiredSubjects() {
        return new ArrayList<>(requiredSubjectIds);
    }
    
    // Get the semester for a specific subject
    public int getSemesterForSubject(String subjectId) {
        int index = requiredSubjectIds.indexOf(subjectId);
        if (index >= 0) {
            return semesters.get(index);
        }
        return -1; // Subject not found in this curriculum
    }
    
    // Getters and Setters
    public String getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        this.curriculumId = curriculumId;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    @Override
    public String toString() {
        return curriculumId + ": " + curriculumName + " (" + departmentName + ")";
    }
}