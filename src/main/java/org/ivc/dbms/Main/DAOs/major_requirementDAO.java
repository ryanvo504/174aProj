package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.major_requirement;

public class major_requirementDAO {

    Connection connection;
    
    public major_requirementDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addMajorRequirement(major_requirement majorRequirement) throws SQLException {
        String sql = "INSERT INTO major_requirements (major, course_number) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, majorRequirement.getMajor());
            pstmt.setString(2, majorRequirement.getcourseNumber());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public List<major_requirement> getMajorRequirementsByMajor(String major) throws SQLException {
        List<major_requirement> majorRequirements = new ArrayList<>();
        String sql = "SELECT major, course_number FROM major_requirements WHERE major = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, major);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    major_requirement majorRequirement = new major_requirement(
                        rs.getString("major"), 
                        rs.getString("course_number"));
                    
                    majorRequirements.add(majorRequirement);
                }
            }
        }
        return majorRequirements;
    }

    public List<major_requirement> getAllMajorRequirements() throws SQLException {
        List<major_requirement> majorRequirements = new ArrayList<>();
        String sql = "SELECT major, course_number FROM major_requirements";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                major_requirement majorRequirement = new major_requirement(
                    rs.getString("major"), 
                    rs.getString("course_name"));
                
                majorRequirements.add(majorRequirement);
            }
        }
        return majorRequirements;
    }



    public boolean deleteMajorRequirement(String courseId) throws SQLException {
        String sql = "DELETE FROM major_requirements WHERE courseId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    

}
