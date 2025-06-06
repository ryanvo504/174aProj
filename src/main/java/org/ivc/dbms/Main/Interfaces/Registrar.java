package org.ivc.dbms.Main.Interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ivc.dbms.Main.DAOs.courseDAO;
import org.ivc.dbms.Main.DAOs.enrolled_inDAO;
import org.ivc.dbms.Main.DAOs.has_completedDAO;
import org.ivc.dbms.Main.DAOs.offeredDAO;
import org.ivc.dbms.Main.DAOs.studentDAO;
import org.ivc.dbms.Main.classes.course;
import org.ivc.dbms.Main.classes.enrolled_in;
import org.ivc.dbms.Main.classes.has_completed;
import org.ivc.dbms.Main.classes.offered;
import org.ivc.dbms.Main.classes.student;

/**
 * Main interface for Registrar's Office operations.
 * This class provides a clean interface for all registrar staff operations.
 */
public class Registrar {
    private final RegistrarStaffInterface staffInterface;
    private final courseDAO courseDao;
    private final studentDAO studentDao;
    private final offeredDAO myOfferedDAO;
    private final enrolled_inDAO myEnrolledInDAO;
    private final has_completedDAO myHasCompletedDAO;
    private final Connection connection;
    private final String currentQuarter;

    public Registrar(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
        this.staffInterface = new RegistrarStaffImpl(connection);
        this.courseDao = new courseDAO(connection);
        this.studentDao = new studentDAO(connection);
        this.myOfferedDAO = new offeredDAO(connection);
        this.myEnrolledInDAO = new enrolled_inDAO(connection);
        this.myHasCompletedDAO = new has_completedDAO(connection);
        this.currentQuarter = "S 25";
    }

    /**
     * Adds a student to a course.
     * @param permNumber Student's perm number
     * @param courseNumber Course number
     * @return true if successful, false otherwise
     */
    public boolean addStudentToCourse(int permNumber, int code, String quarter) {
        try {
            // Verify student exists
            if(!quarter.equals(currentQuarter)) {
                System.err.println("Cannot enroll student in course for quarter " + quarter + " as it is not the current quarter.");
                return false;
            }

            Optional<student> student = studentDao.getStudentByPermNumber(permNumber);
            if (student.isEmpty()) {
                System.err.println("Student with perm number " + permNumber + " not found");
                return false;
            }
            Optional<offered> offered = myOfferedDAO.getOfferedByPrimaryKey(code, quarter);
            if(offered.isEmpty()) {
                System.err.println("Offered course with code " + code + " and quarter " + quarter + " not found");
                return false;
            }
            List<enrolled_in> enrolledCourses = myEnrolledInDAO.getEnrolledInByPermAndQuarter(permNumber, quarter);
            if(enrolledCourses.stream().anyMatch(e -> e.getEnrollmentCode() == code && e.getQuarter().equals(quarter))) {
                System.err.println("Student with perm number " + permNumber + " is already enrolled in course " + code + " for quarter " + quarter);
                return false;
            }

            if(enrolledCourses.size() >= 5) {
                System.err.println("Student with perm number " + permNumber + " cannot enroll in more than 5 courses per quarter.");
                return false;
            }


            myEnrolledInDAO.addEnrolledIn(new enrolled_in(permNumber, code, quarter));
            System.out.println("Student with perm number " + permNumber + " successfully added to course " + code + " for quarter " + quarter);
            return true;
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
    public boolean dropStudentFromCourse(int permNumber, int enrollmentCode, String quarter) {
        try {

            if(!quarter.equals(currentQuarter)) {
                System.err.println("Cannot enroll student in course for quarter " + quarter + " as it is not the current quarter.");
                return false;
            }

            // Verify student exists
            Optional<student> student = studentDao.getStudentByPermNumber(permNumber);
            if (student.isEmpty()) {
                System.err.println("Student with perm number " + permNumber + " not found");
                return false;
            }

            // Verify course exists
            Optional<offered> offered = myOfferedDAO.getOfferedByPrimaryKey(enrollmentCode, quarter);
            if (offered.isEmpty()) {
                System.err.println("Offered course with code " + enrollmentCode + " and quarter " + quarter + " not found");
                return false;
            }
            List<enrolled_in> enrolledCourses = myEnrolledInDAO.getEnrolledByPERM_NUMBER(permNumber);
            if (enrolledCourses.stream().noneMatch(e -> e.getEnrollmentCode() == enrollmentCode && e.getQuarter().equals(quarter))) {
                System.err.println("Student with perm number " + permNumber + " is not enrolled in course " + enrollmentCode + " for quarter " + quarter);
                return false;
            }
            List<enrolled_in> enrollmentList = myEnrolledInDAO.getEnrolledInByPermAndQuarter(permNumber, quarter);
            if(enrollmentList.size() <= 1){
                System.err.println("Student with perm number " + permNumber + " cannot drop course " + enrollmentCode + " for quarter " + quarter + " as they are enrolled in only this course.");
                return false;
            }
        
            myEnrolledInDAO.deleteEnrolledIn(permNumber, enrollmentCode, quarter);
            System.out.println("Student with perm number " + permNumber + " successfully dropped from course " + enrollmentCode + " for quarter " + quarter);
            return true;
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
    public List<String> listStudentCourses(int permNumber, String quarter) {
        try {
            // Verify student exists
            Optional<student> student = studentDao.getStudentByPermNumber(permNumber);
            if (!student.isEmpty()) {
            } else {
                System.err.println("Student with perm number " + permNumber + " not found");
                return List.of();
            }

            List<enrolled_in> enrolledCourses = myEnrolledInDAO.getEnrolledInByPermAndQuarter(permNumber, quarter);
            if (enrolledCourses.isEmpty()) {
                System.err.println("No courses found for student with perm number " + permNumber + " in quarter " + quarter);
                return List.of();
            }
            
            System.out.println("Courses for student with perm number " + permNumber + " in quarter " + quarter + ":");
            for(enrolled_in enrolled : enrolledCourses) {
                offered offeredCourse = myOfferedDAO.getOfferedByPrimaryKey(enrolled.getEnrollmentCode(), enrolled.getQuarter())
                    .orElseThrow(() -> new SQLException("Offered course not found for code " + enrolled.getEnrollmentCode() + " and quarter " + enrolled.getQuarter()));
                System.out.println("Course: " + offeredCourse.getCourseNumber() + "Professor: " + offeredCourse.getProfessor().getName() + " Info: " + offeredCourse.getBuilding().getBuildingCode());
            
            }


            // Convert enrolled_in objects to course numbers
            return enrolledCourses.stream()
                .map(enrolled -> String.valueOf(enrolled.getEnrollmentCode()))
                .toList();

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
    public List<has_completed> listPreviousQuarterGrades(int permNumber, String quarter) {
        try {
            // Verify student exists
            Optional<student> student = studentDao.getStudentByPermNumber(permNumber);
            if (student.isEmpty()) {
                System.err.println("Student with perm number " + permNumber + " not found");
                return List.of();
            }
            List<has_completed> previousCourses = myHasCompletedDAO.getCompletedByStudentIdAndQuarter(permNumber, quarter);
            if (previousCourses.isEmpty()) {
                System.err.println("No previous quarter grades found for student with perm number " + permNumber);
                return List.of();
            }
            System.out.println("Previous quarter grades for student with perm number " + permNumber + " in " + quarter + " :");
            for (has_completed completed : previousCourses) {
                course courseInfo = courseDao.getCourseByCourseNumber(completed.getCourseName())
                    .orElseThrow(() -> new SQLException("Course not found for number " + completed.getCourseName()));
                System.out.println("Course: " + courseInfo.getCourseNumber() + " Grade: " + completed.getGrade());
            }


            return previousCourses;
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
    public List<String> generateClassList(int enrollmentCode, String quarter) {
        try {
            // Verify course exists
            Optional<offered> course = myOfferedDAO.getOfferedByPrimaryKey(enrollmentCode, quarter);
            if (course.isEmpty()) {
                System.err.println("Course with enrollment code " + enrollmentCode + " and quarter " + quarter + " not found");
                return List.of();
            }

            List<enrolled_in> enrolledStudents = myEnrolledInDAO.getEnrolledByCodeAndQuarter(enrollmentCode, quarter);
            if (enrolledStudents.isEmpty()) {
                System.err.println("No students enrolled in course " + enrollmentCode + " for quarter " + quarter);
                return List.of();
            }
            List<String> studentPermNumbers = enrolledStudents.stream()
                .map(enrolled -> String.valueOf(enrolled.getPermNumber()))
                .toList();
            List<String> studentNames = studentPermNumbers.stream()
                .map(permNumber -> {
                    try {
                        return studentDao.getStudentByPermNumber(Integer.valueOf(permNumber))
                            .map(student -> student.getName())
                            .orElse("Unknown Student");
                    } catch (SQLException e) {
                        return "Error retrieving student name: " + e.getMessage();
                    }
                })
                .toList();
            System.out.println("Class list for course " + course.get().getCourseNumber() + " in quarter " + quarter + ":");
            for(String permNumber : studentPermNumbers) {
                student studentInfo = studentDao.getStudentByPermNumber(Integer.valueOf(permNumber))
                    .orElseThrow(() -> new SQLException("Student not found for perm number " + permNumber));
                System.out.println("Student: " + studentInfo.getName());
            }

            return studentNames;
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
    public boolean enterGradesFromFile(int enrollmentCode, String quarter, String fileName) throws FileNotFoundException, IOException {
        try {
            // Verify course exists
            Optional<offered> course = myOfferedDAO.getOfferedByPrimaryKey(enrollmentCode, quarter);
            if (course.isEmpty()) {
                System.err.println("Course with enrollment code " + enrollmentCode + " and quarter " + quarter + " not found");
                return false;
            }

            File file = new File("src\\main\\java\\org\\ivc\\dbms\\Main\\" + fileName);
            if (!file.exists() || !file.isFile()) {
                System.err.println("Grade file " + fileName + " does not exist or is not a valid file");
                return false;
            }
            BufferedReader  reader = new BufferedReader(new java.io.FileReader(file));
            String line;
            String courseName = course.get().getCourseNumber();


            while((line = reader.readLine()) != null){
                // Split the line into two parts: perm number and grade.
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.err.println("Invalid line format in grade file: " + line);
                    continue;
                }
                int permNumber = Integer.parseInt(parts[0]);
                String grade = parts[1];
                // Verify student exists
                Optional<student> student = studentDao.getStudentByPermNumber(permNumber);
                if (student.isEmpty()) {
                    System.err.println("Student with perm number " + permNumber + " not found");
                    continue;
                }

                // Check to see if the grade already exists for this student in this course and quarter
                List<has_completed> existingGrades = myHasCompletedDAO.getCompletedByStudentIdAndQuarter(permNumber, quarter);
                if (existingGrades.stream().anyMatch(c -> c.getCourseName().equals(courseName))) {
                    System.err.println("Grade for student with perm number " + permNumber + " in course " + courseName + " for quarter " + quarter + " already exists.");
                    continue;
                }

                myHasCompletedDAO.addCompleted(new has_completed(permNumber, courseName, grade, quarter));
                System.out.println("Grade " + grade + " entered for student with perm number " + permNumber + " in course " + courseName + " for quarter " + quarter);

            }

            reader.close();
            System.out.println("All grades entered successfully from file " + fileName);
            return true;

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
    public List<List<has_completed>> requestTranscript(int permNumber) {
        try {
            // Verify student exists
            Optional<student> student = studentDao.getStudentByPermNumber(permNumber);
            if (student.isEmpty()) {
                System.out.println("Student with perm number " + permNumber + " not found");
                return List.of();
            }

            List<has_completed> completedCourses = myHasCompletedDAO.getCompletedByStudentId(permNumber);
            if (completedCourses.isEmpty()) {
                System.out.println( "No completed courses found for student with perm number " + permNumber);
                return List.of();
            }
            System.out.println("Requesting transcript for student with perm number " + permNumber + ":");
        
            List<List<has_completed>> groupedCourses = completedCourses.stream()
                .collect(Collectors.groupingBy(has_completed::getQuarter))
                .values()
                .stream()
                .toList();

            for (List<has_completed> quarterCourses : groupedCourses) {
                String quarter = quarterCourses.get(0).getQuarter();
                System.out.println("Quarter: " + quarter);
                for (has_completed course : quarterCourses) {
                    System.out.println("Course: " + course.getCourseName() + " Grade: " + course.getGrade());
                }
            }

            return groupedCourses;
            //return staffInterface.requestTranscript(String.valueOf(permNumber));
        } catch (SQLException e) {
            System.err.println("Error requesting transcript: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Generates grade mailers for all students.
     * @return true if successful, false otherwise
     */

    public boolean generateGradeMailers(String quarter) {
        try {
            List<has_completed> completedCourses = myHasCompletedDAO.getCompletedByQuarter(quarter);
            List<List<has_completed>> groupedCourses = completedCourses.stream()
                .collect(Collectors.groupingBy(has_completed::getStudentId))
                .values()
                .stream()
                .toList();
    
            for (List<has_completed> studentCourses : groupedCourses) {
                student studentInfo = studentDao.getStudentByPermNumber(studentCourses.get(0).getStudentId())
                    .orElseThrow(() -> new SQLException("Student not found for perm number " + studentCourses.get(0).getStudentId()));

                System.out.println("Grade mailer for student " + studentInfo.getName() + " (Perm Number: " + studentInfo.getPermNumber() + "):");
                for(has_completed course : studentCourses) {
                    System.out.println("Course: " + course.getCourseName() + " Grade: " + course.getGrade() + " Quarter: " + course.getQuarter());
                }
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error generating grade mailers: " + e.getMessage());
            return false;
        }
    }

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