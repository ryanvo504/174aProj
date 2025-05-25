package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.enrolled_in;

public class enrolled_inDAO {

    
    private Connection connection;

    public enrolled_inDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addEnrolledIn(enrolled_in enrolled) throws SQLException {
        String sql = "INSERT INTO Enrolled (studentId, courseId) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, enrolled.getStudentId());
            pstmt.setString(2, enrolled.getcourseName());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public enrolled_in getEnrolledByCourseId(String courseId) throws SQLException {
        String sql = "SELECT studentId, courseId FROM Enrolled WHERE courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    enrolled_in enrolled = new enrolled_in(
                        rs.getLong("studentId"), 
                        rs.getString("courseId"));
                    return enrolled;
                }
            }
        }
        return null;
    }

    public enrolled_in getEnrolledByStudentId(Long studentId) throws SQLException {
        String sql = "SELECT studentId, courseId FROM Enrolled WHERE studentId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    enrolled_in enrolled = new enrolled_in(
                        rs.getLong("studentId"), 
                        rs.getString("courseId"));
                    return enrolled;
                }
            }
        }
        return null;
    }

    public List<enrolled_in> getAllEnrolled() throws SQLException {
        List<enrolled_in> enrolledList = new ArrayList<>();
        String sql = "SELECT studentId, courseId FROM Enrolled";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                enrolled_in enrolled = new enrolled_in(
                    rs.getLong("studentId"), 
                    rs.getString("courseId"));
                enrolledList.add(enrolled);
            }
        }
        return enrolledList;
    }

    public boolean updateEnrolledIn(enrolled_in enrolled) throws SQLException {
        String sql = "UPDATE Enrolled SET courseId = ? WHERE studentId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, enrolled.getcourseName());
            pstmt.setLong(2, enrolled.getStudentId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteEnrolledIn(Long studentId) throws SQLException {
        String sql = "DELETE FROM Enrolled WHERE studentId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, studentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    


}
