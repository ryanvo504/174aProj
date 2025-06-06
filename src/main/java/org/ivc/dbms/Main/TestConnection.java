/* Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.*/
/*
   DESCRIPTION    
   This code sample shows how to use JDBC and the OracleDataSource API to establish a
   connection to your database.
   This is adapted from an official Oracle sample project
   (https://github.com/oracle-samples/oracle-db-examples/blob/main/java/jdbc/ConnectionSamples/DataSourceSample.java)
   to suit the needs of your CS174A project.
    
    Step 1: Download the Zipped JDBC driver (ojdbc11.jar) and Companion Jars from this
            link:
            https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html
            Extract the zipped contents into the lib folder. This allows your code to
            interface properly with JDBC.
    Step 2: Enter the database details (DB_USER, DB_PASSWORD and DB_URL) in this file.
            Note that DB_URL will require you to know the path to your connection
            wallet.
    Step 3: Run the file with "java -cp lib/ojdbc11.jar ./src/TestConnection.java"
 */

 package org.ivc.dbms.Main;

import java.io.IOException;
 import java.sql.Connection;
 import java.sql.DatabaseMetaData;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

import org.ivc.dbms.Main.DAOs.studentDAO;
import org.ivc.dbms.Main.Interfaces.Gold;
import org.ivc.dbms.Main.Interfaces.Registrar;
import org.ivc.dbms.Main.classes.student;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
 
 public class TestConnection {
     // The recommended format of a connection URL is:
     // "jdbc:oracle:thin:@<DATABASE_NAME_LOWERCASE>_tp?TNS_ADMIN=<PATH_TO_WALLET>"
     // where
     // <DATABASE_NAME_LOWERCASE> is your database name in lowercase
     // and
     // <PATH_TO_WALLET> is the path to the connection wallet on your machine.
     // NOTE: on a Mac, there's no C: drive...
     final static String DB_URL = "jdbc:oracle:thin:@avp4b59m3xioruwk_tp?TNS_ADMIN=c:/Wallet_AVP4B59M3XIORUWK";

     final static String DB_USER = "ADMIN";
     final static String DB_PASSWORD = "DavidJoynerRyanVo011!";
 
     // This method creates a database connection using
     // oracle.jdbc.pool.OracleDataSource.



        public static void main(String args[]) throws SQLException {
            Properties info = new Properties();
            System.out.println(DB_URL);

    
            System.out.println("Initializing connection properties...");
            info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
            info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
            info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");
    
            //System.out.println("Creating OracleDataSource...");
            OracleDataSource ods = new OracleDataSource();
    
            //System.out.println("Setting connection properties...");
            ods.setURL(DB_URL);
            ods.setConnectionProperties(info);
    
            // With AutoCloseable, the connection is closed automatically
            try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
               // System.out.println("Connection established!");
                // Get JDBC driver name and version
                DatabaseMetaData dbmd = connection.getMetaData();
                //System.out.println("Driver Name: " + dbmd.getDriverName());
               // System.out.println("Driver Version: " + dbmd.getDriverVersion());
                // Print some connection properties
                System.out.println(
                    "Default Row Prefetch Value: " + connection.getDefaultRowPrefetch()
                );
                //System.out.println("Database username: " + connection.getUserName());
                //System.out.println();
                // Perform some database operations
                System.out.println("Select an Interface to test:\n1) Gold\n2) Registrar\n");
                int selection = 1;
                Scanner scanner = new Scanner(System.in);
                selection = scanner.nextInt();
                
                switch (selection) {
                    case 1 -> GoldInterface(connection);
                    case 2 -> RegistrarInterface(connection);
                    default -> System.out.println("Invalid selection.");
                }
            } catch (Exception e) {
                System.out.println("CONNECTION ERROR:");
                System.out.println(e);
            }

        }
    
     public static void GoldInterface(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int studentId;
        int pin;
        System.out.println("Welcome to Gold\nPlease enter your student ID and pin (one at a time):");
        studentId = scanner.nextInt();
        pin = scanner.nextInt();
        studentDAO studentDAO = new studentDAO(connection);
        Optional<student> student = studentDAO.getStudentByPermNumberAndPin(studentId, pin);
        if(student.isEmpty()){
            System.out.println("Invalid student ID or pin");
            return;
        }
        
        System.out.println("You have successfully logged in as student: " + student.get().getName());
        System.out.println("""
            Gold Interface:
            1.) Add Course
            2.) drop Course
            3.) List Enrolled Courses
            4.) List Grads From Quarter
            5.) Requirement Check
            6.) Make Graduation Plan
            7.) Change Pin
            0.) Exit Gold Interface"""
            );        
        
        
        int selection;
        selection = scanner.nextInt();
        Gold gold = new Gold(connection);
        while(selection != 0){
            switch (selection) {
                case 1 ->                     {
                        System.out.println("Enter code, quarter,year (one at a time)");
                        int code = scanner.nextInt();
                        String quarter = scanner.next();
                        String year = scanner.next();
                        gold.addCourse(studentId, pin, code, year + " " + quarter);
                    }
                case 2 ->                     {
                        System.out.println("code, quarter,year (one at a time)");
                        int code = scanner.nextInt();
                        String quarter = scanner.next();
                        String year = scanner.next();
                        gold.dropCourse(studentId, pin, code, year + " " + quarter);
                    }
                case 3 ->                     {
                        System.out.println("Enter Quarter, year (one at a time)");
                        String quarter = scanner.next();
                        String year = scanner.next();
                        gold.viewSchedule(studentId, pin, year + " " + quarter);
                    }
                case 4 -> {
                        System.out.println("Enter Quarter, year (one at a time)");
                        String quarter = scanner.next();
                        String year = scanner.next();
                        gold.ViewQuarter(studentId, pin, year + " " + quarter);
                }
                case 5 -> {
                        gold.viewRequirements(studentId, pin);
                }
                case 6 -> {
                    gold.generatePlan(studentId, pin);
                }
                case 7 -> {
                    System.out.println("Enter new pin:");
                    Integer newPin = scanner.nextInt();
                    gold.changePin(studentId, pin, newPin);
                    pin = newPin; // Update pin for further operations
                }
                default -> System.out.println("Invalid selection.");
            }
            
            System.out.println("""


            Gold Interface:
            1.) Add Course
            2.) drop Course
            3.) List Enrolled Courses
            4.) List Grads From Quarter
            5.) Requirement Check
            6.) Make Graduation Plan
            7.) Change Pin
            0.) Exit Gold Interface"""
            );
            selection = scanner.nextInt();

        }

        System.out.println("Exiting Gold Interface.");
     }
  

     public static void RegistrarInterface(Connection connection) throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        Registrar registar = new Registrar(connection);
        int selection = 1;
        System.out.println("Welcome to Registrar Interface\nPlease enter your selection:");

            System.out.println("""
                               Registrar Interface:
                               1.) Add Student
                               2.) Drop Student
                               3.) List student Enrollments
                               4.) List Student Grades
                               5.) Generate class list
                               6.) Enter Grades
                               7.) Transcript
                               8.) Mail Grades
                               0.) Exit Registrar Interface""");
            selection = scanner.nextInt();

        while(selection != 0){

            switch (selection) {
                case 1 -> {
                    System.out.println("Enter student_Id, enrollment code, quarter year");
                    int id = scanner.nextInt();
                    int enrollmentCode = scanner.nextInt();
                    String quarter = scanner.next();
                    String year = scanner.next();
                    registar.addStudentToCourse(id, enrollmentCode, year + " " + quarter);
                }
                case 2 -> {
                    System.out.println("Enter student_Id, enrollment code, quarter, year");
                    int id = scanner.nextInt();
                    int enrollmentCode = scanner.nextInt();
                    String quarter = scanner.next();
                    String year = scanner.next();
                    registar.dropStudentFromCourse(id, enrollmentCode, year + " " + quarter);
                }
                case 3 -> {
                    System.out.println("Enter student_id, quarter, year");
                    int id = scanner.nextInt();
                    String quarter = scanner.next();
                    String year = scanner.next();
                    registar.listStudentCourses(id, year + " " + quarter);
                }
                case 4 -> {
                    System.out.println("Enter student_id, quarter, year");
                    int id = scanner.nextInt();
                    String quarter = scanner.next();
                    String year = scanner.next();
                    registar.listPreviousQuarterGrades(id, year + " " + quarter);
                }
                case 5 -> {
                    System.out.println("Enter enrollment code, quarter, year");
                    int enrollmentCode = scanner.nextInt();
                    String quarter = scanner.next();
                    String year = scanner.next();
                    registar.generateClassList(enrollmentCode, year + " " + quarter);
                }
                case 6 ->{
                    System.out.println("Enter enrollment code, quarter, year, fileName");
                    int enrollmentCode = scanner.nextInt();
                    String quarter = scanner.next();
                    String year = scanner.next();
                    String file = scanner.next();

                    registar.enterGradesFromFile(enrollmentCode, year + " " + quarter, file);
                }
                case 7 -> {
                    System.out.println("Enter student_id");
                    int id = scanner.nextInt();
                    registar.requestTranscript(id);
                }
                case 8 -> {
                    System.out.println("Enter Quarter, year");
                    String quarter = scanner.next();
                    String year = scanner.next();

                    registar.generateGradeMailers(year + " " + quarter);
                }
                case 0 -> System.out.println("Exiting Registrar Interface.");
                default -> System.out.println("Invalid selection.");
            }

            System.out.println("""
                               Registrar Interface:
                               1.) Add Student
                               2.) Drop Student
                               3.) List student Enrollments
                               4.) List Student Grades
                               5.) Generate class list
                               6.) Enter Grades
                               7.) Transcript
                               8.) Mail Grades
                               0.) Exit Registrar Interface""");
            selection = scanner.nextInt();
        }


    }
  
  
  
  
     // Inserts another TA into the Instructors table.
     public static void insertTA(Connection connection) throws SQLException {
         System.out.println("Preparing to insert TA into Instructors table...");
         // Statement and ResultSet are AutoCloseable and closed automatically. 
         try (Statement statement = connection.createStatement()) {
             try (
                 ResultSet resultSet = statement.executeQuery(
                     "INSERT INTO INSTRUCTORS VALUES (3, 'Momin Haider', 'TA')"
                 )
             ) {}
         } catch (Exception e) {
             System.out.println("ERROR: insertion failed.");
             System.out.println(e);
         }
     }
 
     // Displays data from Instructors table.
     public static void printInstructors(Connection connection) throws SQLException {
         // Statement and ResultSet are AutoCloseable and closed automatically. 
         try (Statement statement = connection.createStatement()) {
             try (
                 ResultSet resultSet = statement.executeQuery(
                     "SELECT * FROM INSTRUCTORS"
                 )
             ) {
                 System.out.println("INSTRUCTORS:");
                 System.out.println("I_ID\tI_NAME\t\tI_ROLE");
                 while (resultSet.next()) {
                     System.out.println(
                         resultSet.getString("I_ID") + "\t"
                         + resultSet.getString("I_NAME") + "\t"
                         + resultSet.getString("I_ROLE")
                     );
                 }
             }
         } catch (Exception e) {
             System.out.println("ERROR: selection failed.");
             System.out.println(e);
         }
     }
 }