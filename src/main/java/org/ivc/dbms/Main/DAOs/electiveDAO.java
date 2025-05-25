package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.elective;

public class electiveDAO {

    Connection connection;
    
    public electiveDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addElective(elective elective) throws SQLException {
        String sql = "INSERT INTO elective (major, course_name) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, elective.getMajor());
            pstmt.setString(2, elective.getCourseName());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<elective> getMajorRequirementsByMajor(String major) throws SQLException {
        List<elective> majorRequirements = new ArrayList<>();
        String sql = "SELECT major, course_name FROM major_requirement WHERE major = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, major);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    elective electiveRequirement = new elective(
                        rs.getString("major"), 
                        rs.getString("course_name"));
                    
                    majorRequirements.add(electiveRequirement);
                }
            }
        }
        return majorRequirements;
    }

    public List<elective> getAllMajorRequirements() throws SQLException {
        List<elective> majorRequirements = new ArrayList<>();
        String sql = "SELECT majorId, courseId FROM major_requirement";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                elective majorRequirement = new elective(
                    rs.getString("major"), 
                    rs.getString("course_name"));
                
                majorRequirements.add(majorRequirement);
            }
        }
        return majorRequirements;
    }



    public boolean deleteMajorRequirement(String courseId) throws SQLException {
        String sql = "DELETE FROM major_requirement WHERE courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    


}
