package org.ivc.dbms.Main.classes;

public class has_completed {
    private Long studentId;
    private Long courseId;

    private char grade;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
        sb.append(", courseId=").append(courseId);
        sb.append(", grade=").append(grade);
        sb.append('}');
        return sb.toString();
    }

    public has_completed(Long studentId, Long courseId, char grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }



}
