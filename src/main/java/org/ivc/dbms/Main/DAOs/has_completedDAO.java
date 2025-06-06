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
        String sql = "INSERT INTO has_completed (student_id, course_Id, quarter, Grade) VALUES (?, ?, ? , ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, completed.getStudentId());
            pstmt.setString(2, completed.getCourseName());
            pstmt.setString(3, String.valueOf(completed.getQuarter()));
            pstmt.setString(4, String.valueOf(completed.getGrade()));
        
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public has_completed getCompletedByCourseId(String courseId) throws SQLException {
        String sql = "SELECT * FROM has_completed WHERE course_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    has_completed completed = new has_completed(
                        rs.getInt("student_id"), 
                        rs.getString("course_id"),
                        rs.getString("grade"),
                        rs.getString("quarter"));
                    return completed;
                }
            }
        }
        return null;
    }
    public List<has_completed> getCompletedByStudentId(Integer studentId) throws SQLException {
        String sql = "SELECT * FROM has_completed WHERE student_id = ?";
        List<has_completed> completedList = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    has_completed completed = new has_completed(
                        rs.getInt("student_id"), 
                        rs.getString("course_id"),
                        rs.getString("grade"), 
                        rs.getString("quarter")
                        );
                    completedList.add(completed);
                }
            }
        }
        return completedList;
    }

    public List<has_completed> getCompletedByStudentIdAndQuarter(Integer studentId, String quarter) throws SQLException {
        String sql = "SELECT * FROM has_completed WHERE student_id = ? AND quarter = ?";
        List<has_completed> completedList = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, quarter);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    has_completed completed = new has_completed(
                        rs.getInt("student_id"), 
                        rs.getString("course_id"),
                        rs.getString("grade"),
                        rs.getString("quarter"));
                    completedList.add(completed);
                }
            }
        }
        return completedList;
    }


    public List<String> getCompletedCourseIdsByStudentId(Integer studentId) throws SQLException {
        String sql = "SELECT course_id FROM has_completed WHERE student_id = ?";
        List<String> courseIds = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courseIds.add(rs.getString("course_id"));
                }
            }
        }
        return courseIds;
    }

    public List<has_completed> getAllCompleted() throws SQLException {
        List<has_completed> completedList = new ArrayList<>();
        String sql = "SELECT student_id, course_id, grade FROM has_completed";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                has_completed completed = new has_completed(
                    rs.getInt("student_id"), 
                    rs.getString("course_id"),
                    rs.getString("grade"),
                    rs.getString("quarter"));
                completedList.add(completed);
            }
        }
        return completedList;
    }

    public List<has_completed> getCompletedByQuarter(String quarter) throws SQLException {
        List<has_completed> completedList = new ArrayList<>();
        String sql = "SELECT * FROM has_completed WHERE quarter = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, quarter);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    has_completed completed = new has_completed(
                        rs.getInt("student_id"), 
                        rs.getString("course_id"),
                        rs.getString("grade"),
                        rs.getString("quarter"));
                    completedList.add(completed);
                }
            }
        }
        return completedList;
    }


    public boolean updateCompleted(has_completed completed) throws SQLException {
        String sql = "UPDATE has_completed SET grade = ? WHERE student_id = ? AND courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(completed.getGrade()));
            pstmt.setInt(2, completed.getStudentId());
            pstmt.setString(3, completed.getCourseName());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCompleted(Integer studentId, String courseId) throws SQLException {
        String sql = "DELETE FROM has_completed WHERE student_id = ? AND courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
}

