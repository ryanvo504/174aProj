package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ivc.dbms.Main.classes.major;

public class majorDAO {

    
    private Connection connection;

    public majorDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addmajor(major major) throws SQLException {
        String sql = "INSERT INTO major (name) VALUES (?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, major.getName());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public Optional<major> getmajorbyname(String name) throws SQLException {
        String sql = "SELECT name FROM major WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    major major = new major(rs.getString("name"));
                    return Optional.of(major);
                }
            }
        }
        return Optional.empty();
    }

    public List<major> getAllmajors() throws SQLException {
        List<major> majors = new ArrayList<>();
        String sql = "SELECT * FROM major";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                major major = new major(rs.getString("name"));


                majors.add(major);
            }
        }
        return majors;
    }


    public boolean deletemajor(String name) throws SQLException {
        String sql = "DELETE FROM major WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updatemajor(major major, String updated) throws SQLException {
        String sql = "UPDATE major SET name = ? WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, major.getName());
            pstmt.setString(2, updated); 
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}

