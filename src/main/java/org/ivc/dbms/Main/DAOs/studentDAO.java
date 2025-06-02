package org.ivc.dbms.Main.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.classes.student;

public class studentDAO {

    
    private Connection connection;

    public studentDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addstudent(student student) throws SQLException {
        String sql = "INSERT INTO students (Perm_Number, Pin, Name, Address, Dept) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, student.getPermNumber());
            pstmt.setInt(2, student.getPin());
            pstmt.setString(3, student.getName());
            pstmt.setString(4, student.getAddress());
            pstmt.setString(5, student.getdept());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public student getStudentByPermNumber(Integer permNumber) throws SQLException {
        String sql = "SELECT perm_number, Pin, name, address, dept FROM students WHERE perm_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, permNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    student student = new student(
                        rs.getInt("perm_number"), 
                        rs.getInt("pin"), 
                        rs.getString("name"), 
                        rs.getString("address"), 
                        rs.getString("dept"));
                    return student;
                }
            }
        }
        return null;
    }

    public List<student> getAllstudents() throws SQLException {
        List<student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                student student = new student(
                rs.getInt("perm_number"), 
                rs.getInt("pin"), 
                rs.getString("name"), 
                rs.getString("address"), 
                rs.getString("dept"));

                students.add(student);
            }
        }
        return students;
    }

    public boolean updatestudent(student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, address = ?, dept = ?, pin = ? WHERE perm_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getAddress());
            pstmt.setString(3, student.getdept());
            pstmt.setInt(4, student.getPin());
            pstmt.setInt(5, student.getPermNumber()); 
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletestudent(Integer perm) throws SQLException {
        String sql = "DELETE FROM students WHERE perm_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, perm);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


}
