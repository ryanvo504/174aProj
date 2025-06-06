package org.ivc.dbms.Main.Interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ivc.dbms.Main.Interfaces.RegistrarStaffInterface.CourseGrade;
import org.ivc.dbms.Main.classes.enrolled_in;

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
    public boolean addStudentToCourse(String studentId, Integer code, String quarter) {
        try {
            String sql = "INSERT INTO ENROLLED_IN (PERM_NUMBER, ENROLLMENT_CODE, QUARTER_YR) VALUES (?, ?, ?)";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setInt(2, code);
            stmt.setString(3, quarter);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean dropStudentFromCourse(String studentId, Integer code, String quarter) {
        try {
            String sql = "DELETE FROM ENROLLED_IN WHERE PERM_NUMBER = ? AND ENROLLMENT_CODE = ? AND QUARTER_YR = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setInt(2, code);
            stmt.setString(3, quarter);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<enrolled_in> listStudentCourses(String studentId, String quarter) {
        List<enrolled_in> courses = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ENROLLED_IN WHERE PERM_NUMBER = ? AND QUARTER_YR = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setString(2, quarter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                enrolled_in course = new enrolled_in();
                course.setPermNumber(rs.getInt("PERM_NUMBER"));
                course.setEnrollmentCode(rs.getInt("ENROLLMENT_CODE"));
                course.setQuarter(rs.getString("QUARTER_YR"));
                courses.add(course);
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
            String sql = "SELECT c.course_number, g.grade, c.quarter " +
                        "FROM grades g " +
                        "JOIN Course c ON g.course_number = c.course_number " +
                        "WHERE g.perm_number = ? AND c.quarter = " +
                        "(SELECT MAX(quarter) FROM Course)";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grades.add(new CourseGrade(
                    rs.getString("course_number"),
                    rs.getString("grade"),
                    rs.getString("quarter")
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
            String sql = "INSERT INTO grades (perm_number, course_number, grade) VALUES (?, ?, ?)";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String studentId = parts[0].trim();
                    String grade = parts[1].trim();
                    
                    stmt.setInt(1, Integer.parseInt(studentId));
                    stmt.setString(2, courseId);
                    stmt.setString(3, grade);
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
            String sql = "SELECT c.course_number, c.title, g.grade, c.quarter " +
                        "FROM grades g " +
                        "JOIN Course c ON g.course_number = c.course_number " +
                        "WHERE g.perm_number = ? " +
                        "ORDER BY c.quarter, c.course_number";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            ResultSet rs = stmt.executeQuery();
            
            String currentQuarter = null;
            while (rs.next()) {
                String quarter = rs.getString("quarter");
                if (!quarter.equals(currentQuarter)) {
                    if (currentQuarter != null) {
                        transcript.append("\n");
                    }
                    transcript.append("Quarter: ").append(quarter).append("\n");
                    currentQuarter = quarter;
                }
                transcript.append(String.format("%s - %s: %s\n",
                    rs.getString("course_number"),
                    rs.getString("title"),
                    rs.getString("grade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transcript.toString();
    }
    
    @Override
    public boolean generateGradeMailers() {
        try {
            String sql = "SELECT DISTINCT PERM_NUMBER FROM ENROLLED_IN";
            PreparedStatement stmt = dbConnection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String studentId = rs.getString("PERM_NUMBER");
                String transcript = requestTranscript(studentId);
                // Here you would typically write the transcript to a file or send it via email
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