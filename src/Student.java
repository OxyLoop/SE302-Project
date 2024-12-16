import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private List<Course> enrolledCourses;


    public Student(String name) {
        this.name = name;
        this.enrolledCourses = new ArrayList<>();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }


    public void addCourse(Course course) {
        enrolledCourses.add(course);
    }


    public void removeCourse(Course course) {
        enrolledCourses.remove(course);
    }


    public void listCourses() {
        System.out.println("Courses for " + name + ":");
        for (Course course : enrolledCourses) {
            System.out.println(course);
        }
    }


}
