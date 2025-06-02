package org.ivc.dbms.Main.classes;

public class has_completed {
    private Long studentId;
    private String courseName;
    private char grade;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public char getGrade() {
        return grade;
    }

    public void setGrade(char grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("has_completed{");
        sb.append("studentId=").append(studentId);
        sb.append(", courseId=").append(courseName);
        sb.append(", grade=").append(grade);
        sb.append('}');
        return sb.toString();
    }

    public has_completed(Long studentId, String courseName, char grade) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.grade = grade;
    }



}
