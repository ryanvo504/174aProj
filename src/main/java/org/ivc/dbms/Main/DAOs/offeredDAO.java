package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.offered;
import org.ivc.dbms.Main.classes.quarter;
import org.ivc.dbms.Main.classes.professor;
import org.ivc.dbms.Main.classes.building;

public class offeredDAO {
    private Connection connection;

    public offeredDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addOffered(offered offered) throws SQLException {
        String sql = "INSERT INTO Offered (enrollment_code, quarter_yr, professor_name, building_code, room_num, time, max_enrollment) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, offered.getEnrollmentCode());
            pstmt.setString(2, offered.getQuarter().getQuarterYr());
            pstmt.setString(3, offered.getProfessor().getName());
            pstmt.setString(4, offered.getBuilding().getBuildingCode());
            pstmt.setInt(5, offered.getRoomNum());
            pstmt.setString(6, offered.getTime());
            pstmt.setInt(7, offered.getMaxEnrollment());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public offered getOfferedByEnrollmentCode(int enrollmentCode) throws SQLException {
        String sql = "SELECT o.*, q.quarter_yr, p.name as professor_name, b.building_code " +
                    "FROM Offered o " +
                    "JOIN Quarter q ON o.quarter_yr = q.quarter_yr " +
                    "JOIN Professor p ON o.professor_name = p.name " +
                    "JOIN Building b ON o.building_code = b.building_code " +
                    "WHERE o.enrollment_code = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    offered offered = new offered();
                    offered.setEnrollmentCode(rs.getInt("enrollment_code"));
                    
                    quarter quarter = new quarter();
                    quarter.setQuarterYr(rs.getString("quarter_yr"));
                    offered.setQuarter(quarter);
                    
                    professor professor = new professor();
                    professor.setName(rs.getString("professor_name"));
                    offered.setProfessor(professor);
                    
                    building building = new building();
                    building.setBuildingCode(rs.getString("building_code"));
                    offered.setBuilding(building);
                    
                    offered.setRoomNum(rs.getInt("room_num"));
                    offered.setTime(rs.getString("time"));
                    offered.setMaxEnrollment(rs.getInt("max_enrollment"));
                    
                    return offered;
                }
            }
        }
        return null;
    }

    public List<offered> getAllOffered() throws SQLException {
        List<offered> offeredList = new ArrayList<>();
        String sql = "SELECT o.*, q.quarter_yr, p.name as professor_name, b.building_code " +
                    "FROM Offered o " +
                    "JOIN Quarter q ON o.quarter_yr = q.quarter_yr " +
                    "JOIN Professor p ON o.professor_name = p.name " +
                    "JOIN Building b ON o.building_code = b.building_code";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                offered offered = new offered();
                offered.setEnrollmentCode(rs.getInt("enrollment_code"));
                
                quarter quarter = new quarter();
                quarter.setQuarterYr(rs.getString("quarter_yr"));
                offered.setQuarter(quarter);
                
                professor professor = new professor();
                professor.setName(rs.getString("professor_name"));
                offered.setProfessor(professor);
                
                building building = new building();
                building.setBuildingCode(rs.getString("building_code"));
                offered.setBuilding(building);
                
                offered.setRoomNum(rs.getInt("room_num"));
                offered.setTime(rs.getString("time"));
                offered.setMaxEnrollment(rs.getInt("max_enrollment"));
                
                offeredList.add(offered);
            }
        }
        return offeredList;
    }

    public boolean updateOffered(offered offered) throws SQLException {
        String sql = "UPDATE Offered SET quarter_yr = ?, professor_name = ?, building_code = ?, " +
                    "room_num = ?, time = ?, max_enrollment = ? WHERE enrollment_code = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, offered.getQuarter().getQuarterYr());
            pstmt.setString(2, offered.getProfessor().getName());
            pstmt.setString(3, offered.getBuilding().getBuildingCode());
            pstmt.setInt(4, offered.getRoomNum());
            pstmt.setString(5, offered.getTime());
            pstmt.setInt(6, offered.getMaxEnrollment());
            pstmt.setInt(7, offered.getEnrollmentCode());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteOffered(int enrollmentCode) throws SQLException {
        String sql = "DELETE FROM Offered WHERE enrollment_code = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentCode);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
