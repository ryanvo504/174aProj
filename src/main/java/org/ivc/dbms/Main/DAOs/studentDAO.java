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
        String sql = "INSERT INTO student (perm_number, name, address, dept, pin) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, student.getPermNumber());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getAddress());
            pstmt.setString(4, student.getdept());
        
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public student getStudentByPermNumber(Long permNumber) throws SQLException {
        String sql = "SELECT perm_number, name, address, dept,, title FROM student WHERE perm_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, permNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    student student = new student(
                        rs.getLong("perm_number"), 
                        rs.getLong("pin"), 
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
        String sql = "SELECT student_number, title FROM student";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                student student = new student(
                rs.getLong("perm_number"), 
                rs.getLong("pin"), 
                rs.getString("name"), 
                rs.getString("address"), 
                rs.getString("dept"));

                students.add(student);
            }
        }
        return students;
    }

    public boolean updatestudent(student student) throws SQLException {
        String sql = "UPDATE student SET name = ?, address = ?, dept = ?, pin = ?, WHERE student_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getAddress());
            pstmt.setString(3, student.getdept());
            pstmt.setLong(4, student.getPin());
            pstmt.setLong(5, student.getPermNumber()); 
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletestudent(Long perm) throws SQLException {
        String sql = "DELETE FROM student WHERE student_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, perm);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


}
