package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.prerequisite;

public class prerequisiteDAO {
    private Connection connection;
    private static final double MIN_GRADE_POINTS = 2.0; // C grade minimum

    public prerequisiteDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addPrerequisite(String courseNumber, String prerequisiteNumber) throws SQLException {
        String sql = "INSERT INTO Prerequisite (course_number, prerequisite_number) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            pstmt.setString(2, prerequisiteNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<prerequisite> getPrerequisitesForCourse(String courseNumber) throws SQLException {
        List<prerequisite> prerequisites = new ArrayList<>();
        String sql = "SELECT prerequisite_number FROM Prerequisite WHERE course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    prerequisite prereq = new prerequisite(rs.getString("prerequisite_number"));
                    prerequisites.add(prereq);
                }
            }
        }
        return prerequisites;
    }

    public boolean canEnrollInCourse(String studentId, String courseNumber) throws SQLException {
        // First check if student is already enrolled in any prerequisite course
        if (isEnrolledInPrerequisites(studentId, courseNumber)) {
            return false;
        }

        // Then check if all prerequisites are completed with C or better
        return hasCompletedPrerequisites(studentId, courseNumber);
    }

    private boolean isEnrolledInPrerequisites(String studentId, String courseNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Enrolled_In e " +
                    "JOIN Prerequisite p ON e.course_number = p.prerequisite_number " +
                    "WHERE p.course_number = ? AND e.student_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            pstmt.setString(2, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean hasCompletedPrerequisites(String studentId, String courseNumber) throws SQLException {
        String sql = "SELECT p.prerequisite_number, h.grade " +
                    "FROM Prerequisite p " +
                    "LEFT JOIN Has_Completed h ON p.prerequisite_number = h.course_number " +
                    "AND h.student_id = ? " +
                    "WHERE p.course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String grade = rs.getString("grade");
                    
                    // If no grade found or grade is below C, return false
                    if (grade == null || getGradePoints(grade) < MIN_GRADE_POINTS) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private double getGradePoints(String grade) {
        if (grade == null || grade.isEmpty()) {
            return 0.0;
        }

        // Extract base grade and suffix
        String baseGrade = grade.substring(0, 1).toUpperCase();
        String suffix = grade.length() > 1 ? grade.substring(1) : "";

        // Base grade points
        double points;
        switch (baseGrade) {
            case "A": points = 4.0; break;
            case "B": points = 3.0; break;
            case "C": points = 2.0; break;
            case "D": points = 1.0; break;
            case "F": return 0.0;  // F is always 0.0 regardless of suffix
            default: return 0.0;
        }

        // Add suffix points
        if (suffix.equals("+")) {
            points += 0.3;
        } else if (suffix.equals("-")) {
            points -= 0.3;
        }

        return points;
    }

    public boolean deletePrerequisite(String courseNumber, String prerequisiteNumber) throws SQLException {
        String sql = "DELETE FROM Prerequisite WHERE course_number = ? AND prerequisite_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            pstmt.setString(2, prerequisiteNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
