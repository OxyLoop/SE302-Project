public class Course {
    private String name;
    private String code;
    private String lecturer;
    private String timing;
    private String classroom;
// push test

    public Course(String name, String code, String lecturer, String timing, String classroom) {
        this.name = name;
        this.code = code;
        this.lecturer = lecturer;
        this.timing = timing;
        this.classroom = classroom;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
