package org.ivc.dbms.Main.classes;

public class prerequisite {
<<<<<<< HEAD
    private Long courseId;
    private Long prerequisiteId;
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public Long getPrerequisiteId() {
        return prerequisiteId;
    }
    public void setPrerequisiteId(Long prerequisiteId) {
        this.prerequisiteId = prerequisiteId;
    }
    @Override
    public String toString() {
        return "prerequisite [courseId=" + courseId + ", prerequisiteId=" + prerequisiteId + "]";
    }
    public prerequisite(Long courseId, Long prerequisiteId) {
        this.courseId = courseId;
        this.prerequisiteId = prerequisiteId;
    }
}
=======
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
>>>>>>> main
