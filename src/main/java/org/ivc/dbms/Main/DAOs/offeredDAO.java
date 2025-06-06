package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ivc.dbms.Main.classes.building;
import org.ivc.dbms.Main.classes.offered;
import org.ivc.dbms.Main.classes.professor;
import org.ivc.dbms.Main.classes.quarter;

public class offeredDAO {
    private Connection connection;

    public offeredDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addOffered(offered offered) throws SQLException {
        String sql = "INSERT INTO Offered (enrollment_code, quarter_yr, professor_name, building_code, room_num, time, max_enrollment, Course_Number) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, offered.getEnrollmentCode());
            pstmt.setString(2, offered.getQuarter().getQuarterYr());
            pstmt.setString(3, offered.getProfessor().getName());
            pstmt.setString(4, offered.getBuilding().getBuildingCode());
            pstmt.setInt(5, offered.getRoomNum());
            pstmt.setString(6, offered.getTime());
            pstmt.setInt(7, offered.getMaxEnrollment());
            pstmt.setString(8, offered.getCourseNumber());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public Optional<offered> getOfferedByPrimaryKey(int enrollmentCode, String quarterYr){
        String sql =  "SELECT * FROM OFFERED WHERE ENROLLMENT_CODE = ? AND QUARTER_YR = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentCode);
            pstmt.setString(2, quarterYr);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    offered offered = new offered();
                    offered.setEnrollmentCode(rs.getInt("enrollment_code"));
                    offered.setQuarter(new quarter(rs.getString("quarter_yr")));
                    offered.setProfessor(new professor(rs.getString("professor_name")));
                    offered.setBuilding(new building(rs.getString("building_code")));
                    offered.setRoomNum(rs.getInt("room_num"));
                    offered.setTime(rs.getString("time"));
                    offered.setMaxEnrollment(rs.getInt("max_enrollment"));
                    offered.setCourseNumber(rs.getString("course_number"));
                    
                    return Optional.of(offered);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<offered> getAllOffered() throws SQLException {
        List<offered> offeredList = new ArrayList<>();
        String sql = "SELECT * FROM OFFERED";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                offered offered = new offered();
                offered.setEnrollmentCode(rs.getInt("enrollment_code"));
                offered.setQuarter(new quarter(rs.getString("quarter_yr")));
                offered.setProfessor(new professor(rs.getString("professor_name")));
                offered.setBuilding(new building(rs.getString("building_code")));
                offered.setRoomNum(rs.getInt("room_num"));
                offered.setTime(rs.getString("time"));
                offered.setMaxEnrollment(rs.getInt("max_enrollment"));
                offered.setCourseNumber(rs.getString("course_number"));
                offeredList.add(offered);
            }
        }
        return offeredList;
    }

    public boolean updateOffered(offered offered) throws SQLException {
        String sql = "UPDATE Offered SET quarter_yr = ?, professor_name = ?, building_code = ?, " +
                    "room_num = ?, time = ?, max_enrollment = ?, course_number = ? WHERE enrollment_code = ? and quarter_yr = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, offered.getQuarter().getQuarterYr());
            pstmt.setString(2, offered.getProfessor().getName());
            pstmt.setString(3, offered.getBuilding().getBuildingCode());
            pstmt.setInt(4, offered.getRoomNum());
            pstmt.setString(5, offered.getTime());
            pstmt.setInt(6, offered.getMaxEnrollment());
            pstmt.setString(7, offered.getCourseNumber());
            pstmt.setInt(8, offered.getEnrollmentCode());
            pstmt.setString(9, offered.getQuarter().getQuarterYr());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteOffered(int enrollmentCode, String qrt_yr) throws SQLException {
        String sql = "DELETE FROM Offered WHERE enrollment_code = ? and quarter_yr = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentCode);
            pstmt.setString(2, qrt_yr);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
