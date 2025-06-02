package org.ivc.dbms.Main.classes;

public class elective {
    private String major;
    private String CourseName;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String CourseName) {
        this.CourseName = CourseName;
    }

    public elective(String major, String courseName) {
        this.major = major;
        CourseName = courseName;
    }

}
