package org.ivc.dbms.Main.Interfaces;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.ivc.dbms.Main.DAOs.courseDAO;
import org.ivc.dbms.Main.DAOs.studentDAO;
import org.ivc.dbms.Main.Interfaces.RegistrarStaffInterface.CourseGrade;
import org.ivc.dbms.Main.classes.course;
import org.ivc.dbms.Main.classes.student;

/**
 * Main interface for Registrar's Office operations.
 * This class provides a clean interface for all registrar staff operations.
 */
public class Registrar {
    private final RegistrarStaffInterface staffInterface;
    private final courseDAO courseDao;
    private final studentDAO studentDao;
    private final Connection connection;

    public Registrar(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
        this.staffInterface = new RegistrarStaffImpl(connection);
        this.courseDao = new courseDAO(connection);
        this.studentDao = new studentDAO(connection);
    }

    /**
     * Adds a student to a course.
     * @param permNumber Student's perm number
     * @param courseNumber Course number
     * @return true if successful, false otherwise
     */
    public boolean addStudentToCourse(int permNumber, String courseNumber) {
        try {
            // Verify student exists
            student student = studentDao.getStudentByPermNumber(permNumber);
            if (student == null) {
                System.err.println("Student with perm number " + permNumber + " not found");
                return false;
            }

            // Verify course exists
            Optional<course> course = courseDao.getCourseByCourseNumber(courseNumber);
            if (course.isEmpty()) {
                System.err.println("Course " + courseNumber + " not found");
                return false;
            }

            return staffInterface.addStudentToCourse(String.valueOf(permNumber), courseNumber);
        } catch (SQLException e) {
            System.err.println("Error adding student to course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Drops a student from a course.
     * @param permNumber Student's perm number
     * @param courseNumber Course number
     * @return true if successful, false otherwise
     */
    public boolean dropStudentFromCourse(int permNumber, String courseNumber) {
        try {
            // Verify student exists
            student student = studentDao.getStudentByPermNumber(permNumber);
            if (student == null) {
                System.err.println("Student with perm number " + permNumber + " not found");
                return false;
            }

            // Verify course exists
            Optional<course> course = courseDao.getCourseByCourseNumber(courseNumber);
            if (course.isEmpty()) {
                System.err.println("Course " + courseNumber + " not found");
                return false;
            }

            return staffInterface.dropStudentFromCourse(String.valueOf(permNumber), courseNumber);
        } catch (SQLException e) {
            System.err.println("Error dropping student from course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lists all courses taken by a student.
     * @param permNumber Student's perm number
     * @return List of course numbers
     */
    public List<String> listStudentCourses(int permNumber) {
        try {
            // Verify student exists
            student student = studentDao.getStudentByPermNumber(permNumber);
            if (student == null) {
                System.err.println("Student with perm number " + permNumber + " not found");
                return List.of();
            }

            return staffInterface.listStudentCourses(String.valueOf(permNumber));
        } catch (SQLException e) {
            System.err.println("Error listing student courses: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Lists the grades of the previous quarter for a student.
     * @param permNumber Student's perm number
     * @return List of course grades
     */
    public List<RegistrarStaffInterface.CourseGrade> listPreviousQuarterGrades(int permNumber) {
        try {
            // Verify student exists
            student student = studentDao.getStudentByPermNumber(permNumber);
            if (student == null) {
                System.err.println("Student with perm number " + permNumber + " not found");
                return List.of();
            }

            return staffInterface.listPreviousQuarterGrades(String.valueOf(permNumber));
        } catch (SQLException e) {
            System.err.println("Error listing previous quarter grades: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Generates a class list for a course.
     * @param courseNumber Course number
     * @return List of student perm numbers
     */
    public List<String> generateClassList(String courseNumber) {
        try {
            // Verify course exists
            Optional<course> course = courseDao.getCourseByCourseNumber(courseNumber);
            if (course.isEmpty()) {
                System.err.println("Course " + courseNumber + " not found");
                return List.of();
            }

            return staffInterface.generateClassList(courseNumber);
        } catch (SQLException e) {
            System.err.println("Error generating class list: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Enters grades for a course from a file.
     * @param courseNumber Course number
     * @param gradeFile File containing student grades
     * @return true if successful, false otherwise
     */
    public boolean enterGradesFromFile(String courseNumber, File gradeFile) {
        try {
            // Verify course exists
            Optional<course> course = courseDao.getCourseByCourseNumber(courseNumber);
            if (course.isEmpty()) {
                System.err.println("Course " + courseNumber + " not found");
                return false;
            }

            // Verify file exists
            if (!gradeFile.exists() || !gradeFile.isFile()) {
                System.err.println("Grade file does not exist or is not a file");
                return false;
            }

            return staffInterface.enterGradesFromFile(courseNumber, gradeFile);
        } catch (SQLException e) {
            System.err.println("Error entering grades from file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Requests a transcript for a student.
     * @param permNumber Student's perm number
     * @return Formatted transcript string
     */
    public String requestTranscript(int permNumber) {
        try {
            // Verify student exists
            student student = studentDao.getStudentByPermNumber(permNumber);
            if (student == null) {
                return "Student with perm number " + permNumber + " not found";
            }

            return staffInterface.requestTranscript(String.valueOf(permNumber));
        } catch (SQLException e) {
            return "Error requesting transcript: " + e.getMessage();
        }
    }

    /**
     * Generates grade mailers for all students.
     * @return true if successful, false otherwise
     */
    public boolean generateGradeMailers() {
        try {
            return staffInterface.generateGradeMailers();
        } catch (Exception e) {
            System.err.println("Error generating grade mailers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the database connection.
     * @return The database connection
     */
    public Connection getConnection() {
        return connection;
    }
} 