package org.ivc.dbms.Main.Interfaces;

import java.io.File;
import java.util.List;

import org.ivc.dbms.Main.classes.enrolled_in;

/**
 * Interface for Registrar's Office staff functionality.
 * This interface provides methods for managing student records, courses, and grades.
 */
public interface RegistrarStaffInterface {
    
    /**
     * Adds a student to a course.
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     * @return true if successful, false otherwise
     */
    boolean addStudentToCourse(String studentId, Integer code, String quarter);
    
    /**
     * Drops a student from a course.
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     * @return true if successful, false otherwise
     */
    boolean dropStudentFromCourse(String studentId, Integer code, String quarter);
    
    /**
     * Lists all courses taken by a student.
     * @param studentId The ID of the student
     * @return List of course IDs
     */
    //List<String> listStudentCourses(String studentId);
    
    /**
     * Lists all courses offered in a specific quarter.
     * @param studentId
     * @param quarter The quarter to list courses for
     * @return List of course IDs
     */
    List<enrolled_in> listStudentCourses(String studentId, String quarter);

    /**
     * Lists the grades of the previous quarter for a student.
     * @param studentId The ID of the student
     * @return List of course grades from the previous quarter
     */
    List<CourseGrade> listPreviousQuarterGrades(String studentId);
    
    /**
     * Generates a class list for a course.
     * @param courseId The ID of the course
     * @return List of enrolled student IDs
     */
    List<String> generateClassList(String courseId);
    
    /**
     * Enters grades for a course from a file.
     * @param courseId The ID of the course
     * @param gradeFile The file containing student grades
     * @return true if successful, false otherwise
     */
    boolean enterGradesFromFile(String courseId, File gradeFile);
    
    /**
     * Requests a transcript for a student.
     * @param studentId The ID of the student
     * @return The transcript as a formatted string
     */
    String requestTranscript(String studentId);
    
    /**
     * Generates grade mailers for all students.
     * @return true if successful, false otherwise
     */
    boolean generateGradeMailers();

    
    /**
     * Inner class to represent a course grade.
     */
    class CourseGrade {
        private final String courseId;
        private final String grade;
        private final String quarter;
        
        public CourseGrade(String courseId, String grade, String quarter) {
            this.courseId = courseId;
            this.grade = grade;
            this.quarter = quarter;
        }
        
        public String getCourseId() { return courseId; }
        public String getGrade() { return grade; }
        public String getQuarter() { return quarter; }
    }
} 