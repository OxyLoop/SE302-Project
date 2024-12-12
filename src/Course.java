public class Course {
    
    private String code;
    private String lecturer;
    private String day;
    private String time;
    private String classroom;


    public Course(String code, String lecturer, String timing, String classroomName) {
        this.code = code;
        this.lecturer = lecturer;
        setDayAndTime(timing);
        this.classroom = classroom;
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
}
