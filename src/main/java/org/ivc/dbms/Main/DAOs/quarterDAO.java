package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.quarter;

public class quarterDAO {
    private Connection connection;

    public quarterDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addQuarter(quarter quarter) throws SQLException {
        String sql = "INSERT INTO Quarter (quarter_yr) VALUES (?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, quarter.getQuarterYr());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public quarter getQuarterByQuarterYr(String quarterYr) throws SQLException {
        String sql = "SELECT quarter_yr FROM Quarter WHERE quarter_yr = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, quarterYr);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    quarter quarter = new quarter();
                    quarter.setQuarterYr(rs.getString("quarter_yr"));
                    return quarter;
                }
            }
        }
        return null;
    }

    public List<quarter> getAllQuarters() throws SQLException {
        List<quarter> quarters = new ArrayList<>();
        String sql = "SELECT quarter_yr FROM Quarter";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                quarter quarter = new quarter();
                quarter.setQuarterYr(rs.getString("quarter_yr"));
                quarters.add(quarter);
            }
        }
        return quarters;
    }

    public boolean updateQuarter(quarter quarter) throws SQLException {
        String sql = "UPDATE Quarter SET quarter_yr = ? WHERE quarter_yr = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, quarter.getQuarterYr());
            pstmt.setString(2, quarter.getQuarterYr());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteQuarter(String quarterYr) throws SQLException {
        String sql = "DELETE FROM Quarter WHERE quarter_yr = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, quarterYr);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
