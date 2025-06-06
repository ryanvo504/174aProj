package org.ivc.dbms.Main.classes;

public class has_completed {
    private Integer studentId;
    private String courseName;
    private String quarter;
    private String grade;

    public Integer getStudentId() {
        return studentId;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getQuarter(){
        return quarter;
    }
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
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

    public has_completed(Integer studentId, String courseName, String grade, String quarter) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.grade = grade;
        this.quarter = quarter; // Default value, can be set later if needed
    }



}
