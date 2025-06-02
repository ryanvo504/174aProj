package org.ivc.dbms.Main;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.ivc.dbms.Main.DAOs.courseDAO;
import org.ivc.dbms.Main.classes.course;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

public class Main {
    // The recommended format of a connection URL is:
    // "jdbc:oracle:thin:@<DATABASE_NAME_LOWERCASE>_tp?TNS_ADMIN=<PATH_TO_WALLET>"
    // where
    // <DATABASE_NAME_LOWERCASE> is your database name in lowercase
    // and
    // <PATH_TO_WALLET> is the path to the connection wallet on your machine.
    // NOTE: on a Mac, there's no C: drive...
    // IMPORTANT: Replace with your actual DB_URL, DB_USER, DB_PASSWORD
    final static String DB_URL = "jdbc:oracle:thin:@avp4b59m3xioruwk_tp?TNS_ADMIN=/Users/ryanvo/Downloads/$RE3IG0I";
    final static String DB_USER = "ADMIN";
    final static String DB_PASSWORD = "DavidJoynerRyanVo011!";

    public static void main(String args[]) throws SQLException {
        Properties info = new Properties();
        System.out.println("Attempting to connect to: " + DB_URL);

        System.out.println("Initializing connection properties...");
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

        System.out.println("Creating OracleDataSource...");
        OracleDataSource ods = new OracleDataSource();

        System.out.println("Setting connection properties...");
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);

        // With AutoCloseable, the connection is closed automatically
        try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
            System.out.println("Connection established successfully!");
            // Get JDBC driver name and version
            DatabaseMetaData dbmd = connection.getMetaData();
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Driver Version: " + dbmd.getDriverVersion());
            System.out.println("Database username: " + connection.getUserName());
            System.out.println();

            // --- Demonstrate courseDAO operations ---
            courseDAO courseDAO = new courseDAO(connection);

            // 1. Add a new course
            System.out.println("--- Adding a new course ---");
            course newcourse = new course("CS101", "Intro to Programming");
            try {
                if (courseDAO.addCourse(newcourse)) {
                    System.out.println("course added: " + newcourse);
                } else {
                    System.out.println("Failed to add course: " + newcourse);
                }
            } catch (SQLException e) {
                System.err.println("Error adding course: " + e.getMessage());
                // Handle specific errors like unique constraint violation if needed
            }
            System.out.println();

            // 2. Get a course by course number
            System.out.println("--- Retrieving course CS101 ---");
            try {
                course retrievedcourse = courseDAO.getCourseByCourseNumber("CS101");
                if (retrievedcourse != null) {
                    System.out.println("Retrieved course: " + retrievedcourse);
                } else {
                    System.out.println("course CS101 not found.");
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving course: " + e.getMessage());
            }
            System.out.println();

            // 3. Add another course to demonstrate getAllcourses
            System.out.println("--- Adding another course (CS201) ---");
            course anothercourse = new course("CS201", "Data Structures");
            try {
                if (courseDAO.addCourse(anothercourse)) {
                    System.out.println("course added: " + anothercourse);
                } else {
                    System.out.println("Failed to add course: " + anothercourse);
                }
            } catch (SQLException e) {
                System.err.println("Error adding course: " + e.getMessage());
            }
            System.out.println();

            // 4. Get all courses
            System.out.println("--- Retrieving all courses ---");
            try {
                List<course> allcourses = courseDAO.getAllCourses();
                if (allcourses.isEmpty()) {
                    System.out.println("No courses found in the database.");
                } else {
                    System.out.println("All courses:");
                    for (course course : allcourses) {
                        System.out.println(course);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving all courses: " + e.getMessage());
            }
            System.out.println();

            // 5. Update a course
            System.out.println("--- Updating course CS101 ---");
            newcourse.setTitle("poop"); // Update the title
            try {
                if (courseDAO.updateCourse(newcourse)) {
                    System.out.println("course updated: " + newcourse);
                    // Verify update
                    course updatedcourse = courseDAO.getCourseByCourseNumber("CS101");
                    System.out.println("Verified updated course: " + updatedcourse);
                } else {
                    System.out.println("Failed to update course: " + newcourse);
                }
            } catch (SQLException e) {
                System.err.println("Error updating course: " + e.getMessage());
            }
            System.out.println();

            // 6. Delete a course
            System.out.println("--- Deleting course CS201 ---");
            try {
                if (courseDAO.deleteCourse("CS201")) {
                    System.out.println("course CS201 deleted successfully.");
                    // Verify deletion
                    course deletedcourse = courseDAO.getCourseByCourseNumber("CS201");
                    if (deletedcourse == null) {
                        System.out.println("Verification: course CS201 no longer exists.");
                    }
                } else {
                    System.out.println("Failed to delete course CS201.");
                }
            } catch (SQLException e) {
                System.err.println("Error deleting course: " + e.getMessage());
            }
            System.out.println();

            System.out.println("Demonstration complete.");

        } catch (Exception e) {
            System.err.println("CONNECTION ERROR:");
            System.err.println(e);
        }
    }
}