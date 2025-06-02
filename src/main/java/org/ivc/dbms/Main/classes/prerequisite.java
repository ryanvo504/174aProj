package org.ivc.dbms.Main.classes;

public class prerequisite {
    private String prerequisiteNumber;  // The prerequisite course number

    public prerequisite() {
    }

    public prerequisite(String prerequisiteNumber) {
        this.prerequisiteNumber = prerequisiteNumber;
    }

    public String getPrerequisiteNumber() {
        return prerequisiteNumber;
    }

    public void setPrerequisiteNumber(String prerequisiteNumber) {
        this.prerequisiteNumber = prerequisiteNumber;
    }

    @Override
    public String toString() {
        return "Prerequisite [prerequisiteNumber=" + prerequisiteNumber + "]";
    }
}