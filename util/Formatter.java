package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting data
 */
public class Formatter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Format a date to a string
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        return date.format(DATE_FORMATTER);
    }
    
    // Parse a date string to a LocalDate
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    // Format a grade for display (empty becomes "Not graded")
    public static String formatGrade(String grade) {
        if (grade == null || grade.isEmpty()) {
            return "Not graded";
        }
        return grade;
    }
    
    // Format a curriculum ID for display
    public static String formatCurriculumId(String curriculumId) {
        if (curriculumId == null || curriculumId.isEmpty()) {
            return "N/A";
        }
        return curriculumId;
    }
}