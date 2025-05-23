package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.course;

public class courseDAO {
    
    private Connection connection;

    public courseDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addCourse(course course) throws SQLException {
        String sql = "INSERT INTO Course (course_number, title) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseNumber());
            pstmt.setString(2, course.getTitle());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public course getCourseByCourseNumber(String courseNumber) throws SQLException {
        String sql = "SELECT course_number, title FROM Course WHERE course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    course course = new course();
                    course.setCourseNumber(rs.getString("course_number"));
                    course.setTitle(rs.getString("title"));
                    return course;
                }
            }
        }
        return null;
    }

    public List<course> getAllCourses() throws SQLException {
        List<course> courses = new ArrayList<>();
        String sql = "SELECT course_number, title FROM Course";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                course course = new course();
                course.setCourseNumber(rs.getString("course_number"));
                course.setTitle(rs.getString("title"));
                courses.add(course);
            }
        }
        return courses;
    }

    public boolean updateCourse(course course) throws SQLException {
        String sql = "UPDATE Course SET title = ? WHERE course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getCourseNumber()); 
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCourse(String courseNumber) throws SQLException {
        String sql = "DELETE FROM Course WHERE course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

}
