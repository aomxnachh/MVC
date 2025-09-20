package Model;

import java.io.*;
import java.util.*;
import java.time.LocalDate;

/**
 * Data manager class to handle data storage and retrieval
 */
public class DataManager {
    private static final String DATA_DIR = "data/";
    private static final String STUDENTS_FILE = DATA_DIR + "students.dat";
    private static final String SUBJECTS_FILE = DATA_DIR + "subjects.dat";
    private static final String CURRICULUM_FILE = DATA_DIR + "curriculum.dat";
    private static final String REGISTRATIONS_FILE = DATA_DIR + "registrations.dat";
    
    private List<Student> students;
    private List<Subject> subjects;
    private List<SubjectStructure> curriculums;
    private List<RegisteredSubject> registrations;
    
    private static DataManager instance;
    
    // Singleton pattern
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    // Private constructor
    private DataManager() {
        students = new ArrayList<>();
        subjects = new ArrayList<>();
        curriculums = new ArrayList<>();
        registrations = new ArrayList<>();
        
        // Create data directory if it doesn't exist
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // Load data or create sample data if files don't exist
        if (!loadData()) {
            createSampleData();
            saveData();
        } else {
            // Ensure admin exists if data was loaded
            ensureAdminExists();
        }
    }
    
    // Save all data to files
    public void saveData() {
        saveObjectToFile(students, STUDENTS_FILE);
        saveObjectToFile(subjects, SUBJECTS_FILE);
        saveObjectToFile(curriculums, CURRICULUM_FILE);
        saveObjectToFile(registrations, REGISTRATIONS_FILE);
    }
    
    // Load all data from files
    private boolean loadData() {
        boolean allFilesExist = 
            new File(STUDENTS_FILE).exists() &&
            new File(SUBJECTS_FILE).exists() &&
            new File(CURRICULUM_FILE).exists() &&
            new File(REGISTRATIONS_FILE).exists();
            
        if (!allFilesExist) {
            return false;
        }
        
        try {
            @SuppressWarnings("unchecked")
            List<Student> loadedStudents = (List<Student>) loadObjectFromFile(STUDENTS_FILE);
            students = loadedStudents;
            
            // Ensure admin user exists
            ensureAdminExists();
            
            @SuppressWarnings("unchecked")
            List<Subject> loadedSubjects = (List<Subject>) loadObjectFromFile(SUBJECTS_FILE);
            subjects = loadedSubjects;
            
            @SuppressWarnings("unchecked")
            List<SubjectStructure> loadedCurriculums = (List<SubjectStructure>) loadObjectFromFile(CURRICULUM_FILE);
            curriculums = loadedCurriculums;
            
            @SuppressWarnings("unchecked")
            List<RegisteredSubject> loadedRegistrations = (List<RegisteredSubject>) loadObjectFromFile(REGISTRATIONS_FILE);
            registrations = loadedRegistrations;
            return true;
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            return false;
        }
    }
    
    // Create sample data as required
    private void createSampleData() {
        // Create sample curriculums (at least 2)
        SubjectStructure cs = new SubjectStructure("10000001", "Computer Science", "Department of Computer Science");
        SubjectStructure eng = new SubjectStructure("20000001", "Computer Engineering", "Department of Computer Engineering");
        
        // Create sample subjects (at least 10, with at least 1 having prerequisites)
        Subject s1 = new Subject("05500001", "Introduction to Programming", 3, "Dr. Smith", "");
        Subject s2 = new Subject("05500002", "Data Structures", 3, "Dr. Johnson", "05500001");
        Subject s3 = new Subject("05500003", "Database Systems", 3, "Dr. Brown", "");
        Subject s4 = new Subject("05500004", "Web Development", 3, "Dr. Davis", "");
        Subject s5 = new Subject("05500005", "Computer Networks", 3, "Dr. Wilson", "");
        Subject s6 = new Subject("05500006", "Operating Systems", 3, "Dr. Anderson", "");
        Subject s7 = new Subject("90690001", "General Mathematics", 3, "Dr. Thomas", "");
        Subject s8 = new Subject("90690002", "Physics for Computing", 3, "Dr. Taylor", "");
        Subject s9 = new Subject("90690003", "Technical Writing", 3, "Dr. Harris", "");
        Subject subj10 = new Subject("90690004", "Ethics in Computing", 3, "Dr. Lewis", "");
        
        // Add subjects to curriculums with semesters
        // Computer Science - Semester 1
        cs.addRequiredSubject("05500001", 1); // Intro to Programming
        cs.addRequiredSubject("90690001", 1); // General Mathematics
        cs.addRequiredSubject("90690003", 1); // Technical Writing
        
        // Computer Science - Semester 2
        cs.addRequiredSubject("05500002", 2); // Data Structures
        cs.addRequiredSubject("05500003", 2); // Database Systems
        cs.addRequiredSubject("90690002", 2); // Physics for Computing
        
        // Computer Engineering - Semester 1
        eng.addRequiredSubject("05500001", 1); // Intro to Programming
        eng.addRequiredSubject("90690001", 1); // General Mathematics
        eng.addRequiredSubject("90690002", 1); // Physics for Computing
        
        // Computer Engineering - Semester 2
        eng.addRequiredSubject("05500005", 2); // Computer Networks
        eng.addRequiredSubject("05500006", 2); // Operating Systems
        eng.addRequiredSubject("90690004", 2); // Ethics in Computing
        
        // Create sample students (at least 10 and 1 admin)
        // Admin
        Student admin = new Student(
            "69000000", "Mr.", "Admin", "User", 
            LocalDate.of(1990, 1, 1), "N/A", "admin@kmitl.ac.th", 
            "", "admin", true
        );
        
        // Regular students
        Student s01 = new Student(
            "69000001", "Mr.", "John", "Doe", 
            LocalDate.of(2007, 5, 15), "Bangkok High School", "john@kmitl.ac.th", 
            "10000001", "password", false
        );
        
        Student s02 = new Student(
            "69000002", "Ms.", "Jane", "Smith", 
            LocalDate.of(2006, 8, 22), "Bangkok High School", "jane@kmitl.ac.th", 
            "10000001", "password", false
        );
        
        Student s03 = new Student(
            "69000003", "Mr.", "Mike", "Johnson", 
            LocalDate.of(2007, 3, 10), "Chiang Mai High School", "mike@kmitl.ac.th", 
            "20000001", "password", false
        );
        
        Student s04 = new Student(
            "69000004", "Ms.", "Sarah", "Williams", 
            LocalDate.of(2006, 7, 5), "Chiang Mai High School", "sarah@kmitl.ac.th", 
            "20000001", "password", false
        );
        
        Student s05 = new Student(
            "69000005", "Mr.", "David", "Brown", 
            LocalDate.of(2007, 11, 18), "Phuket High School", "david@kmitl.ac.th", 
            "10000001", "password", false
        );
        
        Student s06 = new Student(
            "69000006", "Ms.", "Emily", "Jones", 
            LocalDate.of(2006, 4, 30), "Phuket High School", "emily@kmitl.ac.th", 
            "10000001", "password", false
        );
        
        Student s07 = new Student(
            "69000007", "Mr.", "Robert", "Taylor", 
            LocalDate.of(2007, 9, 12), "Khon Kaen High School", "robert@kmitl.ac.th", 
            "20000001", "password", false
        );
        
        Student s08 = new Student(
            "69000008", "Ms.", "Jessica", "Anderson", 
            LocalDate.of(2006, 2, 25), "Khon Kaen High School", "jessica@kmitl.ac.th", 
            "20000001", "password", false
        );
        
        Student s09 = new Student(
            "69000009", "Mr.", "Thomas", "Wilson", 
            LocalDate.of(2007, 6, 8), "Songkhla High School", "thomas@kmitl.ac.th", 
            "10000001", "password", false
        );
        
        Student s10 = new Student(
            "69000010", "Ms.", "Olivia", "Martinez", 
            LocalDate.of(2006, 10, 15), "Songkhla High School", "olivia@kmitl.ac.th", 
            "20000001", "password", false
        );
        
        // Create some sample registrations with grades
        RegisteredSubject r1 = new RegisteredSubject("69000001", "05500001", "A");
        RegisteredSubject r2 = new RegisteredSubject("69000001", "90690001", "B+");
        RegisteredSubject r3 = new RegisteredSubject("69000002", "05500001", "B");
        RegisteredSubject r4 = new RegisteredSubject("69000003", "05500001", "A");
        RegisteredSubject r5 = new RegisteredSubject("69000004", "90690001", "C+");
        
        // Add all objects to their respective lists
        curriculums.add(cs);
        curriculums.add(eng);
        
        subjects.add(s1);
        subjects.add(s2);
        subjects.add(s3);
        subjects.add(s4);
        subjects.add(s5);
        subjects.add(s6);
        subjects.add(s7);
        subjects.add(s8);
        subjects.add(s9);
        subjects.add(subj10);
        
        students.add(admin);
        students.add(s01);
        students.add(s02);
        students.add(s03);
        students.add(s04);
        students.add(s05);
        students.add(s06);
        students.add(s07);
        students.add(s08);
        students.add(s09);
        students.add(s10);
        
        registrations.add(r1);
        registrations.add(r2);
        registrations.add(r3);
        registrations.add(r4);
        registrations.add(r5);
    }
    
    // Helper method to save an object to a file
    private void saveObjectToFile(Object obj, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Error saving to " + filePath + ": " + e.getMessage());
        }
    }
    
    // Helper method to load an object from a file
    private Object loadObjectFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        }
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    // Get students by school
    public List<Student> getStudentsBySchool(String school) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getSchool().equals(school) && !student.isAdmin()) {
                result.add(student);
            }
        }
        return result;
    }
    
    // Get all schools
    public List<String> getAllSchools() {
        Set<String> schools = new HashSet<>();
        for (Student student : students) {
            if (!student.isAdmin()) {
                schools.add(student.getSchool());
            }
        }
        return new ArrayList<>(schools);
    }
    
    // Get all subjects
    public List<Subject> getAllSubjects() {
        return new ArrayList<>(subjects);
    }
    
    // Get subject by ID
    public Subject getSubjectById(String subjectId) {
        for (Subject subject : subjects) {
            if (subject.getSubjectId().equals(subjectId)) {
                return subject;
            }
        }
        return null;
    }
    
    // Get all curriculums
    public List<SubjectStructure> getAllCurriculums() {
        return new ArrayList<>(curriculums);
    }
    
    // Get curriculum by ID
    public SubjectStructure getCurriculumById(String curriculumId) {
        for (SubjectStructure curriculum : curriculums) {
            if (curriculum.getCurriculumId().equals(curriculumId)) {
                return curriculum;
            }
        }
        return null;
    }
    
    // Get student by ID
    public Student getStudentById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
    
    // Get student by email (for login)
    public Student getStudentByEmail(String email) {
        for (Student student : students) {
            if (student.getEmail().equals(email)) {
                return student;
            }
        }
        return null;
    }
    
    // Get all registered subjects for a student
    public List<RegisteredSubject> getRegisteredSubjectsForStudent(String studentId) {
        List<RegisteredSubject> result = new ArrayList<>();
        for (RegisteredSubject reg : registrations) {
            if (reg.getStudentId().equals(studentId)) {
                result.add(reg);
            }
        }
        return result;
    }
    
    // Get all students registered for a subject
    public List<Student> getStudentsForSubject(String subjectId) {
        List<Student> result = new ArrayList<>();
        for (RegisteredSubject reg : registrations) {
            if (reg.getSubjectId().equals(subjectId)) {
                Student student = getStudentById(reg.getStudentId());
                if (student != null) {
                    result.add(student);
                }
            }
        }
        return result;
    }
    
    // Check if a student has completed a prerequisite
    public boolean hasCompletedPrerequisite(String studentId, String prerequisiteId) {
        for (RegisteredSubject reg : registrations) {
            if (reg.getStudentId().equals(studentId) && 
                reg.getSubjectId().equals(prerequisiteId) && 
                reg.isGraded()) {
                return true;
            }
        }
        return false;
    }
    
    // Register a student for a subject
    public void registerSubject(String studentId, String subjectId) {
        // Check if already registered
        for (RegisteredSubject reg : registrations) {
            if (reg.getStudentId().equals(studentId) && reg.getSubjectId().equals(subjectId)) {
                return; // Already registered
            }
        }
        
        RegisteredSubject newReg = new RegisteredSubject(studentId, subjectId, null);
        registrations.add(newReg);
        saveData();
    }
    
    // Set grade for a registered subject
    public void setGrade(String studentId, String subjectId, String grade) {
        for (RegisteredSubject reg : registrations) {
            if (reg.getStudentId().equals(studentId) && reg.getSubjectId().equals(subjectId)) {
                reg.setGrade(grade);
                saveData();
                return;
            }
        }
    }
    
    // Add a new student
    public void addStudent(Student student) {
        students.add(student);
        saveData();
    }
    
    // Add a new subject
    public void addSubject(Subject subject) {
        subjects.add(subject);
        saveData();
    }
    
    // Add a new curriculum
    public void addCurriculum(SubjectStructure curriculum) {
        curriculums.add(curriculum);
        saveData();
    }
    
    // Update a student
    public void updateStudent(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(student.getStudentId())) {
                students.set(i, student);
                saveData();
                return;
            }
        }
    }
    
    // Get available subjects for a student to register
    public List<Subject> getAvailableSubjectsForStudent(String studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            return new ArrayList<>();
        }
        
        String curriculumId = student.getCurriculumId();
        SubjectStructure curriculum = getCurriculumById(curriculumId);
        if (curriculum == null) {
            return new ArrayList<>();
        }
        
        List<String> requiredSubjectIds = curriculum.getAllRequiredSubjects();
        List<Subject> availableSubjects = new ArrayList<>();
        
        // Get already registered subjects
        Set<String> registeredSubjectIds = new HashSet<>();
        for (RegisteredSubject reg : getRegisteredSubjectsForStudent(studentId)) {
            registeredSubjectIds.add(reg.getSubjectId());
        }
        
        // Check each required subject
        for (String subjectId : requiredSubjectIds) {
            // Skip if already registered
            if (registeredSubjectIds.contains(subjectId)) {
                continue;
            }
            
            Subject subject = getSubjectById(subjectId);
            if (subject == null) {
                continue;
            }
            
            // Check prerequisite
            if (subject.hasPrerequisite()) {
                if (hasCompletedPrerequisite(studentId, subject.getPrerequisiteId())) {
                    availableSubjects.add(subject);
                }
            } else {
                availableSubjects.add(subject);
            }
        }
        
        return availableSubjects;
    }
    
    // Get count of students registered for a subject
    public int getRegistrationCountForSubject(String subjectId) {
        int count = 0;
        for (RegisteredSubject reg : registrations) {
            if (reg.getSubjectId().equals(subjectId)) {
                count++;
            }
        }
        return count;
    }
    
    // Ensure admin user exists in the student list
    private void ensureAdminExists() {
        boolean adminExists = false;
        
        // Check if admin exists
        for (Student student : students) {
            if (student.getEmail().equals("admin@kmitl.ac.th") && student.isAdmin()) {
                adminExists = true;
                break;
            }
        }
        
        // If admin doesn't exist, add it
        if (!adminExists) {
            Student admin = new Student(
                "69000000", "Mr.", "Admin", "User", 
                LocalDate.of(1990, 1, 1), "N/A", "admin@kmitl.ac.th", 
                "", "admin", true
            );
            students.add(admin);
            saveData(); // Save the updated student list
            System.out.println("Admin user was added to the system.");
        }
    }
}