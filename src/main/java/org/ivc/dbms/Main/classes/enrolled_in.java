package org.ivc.dbms.Main.classes;

public class enrolled_in {
    private Integer permNumber;
    private Integer enrollmentCode;
    private String quarter;



    public enrolled_in() {
    }
    public enrolled_in(Integer permNumber, int enrollmentCode, String quarter) {
        this.permNumber = permNumber;
        this.enrollmentCode = enrollmentCode;
        this.quarter = quarter;
    }
    public Integer getPermNumber() {
        return permNumber;
    }
    public void setPermNumber(Integer permNumber) {
        this.permNumber = permNumber;
    }
    public Integer getEnrollmentCode() {
        return enrollmentCode;
    }
    public void setEnrollmentCode(Integer enrollmentCode) {
        this.enrollmentCode = enrollmentCode;
    }
    public String getQuarter() {
        return quarter;
    }
    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
