package org.ivc.dbms.Main.classes;

public class course {

    private String courseNumber;
    private String title;

    public course() {
    }

    public course(String courseNumber, String title) {
        this.courseNumber = courseNumber;
        this.title = title;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "course{" +
               "courseNumber='" + courseNumber + '\'' +
               ", title='" + title + '\'' +
               '}'; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        course course = (course) o;
        return courseNumber.equals(course.courseNumber);
    }

    @Override
    public int hashCode() {
        return courseNumber.hashCode();
    }

}
