package org.ivc.dbms.Main.Interfaces;

import java.io.File;
import java.util.List;
import java.sql.Connection;
import java.util.Properties;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

public class RegistrarStaffDemo {
    private final RegistrarStaffInterface registrar;

    public RegistrarStaffDemo(RegistrarStaffInterface registrar) {
        this.registrar = registrar;
    }

    public void runDemo() {
        System.out.println("=== Registrar Staff Interface Demo ===\n");

        // Demo student and course IDs
        String studentId = "12345";
        String courseId = "CS101";

        // 1. Add student to course
        System.out.println("1. Adding student to course...");
        boolean added = registrar.addStudentToCourse(studentId, courseId);
        System.out.println("Student " + studentId + " added to course " + courseId + ": " + added + "\n");

        // 2. List student's courses
        System.out.println("2. Listing student's courses...");
        List<String> courses = registrar.listStudentCourses(studentId);
        System.out.println("Courses for student " + studentId + ":");
        courses.forEach(course -> System.out.println("- " + course));
        System.out.println();

        // 3. Generate class list
        System.out.println("3. Generating class list...");
        List<String> classList = registrar.generateClassList(courseId);
        System.out.println("Students in course " + courseId + ":");
        classList.forEach(student -> System.out.println("- " + student));
        System.out.println();

        // 4. List previous quarter grades
        System.out.println("4. Listing previous quarter grades...");
        List<RegistrarStaffInterface.CourseGrade> grades = registrar.listPreviousQuarterGrades(studentId);
        System.out.println("Previous quarter grades for student " + studentId + ":");
        grades.forEach(grade -> System.out.println("- Course: " + grade.getCourseId() + 
            ", Grade: " + grade.getGrade() + 
            ", Quarter: " + grade.getQuarter()));
        System.out.println();

        // 5. Request transcript
        System.out.println("5. Requesting transcript...");
        String transcript = registrar.requestTranscript(studentId);
        System.out.println("Transcript for student " + studentId + ":\n" + transcript + "\n");

        // 6. Enter grades from file
        System.out.println("6. Entering grades from file...");
        File gradeFile = new File("grades.txt");
        boolean gradesEntered = registrar.enterGradesFromFile(courseId, gradeFile);
        System.out.println("Grades entered from file: " + gradesEntered + "\n");

        // 7. Generate grade mailers
        System.out.println("7. Generating grade mailers...");
        boolean mailersGenerated = registrar.generateGradeMailers();
        System.out.println("Grade mailers generated: " + mailersGenerated + "\n");

        // 8. Drop student from course
        System.out.println("8. Dropping student from course...");
        boolean dropped = registrar.dropStudentFromCourse(studentId, courseId);
        System.out.println("Student " + studentId + " dropped from course " + courseId + ": " + dropped + "\n");

        System.out.println("=== Demo Complete ===");
    }

    public static void main(String[] args) {
        try {
            // Initialize database connection
            String url = "jdbc:oracle:thin:@avp4b59m3xioruwk_tp?TNS_ADMIN=/Users/ryanvo/Downloads/$RE3IG0I";
            String username = "ADMIN";
            String password = "DavidJoynerRyanVo011!";
            
            Properties info = new Properties();
            info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, username);
            info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, password);
            info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

            OracleDataSource ods = new OracleDataSource();
            ods.setURL(url);
            ods.setConnectionProperties(info);
            
            Connection connection = ods.getConnection();
            
            // Create an instance of RegistrarStaffImpl with the database connection
            RegistrarStaffImpl registrarImpl = new RegistrarStaffImpl(connection);
            RegistrarStaffDemo demo = new RegistrarStaffDemo(registrarImpl);
            demo.runDemo();
            
            // Close the connection when done
            connection.close();
        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 