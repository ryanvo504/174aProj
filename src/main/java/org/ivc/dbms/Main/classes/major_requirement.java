package org.ivc.dbms.Main.classes;

public class major_requirement {
    private Long majorId;
    private Long CourseId;
    public Long getMajorId() {
        return majorId;
    }
    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }
    public Long getCourseId() {
        return CourseId;
    }
    public void setCourseId(Long courseId) {
        CourseId = courseId;
    }
    @Override
    public String toString() {
        return "major_requirement [majorId=" + majorId + ", CourseId=" + CourseId +"]";
    }

}
