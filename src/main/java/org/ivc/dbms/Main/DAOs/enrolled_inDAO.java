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
        String sql = "INSERT INTO ENROLLED_IN (PERM_NUMBER, ENROLLMENT_CODE, QUARTER_YR) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrolled.getPermNumber());
            pstmt.setInt(2, enrolled.getEnrollmentCode());
            pstmt.setString(3, enrolled.getQuarter());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<enrolled_in> getEnrolledByCodeAndQuarter(Integer EnrollmentCode, String Quarter_YR) throws SQLException {
        String sql = "SELECT * FROM ENROLLED_IN WHERE Enrollment_Code = ? AND Quarter_YR = ?";
        List<enrolled_in> courseList = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, EnrollmentCode);
            pstmt.setString(2, Quarter_YR);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrolled_in enrolled = new enrolled_in();
                    enrolled.setPermNumber(rs.getInt("PERM_NUMBER"));
                    enrolled.setQuarter(Quarter_YR);
                    enrolled.setEnrollmentCode(EnrollmentCode);
                    courseList.add(enrolled);
                }
            }
        }
        return courseList;
    }

    public List<enrolled_in> getEnrolledByPERM_NUMBER(Integer PERM_NUMBER) throws SQLException {
        String sql = "SELECT * FROM ENROLLED_IN WHERE PERM_NUMBER = ?";
        List<enrolled_in> courseList = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, PERM_NUMBER);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrolled_in enrolled = new enrolled_in();
                    enrolled.setPermNumber(PERM_NUMBER);
                    enrolled.setQuarter(rs.getString("Quarter_YR"));
                    enrolled.setEnrollmentCode((rs.getInt("Enrollment_Code")));
                    courseList.add(enrolled);
                }
            }
        }
        return courseList;
    }

    public List<enrolled_in> getEnrolledInByPermAndQuarter(Integer PERM_NUMBER, String Quarter_YR) throws SQLException {
        String sql = "SELECT * FROM ENROLLED_IN WHERE PERM_NUMBER = ? AND Quarter_YR = ?";
        List<enrolled_in> courseList = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, PERM_NUMBER);
            pstmt.setString(2, Quarter_YR);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrolled_in enrolled = new enrolled_in();
                    enrolled.setPermNumber(PERM_NUMBER);
                    enrolled.setQuarter(Quarter_YR);
                    enrolled.setEnrollmentCode(rs.getInt("Enrollment_Code"));
                    courseList.add(enrolled);
                }
            }
        }
        return courseList;
    }

    public List<enrolled_in> getAllEnrolled() throws SQLException {
        List<enrolled_in> enrolledList = new ArrayList<>();
        String sql = "SELECT * FROM ENROLLED_IN";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                enrolled_in enrolled = new enrolled_in();
                enrolled.setPermNumber(Integer.valueOf(rs.getInt("PERM_NUMBER")));
                enrolled.setQuarter(rs.getString("Quarter_YR"));
                enrolled.setEnrollmentCode(rs.getInt("Enrollment_Code"));
            }
        }
        return enrolledList;
    }
    
    public boolean deleteEnrolledIn(Integer PERM_NUMBER, Integer EnrollmentCode, String qrt_yr) throws SQLException {
        String sql = "DELETE FROM Enrolled_IN WHERE PERM_NUMBER = ? AND Enrollment_Code = ? AND Quarter_YR = ?";

        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, PERM_NUMBER);
            pstmt.setInt(2, EnrollmentCode);
            pstmt.setString(3, qrt_yr);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    


}
