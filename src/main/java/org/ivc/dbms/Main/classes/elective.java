package org.ivc.dbms.Main.classes;

public class elective {
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

    public void setCourseId(Long CourseId) {
        this.CourseId = CourseId;
    }

    @Override
    public String toString() {
        return "elective [majorId=" + majorId + ", CourseId=" + CourseId + "]";
    }

    public elective(Long majorId, Long CourseId) {
        this.majorId = majorId;
        this.CourseId = CourseId;
    }

}
