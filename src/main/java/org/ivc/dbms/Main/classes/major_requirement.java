package org.ivc.dbms.Main.classes;

public class major_requirement {
    private String major;
    private String courseNumber;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getcourseNumber() {
        return courseNumber;
    }

    public void setcourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public major_requirement(String major, String courseNumber) {
        this.major = major;
        this.courseNumber = courseNumber;
    }


}
