package org.ivc.dbms.Main.classes;

public class enrolled_in {
    private Long studentId;
    private Long courseId;
    public quarter getQuarter() {
        return quarter;
    }
    public void setQuarter(quarter quarter) {
        this.quarter = quarter;
    }

    private quarter quarter;
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
    @Override
    public String toString() {
        return "enrolled_in [studentId=" + studentId + ", courseId=" + courseId + "]";
    }
    public enrolled_in(Long studentId, Long courseId, quarter quarter) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.quarter = quarter;
    }
}
