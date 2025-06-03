package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ivc.dbms.Main.classes.course;

public class courseDAO {
    
    private final Connection connection;

    public courseDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    public boolean addCourse(course course) throws SQLException {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getCourseNumber() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("Course number and title cannot be null");
        }

        String sql = "INSERT INTO COURSE (course_number, title) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseNumber());
            pstmt.setString(2, course.getTitle());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public Optional<course> getCourseByCourseNumber(String courseNumber) throws SQLException {
        if (courseNumber == null || courseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Course number cannot be null or empty");
        }

        String sql = "SELECT course_number, title FROM COURSE WHERE course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    course course = new course(
                        rs.getString("course_number"),
                        rs.getString("title")
                    );
                    return Optional.of(course);
                }
            }
        }
        return Optional.empty();
    }

    public List<course> getAllCourses() throws SQLException {
        List<course> courses = new ArrayList<>();
        String sql = "SELECT course_number, title FROM COURSE";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                course course = new course(
                    rs.getString("course_number"),
                    rs.getString("title")
                );
                courses.add(course);
            }
        }
        return courses;
    }

    public boolean updateCourse(course course) throws SQLException {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getCourseNumber() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("Course number and title cannot be null");
        }

        String sql = "UPDATE COURSE SET title = ? WHERE course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getCourseNumber()); 
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCourse(String courseNumber) throws SQLException {
        if (courseNumber == null || courseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Course number cannot be null or empty");
        }

        String sql = "DELETE FROM COURSE WHERE course_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
