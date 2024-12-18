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

    public Course(String code, String lecturer, String timing, int durationHours, String classroom, List<Student> enrolledStudents) {
        this.code = code;
        this.lecturer = lecturer;
        setDayAndTime(timing);
        this.classroom = classroom;
        this.durationHours = durationHours;
        this.enrolledStudents = enrolledStudents != null ? new ArrayList<>(enrolledStudents) : new ArrayList<>();
    }

    public Course(String code, String lecturer, String timing, int durationHours, String classroomName) {
        this(code, lecturer, timing, durationHours, classroomName, new ArrayList<>());
    }
    public Course(String code, String lecturer, String timing, int durationHours, List<Student> enrolledStudents) {
        this.code = code;
        this.lecturer = lecturer;
        setDayAndTime(timing);
        this.classroom = null;
        this.durationHours = durationHours;
        this.enrolledStudents = enrolledStudents != null ? new ArrayList<>(enrolledStudents) : new ArrayList<>();
    }
    
    public String toCsvString() {
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(";")
          .append(day).append(" ").append(time).append(";")
          .append(durationHours).append(";")
          .append(lecturer).append(";");

        // Append enrolled students' names
        for (Student student : enrolledStudents) {
            sb.append(student.getName()).append(";");
        }

        return sb.toString();
    }
    
    public void addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            student.addCourse(this); 
        }
    }

    
    public void removeStudent(Student student) {
        if (enrolledStudents.remove(student)) {
            student.removeCourse(this); 
        }
    }

    
    public List<Student> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents); 
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

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", classroom='" + classroom + '\'' +
                ", durationHours=" + durationHours +
                ", enrolledStudents=" + enrolledStudents +
                '}';
    }
}
