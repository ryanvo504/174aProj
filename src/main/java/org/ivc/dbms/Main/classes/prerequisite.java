package org.ivc.dbms.Main.classes;

public class prerequisite {
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
