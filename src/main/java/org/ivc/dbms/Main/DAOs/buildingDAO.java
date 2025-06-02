package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.building;

public class buildingDAO {
    private Connection connection;

    public buildingDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addBuilding(building building) throws SQLException {
        String sql = "INSERT INTO Building (building_code) VALUES (?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, building.getBuildingCode());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public building getBuildingByCode(String buildingCode) throws SQLException {
        String sql = "SELECT building_code FROM Building WHERE building_code = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, buildingCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    building building = new building();
                    building.setBuildingCode(rs.getString("building_code"));
                    return building;
                }
            }
        }
        return null;
    }

    public List<building> getAllBuildings() throws SQLException {
        List<building> buildings = new ArrayList<>();
        String sql = "SELECT building_code FROM Building";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                building building = new building();
                building.setBuildingCode(rs.getString("building_code"));
                buildings.add(building);
            }
        }
        return buildings;
    }

    public boolean updateBuilding(building building) throws SQLException {
        String sql = "UPDATE Building SET building_code = ? WHERE building_code = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, building.getBuildingCode());
            pstmt.setString(2, building.getBuildingCode());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteBuilding(String buildingCode) throws SQLException {
        String sql = "DELETE FROM Building WHERE building_code = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, buildingCode);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
