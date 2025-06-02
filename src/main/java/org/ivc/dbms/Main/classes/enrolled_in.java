package org.ivc.dbms.Main.classes;

public class enrolled_in {
    private Integer permNumber;
    private String courseNumber;


    public Integer getPermNumber() {
        return permNumber;
    }
    public void getPermNumber(Integer permNumber) {
        this.permNumber = permNumber;
    }
    public String getcourseNumber() {
        return courseNumber;
    }
    public void setcourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }
    @Override
    public String toString() {
        return "enrolled_in [studentId=" + permNumber + ", courseNumber=" + courseNumber + "]";
    }
    public enrolled_in(Integer permNumber, String courseNumber) {
        this.permNumber = permNumber;
        this.courseNumber = courseNumber;
    }
}
