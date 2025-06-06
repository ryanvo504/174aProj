package org.ivc.dbms.Main.Interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ivc.dbms.Main.DAOs.courseDAO;
import org.ivc.dbms.Main.DAOs.electiveDAO;
import org.ivc.dbms.Main.DAOs.enrolled_inDAO;
import org.ivc.dbms.Main.DAOs.has_completedDAO;
import org.ivc.dbms.Main.DAOs.majorDAO;
import org.ivc.dbms.Main.DAOs.major_requirementDAO;
import org.ivc.dbms.Main.DAOs.offeredDAO;
import org.ivc.dbms.Main.DAOs.prerequisiteDAO;
import org.ivc.dbms.Main.DAOs.quarterDAO;
import org.ivc.dbms.Main.DAOs.studentDAO;
import org.ivc.dbms.Main.classes.course;
import org.ivc.dbms.Main.classes.elective;
import org.ivc.dbms.Main.classes.enrolled_in;
import org.ivc.dbms.Main.classes.has_completed;
import org.ivc.dbms.Main.classes.major_requirement;
import org.ivc.dbms.Main.classes.offered;
import org.ivc.dbms.Main.classes.prerequisite;
import org.ivc.dbms.Main.classes.student;

public class Gold {
    private Connection connection;
    private studentDAO myStudentDao;
    private enrolled_inDAO myEnrolledInDao;
    private prerequisiteDAO myPrerequisiteDao;
    private has_completedDAO myHasCompletedDao;
    private offeredDAO myOfferedDao;
    private quarterDAO myQuarterDao;
    private courseDAO myCourseDao;
    private majorDAO myMajorDao;
    private major_requirementDAO myMajorRequirementDao;
    private electiveDAO myMajorElectiveDao;
    private String currentQaurter;

    public Gold(Connection connection) {
        this.connection = connection;
        myStudentDao = new studentDAO(connection);
        myEnrolledInDao = new enrolled_inDAO(connection);
        myPrerequisiteDao = new prerequisiteDAO(connection);
        myHasCompletedDao = new has_completedDAO(connection);
        myOfferedDao = new offeredDAO(connection);
        myQuarterDao = new quarterDAO(connection);
        myCourseDao = new courseDAO(connection);
        myMajorDao = new majorDAO(connection);
        myMajorRequirementDao = new major_requirementDAO(connection);
        myMajorElectiveDao = new electiveDAO(connection);
        this.currentQaurter = "25 S";
    }

    public void addCourse(Integer studentId, Integer Pin, int code, String quarter) throws SQLException {
        // Logic to add a course for the student
        // This would typically involve checking prerequisites, availability, etc.
        Optional<student> studentOpt = myStudentDao.getStudentByPermNumberAndPin(studentId, Pin);
        List<enrolled_in> current_enrollments = myEnrolledInDao.getEnrolledByPERM_NUMBER(studentId);
        List<has_completed> completedGrades = myHasCompletedDao.getCompletedByStudentId(studentId);
        List<String> completedCourses = myHasCompletedDao.getCompletedCourseIdsByStudentId(studentId);
        List<course> courses = myCourseDao.getAllCourses();
        Optional<offered> offeredCourses = myOfferedDao.getOfferedByPrimaryKey(code, quarter);

        if (offeredCourses.isEmpty()) {
            System.out.println("Course with code " + code + " is not being offered in quarter " + quarter);
            return;
        }
        if(current_enrollments.stream().anyMatch(e -> e.getEnrollmentCode() == code)) {
            System.out.println("Student is already enrolled in this course.");
            return;
        }
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }
        List<prerequisite> prerequisites = myPrerequisiteDao.getPrerequisitesForCourse(offeredCourses.get().getCourseNumber());

        for( prerequisite prereq : prerequisites) {
            if(completedCourses.contains(prereq.getPrerequisiteNumber())) {
                 // Student has completed this prerequisite
            } else {
                System.out.println("Student has not completed the prerequisite: " + prereq.getPrerequisiteNumber());
                return; // Student cannot register for the course
            }
        }

        for(has_completed completed : completedGrades) {
            if("C".equals(completed.getGrade()) || "B".equals(completed.getGrade()) || "A".equals(completed.getGrade())) {
                // Student has completed the prerequisite with a passing grade
                
            } 
            else {
                System.out.println("Student has not completed the prerequisite with a passing grade: " + completed.getCourseName());
                return; // Student cannot register for the course
            }
    
        }

        if(current_enrollments.size() >= 5){
            System.out.println("Student has already reached the maximum number of enrollments allowed.");
            return;
        }

        /* Need one me test to make sure the course which we are attempting to take is actually being offered in the current quarter. */
        if(courses.stream().noneMatch(c -> c.getCourseNumber().equals(offeredCourses.get().getCourseNumber()))) {
            System.out.println("Course " + offeredCourses.get().getCourseNumber() + " does not exist.");
            return;
        }

        enrolled_in newEnrollment = new enrolled_in(studentId, offeredCourses.get().getEnrollmentCode(), quarter);
        myEnrolledInDao.addEnrolledIn(newEnrollment);
        System.out.println("Student " + studentId + " has been registered for course " + offeredCourses.get().getEnrollmentCode() + " in quarter " + quarter);
    }

    public void dropCourse(Integer studentId, Integer Pin, int enrollmentCode, String quarter) throws SQLException {
        // Logic to drop the student from the specified course
        if(!quarter.equals(currentQaurter)){
            System.out.println("Cannot drop course in a different quarter than the current one.");
            return;
        }
        
        Optional<student> studentOpt = myStudentDao.getStudentByPermNumberAndPin(studentId, Pin);


        List<enrolled_in> current_enrollments = myEnrolledInDao.getEnrolledByPERM_NUMBER(studentId);


        if(studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        if(!current_enrollments.stream().anyMatch(e -> e.getEnrollmentCode() == enrollmentCode && e.getQuarter().equals(quarter))) {
            System.out.println("Student is not enrolled in this course.");
            return;
        }

        String courseNumber = myOfferedDao.getOfferedByPrimaryKey(enrollmentCode, quarter)
                .map(offered -> offered.getCourseNumber())
                .orElse(null);

        if(current_enrollments.size() <= 1) {
            System.out.println("Student must be enrolled in at least one course.");
            return;
        }

    
        myEnrolledInDao.deleteEnrolledIn(studentId, enrollmentCode, quarter);
        System.out.println("Student " + studentId + " has been dropped from course " + courseNumber);

    }

    public void viewSchedule(Integer studentId, Integer Pin, String quarter) throws SQLException {
        // Logic to view the student's schedule
        Optional<student> studentOpt = myStudentDao.getStudentByPermNumberAndPin(studentId, Pin);
        
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        List<enrolled_in> enrolledCourses = myEnrolledInDao.getEnrolledByPERM_NUMBER(studentId);
        
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled for student " + studentId);
            return;
        }

        System.out.println("Schedule for Student ID: " + studentId + " in quarter " + quarter + ":");
        for (enrolled_in enrolled : enrolledCourses) {
            if (enrolled.getQuarter().equals(quarter)) {
                Optional<offered> offeredCourse = myOfferedDao.getOfferedByPrimaryKey(enrolled.getEnrollmentCode(), quarter);
                if (offeredCourse.isPresent()) {
                    System.out.println(offeredCourse.get().getCourseNumber() + " "+  offeredCourse.get().getBuilding().getBuildingCode());
                } else {
                    System.out.println("Course with Enrollment Code: " + enrolled.getEnrollmentCode() + " not found.");
                }
            }
        }


    }

    public void ViewQuarter(Integer studentId, Integer Pin, String quarter) throws SQLException {
        // Logic to view the student's completed courses in a specific quarter
        Optional<student> studentOpt = myStudentDao.getStudentByPermNumberAndPin(studentId, Pin);
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }


        List<has_completed> completedGrades = myHasCompletedDao.getCompletedByStudentIdAndQuarter(studentId, quarter);

        if (completedGrades.isEmpty()) {
            System.out.println("No courses completed in this quarter.");
            return;
        }


        System.out.println("Courses completed by Student ID: " + studentId + " in quarter " + quarter + ":");
        for (has_completed completed : completedGrades) {
            System.out.println("Course: " + completed.getCourseName() + ", Grade: " + completed.getGrade());
        }

    }

    public void viewRequirements(Integer studentId, Integer Pin) throws SQLException {
        // Logic to view the student's requirements
        Optional<student> studentOpt = myStudentDao.getStudentByPermNumberAndPin(studentId, Pin);
        
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }
        student myStudent= studentOpt.get();
        String major = myStudent.getdept();



        List<has_completed> completedCourses = myHasCompletedDao.getCompletedByStudentId(studentId);
        List<major_requirement> requirements = myMajorRequirementDao.getMajorRequirementsByMajor(major);
        List<elective> electives = myMajorElectiveDao.getMajorRequirementsByMajor(major);


        List<major_requirement> neededCourses = requirements.stream()
                .filter(req -> completedCourses.stream()
                        .noneMatch(completed -> completed.getCourseName().equals(req.getcourseNumber())))
                .toList();

        

        System.out.println("Completed courses for Student ID: " + studentId + " (Major: " + major + "):");
        for(has_completed completed : completedCourses) {
            System.out.println(completed.getCourseName());
        }


        System.out.println("Requirments in progress for Student ID: " + studentId + " (Major: " + major + "):");
        List<enrolled_in> enrolledCourses = myEnrolledInDao.getEnrolledByPERM_NUMBER(studentId);
        for(enrolled_in enrolled : enrolledCourses) {
            Optional<offered> offeredCourse = myOfferedDao.getOfferedByPrimaryKey(enrolled.getEnrollmentCode(), enrolled.getQuarter());
            if (offeredCourse.isPresent()) {
                System.out.println(offeredCourse.get().getCourseNumber());
            }
        }

        System.out.println("Major requirements (Major: " + major + "):");
        for(major_requirement req : requirements) {
            System.out.println(req.getcourseNumber());
        }

        System.out.println("Major requirements left for Student ID: " + studentId + " (Major: " + major + "):");
        if (neededCourses.isEmpty()) {
            System.out.println("All major requirements completed.");
        } else {
            for (major_requirement req : neededCourses) {
                System.out.println(req.getcourseNumber());
            }
        }

        // If they have taken more than 5 electives, then they are finished with their elective requirements.
        List<elective> completedElectives = completedCourses.stream()
                .filter(completed -> electives.stream()
                        .anyMatch(elective -> elective.getCourseName().equals(completed.getCourseName())))
                .map(completed -> new elective(completed.getCourseName(), major))
                .toList();
        if (completedElectives.size() >= 5) {
            System.out.println("All elective requirements completed.");
        } else {
            System.out.println("Elective requirements left to choose from for Student ID: " + studentId + " (Major: " + major + "):");
            List<elective> unchosenElectives = electives.stream()
                    .filter(elective -> completedCourses.stream()
                            .noneMatch(completed -> completed.getCourseName().equals(elective.getCourseName())))
                    .toList();
            for( elective unchosen : unchosenElectives) {
                System.out.println(unchosen.getCourseName());
            }
        }
    }

    public void generatePlan(Integer studentId, Integer Pin) throws SQLException {
        // Logic to generate a plan for the student
        Optional<student> studentOpt = myStudentDao.getStudentByPermNumberAndPin(studentId, Pin);
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        student myStudent = studentOpt.get();
        String major = myStudent.getdept();
        List<has_completed> completedCourses = myHasCompletedDao.getCompletedByStudentId(studentId);
        List<major_requirement> requirements = myMajorRequirementDao.getMajorRequirementsByMajor(major);
        
        
        if (requirements.isEmpty()) {
            System.out.println("No major requirements found for major: " + major);
            return;
        }
        List<elective> electives = myMajorElectiveDao.getMajorRequirementsByMajor(major);
        if (electives.isEmpty()) {
            System.out.println("No elective requirements found for major: " + major);
            return;
        }

        List<major_requirement> neededCourses = new ArrayList<>(requirements.stream()
                .filter(req -> completedCourses.stream()
                        .noneMatch(completed -> completed.getCourseName().equals(req.getcourseNumber())))
                .toList());

        if(neededCourses.isEmpty()) {
            System.out.println("All major requirements completed for major: " + major);
        }

        
        List<elective> completedElectives = new ArrayList<>(completedCourses.stream()
                .filter(completed -> electives.stream()
                        .anyMatch(elective -> elective.getCourseName().equals(completed.getCourseName())))
                .map(completed -> new elective(major, completed.getCourseName()))
                .toList());

        System.out.println("Plan for Student ID: " + studentId + " (Major: " + major + "):");
        // Use offeringDAO to determine which courses are being offered in the next quarters


        List<offered> offeredCourses = myOfferedDao.getAllOffered();

        List<List<offered>> groupedOfferings = offeredCourses.stream()
            .collect(Collectors.groupingBy(o -> o.getQuarter().getQuarterYr())) // group by the string "24 F", etc.
            .values().stream()
            .toList();

        for(List<offered> offerings : groupedOfferings) {
            // filter the offerings based on the neededCourses and completedElectives (Should only recommend until 5 elecitives will have been completed & there are no more needed courses))
            List<offered> coursesToTake = new ArrayList<>(offerings.stream()
                .filter(offered -> neededCourses.stream()
                        .anyMatch(needed -> needed.getcourseNumber().equals(offered.getCourseNumber()))).toList());

            // filter some possible elective offerings to take (as long as the student has not completed 5 electives)
            if (completedElectives.size() < 5) {                
                List<offered> electiveOfferings = new ArrayList<>(offerings.stream()
                    .filter(offered -> completedElectives.stream()
                            .noneMatch(completed -> completed.getCourseName().equals(offered.getCourseNumber())) && electives.stream().anyMatch(e -> e.getCourseName().equals(offered.getCourseNumber())))
                    .collect(Collectors.toList()));
                int i = 0;
                while( i < electiveOfferings.size() && completedElectives.size() < 5) {
                    coursesToTake.add(electiveOfferings.get(i));
                    completedElectives.add(new elective(major, electiveOfferings.get(i).getCourseNumber()));
                    i++;
                }
            }
        
            if (!coursesToTake.isEmpty()) {
                System.out.println("Courses to take in " + offerings.get(0).getQuarter().getQuarterYr() + ":");
                for (offered course : coursesToTake) {
                    System.out.println(course.getCourseNumber());
                }
            }

            // remove the courses that have been added to the plan from the neededCourses and completedElectives
            neededCourses.removeIf(req -> coursesToTake.stream()
                    .anyMatch(offered -> offered.getCourseNumber().equals(req.getcourseNumber())));
        }
    }

    public void changePin(Integer StudentId, Integer oldPin, Integer newPin) throws SQLException {
        // Logic to change the student's PIN
        Optional<student> studentOpt = myStudentDao.getStudentByPermNumberAndPin(StudentId, oldPin);
        
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found or old PIN is incorrect.");
            return;
        }

        student student = studentOpt.get();
        student.setPin(newPin);
        myStudentDao.updatestudent(studentOpt.get());
        System.out.println("PIN changed successfully for Student ID: " + StudentId);
        
    }

}   
