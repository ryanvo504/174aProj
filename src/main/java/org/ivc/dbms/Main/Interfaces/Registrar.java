package org.ivc.dbms.Main.Interfaces;

public class Registrar {
    // This class can be used to define methods and properties related to the registrar's functionalities.
    // For example, methods to manage course registrations, prerequisites, and major requirements can be added here.

    public Registrar() {
        // Constructor logic can be added here if needed.
    }

    // Example method to register a student for a course
    public void registerStudentForCourse(String studentId, String courseNumber) {
        // Logic to register the student for the specified course
    }

    public void dropStudentFromCourse(String studentId, String courseNumber) {
        // Logic to drop the student from the specified course
    }

    public void listCoursesForStudent(String studentId) {
        // Logic to list all courses for the specified student
    }

    public void listStudentsInCourse(String courseNumber) {
        // Logic to list all students enrolled in the specified course
    }
    
    public void enterMultipleCourseGrades(String file){

    }

    public void enterCourseGrade(String studentId, String courseNumber, String grade) {
        // Logic to enter a grade for a student in a specific course
    }

    public void requestTranscript(String studentId) {
        // Logic to request a transcript for the specified student
    }

    public void generateGradeMailer(String courseNumber) {
        // Logic to generate a grade mailer for the specified course
    }

    
}
