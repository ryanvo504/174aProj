package org.ivc.dbms.Main.classes;

public class offered {

    public offered() {
    }

    public offered(int enrollmentCode, quarter quarter, professor professor, building building, int roomNum, String time, int maxEnrollment, String courseNumber) {
        this.enrollmentCode = enrollmentCode;
        this.quarter = quarter;
        this.professor = professor;
        this.building = building;
        this.roomNum = roomNum;
        this.time = time;
        this.maxEnrollment = maxEnrollment;
        this.courseNumber = courseNumber;
    }

    private int enrollmentCode;
    private quarter quarter;
    private professor professor;
    private building building;
    private int roomNum;
    private String time;
    private int maxEnrollment;
    private String courseNumber;
    public int getEnrollmentCode() {
        return enrollmentCode;
    }
    public void setEnrollmentCode(int enrollmentCode) {
        this.enrollmentCode = enrollmentCode;
    }
    public quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(quarter quarter) {
        this.quarter = quarter;
    }

    public professor getProfessor() {
        return professor;
    }

    public void setProfessor(professor professor) {
        this.professor = professor;
    }

    public building getBuilding() {
        return building;
    }

    public void setBuilding(building building) {
        this.building = building;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }
    
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getMaxEnrollment() {
        return maxEnrollment;
    }
    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public String getCourseNumber() {
        return courseNumber;
    }
    public void setCourseNumber(String courseNumber){
        this.courseNumber = courseNumber;
    }

}
