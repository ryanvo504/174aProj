package org.ivc.dbms.Main.Interfaces;

import java.io.*;
import java.util.*;

import org.ivc.dbms.Main.Interfaces.RegistrarStaffInterface.CourseGrade;

import java.sql.*;

/**
 * Implementation of the RegistrarStaffInterface.
 * This class handles all registrar staff operations.
 */
public class RegistrarStaffImpl implements RegistrarStaffInterface {
    private final Connection dbConnection;
    
    public RegistrarStaffImpl(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }
    
    @Override
    public boolean addStudentToCourse(String studentId, String courseId) {
        try {
            String sql = "INSERT INTO ENROLLED_IN (PERM_NUMBER, COURSE_NUMBER) VALUES (?, ?)";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setString(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean dropStudentFromCourse(String studentId, String courseId) {
        try {
            String sql = "DELETE FROM ENROLLED_IN WHERE PERM_NUMBER = ? AND COURSE_NUMBER = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setString(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<String> listStudentCourses(String studentId) {
        List<String> courses = new ArrayList<>();
        try {
            String sql = "SELECT COURSE_NUMBER FROM ENROLLED_IN WHERE PERM_NUMBER = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString("COURSE_NUMBER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    @Override
    public List<CourseGrade> listPreviousQuarterGrades(String studentId) {
        List<CourseGrade> grades = new ArrayList<>();
        try {
            // Get the most recent quarter for this student
            String quarterSql = "SELECT MAX(QUARTER) AS MAX_QUARTER FROM HAS_COMPLETED WHERE STUDENT_ID = ?";
            PreparedStatement quarterStmt = dbConnection.prepareStatement(quarterSql);
            quarterStmt.setInt(1, Integer.parseInt(studentId));
            ResultSet quarterRs = quarterStmt.executeQuery();
            String maxQuarter = null;
            if (quarterRs.next()) {
                maxQuarter = quarterRs.getString("MAX_QUARTER");
            }
            if (maxQuarter == null) return grades;
            String sql = "SELECT h.COURSE_ID, h.GRADE, h.QUARTER FROM HAS_COMPLETED h WHERE h.STUDENT_ID = ? AND h.QUARTER = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setString(2, maxQuarter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grades.add(new CourseGrade(
                    rs.getString("COURSE_ID"),
                    rs.getString("GRADE"),
                    rs.getString("QUARTER")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }
    
    @Override
    public List<String> generateClassList(String courseId) {
        List<String> students = new ArrayList<>();
        try {
            String sql = "SELECT PERM_NUMBER FROM ENROLLED_IN WHERE COURSE_NUMBER = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(rs.getString("PERM_NUMBER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    @Override
    public boolean enterGradesFromFile(String courseId, File gradeFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(gradeFile));
            String line;
            String sql = "INSERT INTO HAS_COMPLETED (STUDENT_ID, COURSE_ID, QUARTER, GRADE) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            // For demo, use a fixed quarter or fetch the current one as needed
            String currentQuarter = "25 W";
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String studentId = parts[0].trim();
                    String grade = parts[1].trim();
                    stmt.setInt(1, Integer.parseInt(studentId));
                    stmt.setString(2, courseId);
                    stmt.setString(3, currentQuarter);
                    stmt.setString(4, grade);
                    stmt.executeUpdate();
                }
            }
            reader.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String requestTranscript(String studentId) {
        StringBuilder transcript = new StringBuilder();
        try {
            String sql = "SELECT h.COURSE_ID, c.TITLE, h.GRADE, h.QUARTER " +
                        "FROM HAS_COMPLETED h " +
                        "JOIN COURSE c ON h.COURSE_ID = c.COURSE_NUMBER " +
                        "WHERE h.STUDENT_ID = ? " +
                        "ORDER BY h.QUARTER, h.COURSE_ID";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            ResultSet rs = stmt.executeQuery();
            String currentQuarter = null;
            while (rs.next()) {
                String quarter = rs.getString("QUARTER");
                if (!quarter.equals(currentQuarter)) {
                    if (currentQuarter != null) {
                        transcript.append("\n");
                    }
                    transcript.append("Quarter: ").append(quarter).append("\n");
                    currentQuarter = quarter;
                }
                transcript.append(String.format("%s - %s: %s\n",
                    rs.getString("COURSE_ID"),
                    rs.getString("TITLE"),
                    rs.getString("GRADE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transcript.toString();
    }
    
    @Override
    public boolean generateGradeMailers() {
        try {
            String sql = "SELECT DISTINCT STUDENT_ID FROM HAS_COMPLETED";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String studentId = rs.getString("STUDENT_ID");
                String transcript = requestTranscript(studentId);
                // For now, we'll just print it to console
                System.out.println("Grade mailer for student " + studentId + ":\n" + transcript);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 