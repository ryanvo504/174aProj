package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.has_completed;

public class has_completedDAO {

    
    private Connection connection;

    public has_completedDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addCompleted(has_completed completed) throws SQLException {
        String sql = "INSERT INTO has_completed (studentId, courseId) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, completed.getStudentId());
            pstmt.setString(2, completed.getCourseName());
            pstmt.setString(3, String.valueOf(completed.getGrade()));
        
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public has_completed getCompletedByCourseId(String courseId) throws SQLException {
        String sql = "SELECT studentId, courseId, grade FROM has_completed WHERE courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    has_completed completed = new has_completed(
                        rs.getLong("studentId"), 
                        rs.getString("courseId"), 
                        rs.getString("grade").charAt(0));
                    return completed;
                }
            }
        }
        return null;
    }
    public has_completed getCompletedByStudentId(Long studentId) throws SQLException {
        String sql = "SELECT studentId, courseId, grade FROM has_completed WHERE studentId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    has_completed completed = new has_completed(
                        rs.getLong("studentId"), 
                        rs.getString("courseId"), 
                        rs.getString("grade").charAt(0));
                    return completed;
                }
            }
        }
        return null;
    }

    public List<has_completed> getAllCompleted() throws SQLException {
        List<has_completed> completedList = new ArrayList<>();
        String sql = "SELECT studentId, courseId, grade FROM has_completed";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                has_completed completed = new has_completed(
                    rs.getLong("studentId"), 
                    rs.getString("courseId"), 
                    rs.getString("grade").charAt(0));
                completedList.add(completed);
            }
        }
        return completedList;
    }

    public boolean updateCompleted(has_completed completed) throws SQLException {
        String sql = "UPDATE has_completed SET grade = ? WHERE studentId = ? AND courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(completed.getGrade()));
            pstmt.setLong(2, completed.getStudentId());
            pstmt.setString(3, completed.getCourseName());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public boolean deleteCompleted(Long studentId, String courseId) throws SQLException {
        String sql = "DELETE FROM has_completed WHERE studentId = ? AND courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, studentId);
            pstmt.setString(2, courseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
}

