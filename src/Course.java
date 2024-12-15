import java.util.ArrayList;
import java.util.List;

public class Course {
    
    private String code;
    private String lecturer;
    private String day;
    private String time;
    private String classroom;
    private int durationHours;
    private List<Student> enrolledStudents;

    
    public Course(String code, String lecturer, String timing, int durationHours, String classroomName) {
        this.code = code;
        this.lecturer = lecturer;
        setDayAndTime(timing);
        this.classroom = classroomName;
        this.durationHours = durationHours; 
        this.enrolledStudents = new ArrayList<>(); 
    }

    public void addStudent(Student student) {
        enrolledStudents.add(student);
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    private void setDayAndTime(String timing) {
        String[] splitTiming = timing.split(" "); 
        if (splitTiming.length == 2) {
            this.day = splitTiming[0];
            this.time = splitTiming[1];
        } else {
            this.day = "";
            this.time = timing;  
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getDurationHours() { 
        return durationHours;
    }

    public void setDurationHours(int durationHours) { 
        if (durationHours > 0) {
            this.durationHours = durationHours;
        } else {
            throw new IllegalArgumentException("Duration must be a positive integer.");
        }
    }
}
