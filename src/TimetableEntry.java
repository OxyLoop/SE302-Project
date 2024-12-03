import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TimetableEntry extends Application {
    private String day;
    private String course;
    private String classroom;
    private String lecturer;
    private String time;
    
    
    public TimetableEntry() {
    }

    public TimetableEntry(String day, String course, String classroom, String lecturer, String time) {
        this.day = day;
        this.course = course;
        this.classroom = classroom;
        this.lecturer = lecturer;
        this.time = time;
    }
    @Override
    public void start(Stage tableStage)  {
        
        GridPane gridPane = new GridPane();
        String[] days = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
        for (int col = 1; col <= days.length; col++) {
            Label dayLabel = new Label(days[col - 1]);
            gridPane.add(dayLabel, col, 0); // Add to first row
        }

        // Add row headers (times)
        String[] times = {
            "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55", 
            "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
        };
        for (int row = 1; row <= times.length; row++) {
            Label timeLabel = new Label(times[row - 1]);
            gridPane.add(timeLabel, 0, row); // Add to first column
        }

        // Add the timetable content
        addClassToGrid(gridPane, "CE 323\nC 508", 2, 1, 3); // Salı 08:30-10:20
        addClassToGrid(gridPane, "EEE 242\nM 202", 2, 5, 2); // Salı 12:10-13:05
        addClassToGrid(gridPane, "CE 315\nC 207", 4, 1, 1); // Perşembe 08:30-09:25
        addClassToGrid(gridPane, "MATH 250\nM 302", 5, 4, 3); // Cuma 11:15-13:05
        addClassToGrid(gridPane, "SE 302\nC 206", 5, 8, 3); // Cuma 14:00-16:45

        // Add styles and spacing
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Create the Scene and set it
        Scene scene = new Scene(gridPane, 800, 600);
        tableStage.setScene(scene);
        tableStage.setTitle("Timetable");
        tableStage.show();
    }
    private void addClassToGrid(GridPane gridPane, String className, int col, int row, int rowSpan) {
        Label classLabel = new Label(className);
        classLabel.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue;");
        gridPane.add(classLabel, col, row, 1, rowSpan); // Add to grid with rowspan
    }

    // Getters and setters
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
