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
        String sql = "INSERT INTO ENROLLED_IN (PERM_NUMBER, COURSE_NUMBER) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrolled.getPermNumber());
            pstmt.setString(2, enrolled.getcourseNumber());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public enrolled_in getEnrolledByCOURSE_NUMBER(String COURSE_NUMBER) throws SQLException {
        String sql = "SELECT PERM_NUMBER, COURSE_NUMBER FROM ENROLLED_IN WHERE COURSE_NUMBER = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, COURSE_NUMBER);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    enrolled_in enrolled = new enrolled_in(
                        rs.getInt("PERM_NUMBER"), 
                        rs.getString("COURSE_NUMBER"));
                    return enrolled;
                }
            }
        }
        return null;
    }

    public enrolled_in getEnrolledByPERM_NUMBER(Integer PERM_NUMBER) throws SQLException {
        String sql = "SELECT PERM_NUMBER, COURSE_NUMBER FROM ENROLLED_IN WHERE PERM_NUMBER = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, PERM_NUMBER);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    enrolled_in enrolled = new enrolled_in(
                        rs.getInt("PERM_NUMBER"), 
                        rs.getString("COURSE_NUMBER"));
                    return enrolled;
                }
            }
        }
        return null;
    }

    public List<enrolled_in> getAllEnrolled() throws SQLException {
        List<enrolled_in> enrolledList = new ArrayList<>();
        String sql = "SELECT PERM_NUMBER, COURSE_NUMBER FROM ENROLLED_IN";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                enrolled_in enrolled = new enrolled_in(
                    rs.getInt("PERM_NUMBER"), 
                    rs.getString("COURSE_NUMBER"));
                enrolledList.add(enrolled);
            }
        }
        return enrolledList;
    }

    public boolean updateEnrolledIn(enrolled_in enrolled) throws SQLException {
        String sql = "UPDATE ENROLLED_IN SET COURSE_NUMBER = ? WHERE PERM_NUMBER = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, enrolled.getcourseNumber());
            pstmt.setInt(2, enrolled.getPermNumber());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteEnrolledIn(Integer PERM_NUMBER) throws SQLException {
        String sql = "DELETE FROM Enrolled_IN WHERE PERM_NUMBER = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, PERM_NUMBER);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    


}
