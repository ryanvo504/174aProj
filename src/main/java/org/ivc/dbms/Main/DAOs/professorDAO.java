package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.professor;

public class professorDAO {
    private Connection connection;

    public professorDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addProfessor(professor professor) throws SQLException {
        String sql = "INSERT INTO Professor (name) VALUES (?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, professor.getName());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public professor getProfessorByName(String name) throws SQLException {
        String sql = "SELECT name FROM Professor WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    professor professor = new professor();
                    professor.setName(rs.getString("name"));
                    return professor;
                }
            }
        }
        return null;
    }

    public List<professor> getAllProfessors() throws SQLException {
        List<professor> professors = new ArrayList<>();
        String sql = "SELECT name FROM Professor";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                professor professor = new professor();
                professor.setName(rs.getString("name"));
                professors.add(professor);
            }
        }
        return professors;
    }

    public boolean updateProfessor(professor professor) throws SQLException {
        String sql = "UPDATE Professor SET name = ? WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, professor.getName());
            pstmt.setString(2, professor.getName());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteProfessor(String name) throws SQLException {
        String sql = "DELETE FROM Professor WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
