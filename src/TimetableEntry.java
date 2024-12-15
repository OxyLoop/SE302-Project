import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TimetableEntry extends Application {
    private String day;
    private String course;
    private String classroom;
    private String lecturer;
    private String time;
    private List<Course> filteredCourses;
    private SchoolLecturePlanner planner;
    
    
    public TimetableEntry() {
    }

    public TimetableEntry(String day, String course, String classroom, String lecturer, String time) {
        this.day = day;
        this.course = course;
        this.classroom = classroom;
        this.lecturer = lecturer;
        this.time = time;
    }
    public TimetableEntry(List<Course> filteredCourses, SchoolLecturePlanner planner) {
        this.filteredCourses = filteredCourses;
        this.planner = planner;
    }
    
    
    
    @Override
public void start(Stage tableStage) {
    GridPane gridPane = new GridPane();
    gridPane.setGridLinesVisible(true);
    
    for (Course course : filteredCourses) {
        int col = getColumnForDay(course.getDay());
        int startRow = getRowForTime(course.getTime());
        int duration = course.getDurationHours(); // Duration in hours (provided directly by the course)
        
        addClassToGrid(gridPane, course.getCode(), col, startRow, duration);
    }
    

    // Days of the week
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    for (int col = 1; col <= days.length; col++) {
        Label dayLabel = new Label(days[col - 1]);
        dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-pref-width: 100;");
        gridPane.add(dayLabel, col, 0);
    }

    // Time slots
    String[] times = {
        "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
        "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
    };
    for (int row = 1; row <= times.length; row++) {
        Label timeLabel = new Label(times[row - 1]);
        timeLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-pref-width: 80;");
        gridPane.add(timeLabel, 0, row);
    }

    // Empty grid cells
    for (int col = 0; col <= days.length; col++) {
        for (int row = 0; row <= times.length; row++) {
            StackPane cell = new StackPane();
            cell.setStyle("-fx-border-color: lightgray; -fx-border-width: 0.5; -fx-pref-height: 40; -fx-pref-width: 100;");
            gridPane.add(cell, col, row);
        }
    }

    // Add courses to the grid
    for (Course course : filteredCourses) {
        int col = getColumnForDay(course.getDay());
        int startRow = getRowForTime(course.getTime());
        int duration = course.getDurationHours(); // Duration in hours

        // Add the course to the grid
        addClassToGrid(gridPane, course.getCode(), col, startRow, duration);
    }

    // Grid styling
    gridPane.setHgap(1);
    gridPane.setVgap(1);
    gridPane.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: white;");

    Scene scene = new Scene(gridPane, 900, 600);
    tableStage.setScene(scene);
    tableStage.setTitle("Timetable");
    tableStage.show();
}



private void addClassToGrid(GridPane gridPane, String className, int col, int startRow, int duration) {
    startRow++;
    for (int i = 0; i < duration; i++) {
        Button hourButton = new Button(className + " (" + (i + 1) + "h)");
        hourButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue; -fx-alignment: center;");

        hourButton.setOnMouseEntered(event -> hourButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightgreen;"));
        hourButton.setOnMouseExited(event -> hourButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue;"));

        hourButton.setOnAction(event -> {
            System.out.println("Button clicked for: " + className);
            testTab();
        });

        // Add the button for each hour in the correct row
        gridPane.add(hourButton, col, startRow + i, 1, 1);
    }
}



    private void testTab() {
        Stage newWindow = new Stage();
        
        VBox vbox = new VBox(10); 
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;"); 
    
        Label label = new Label("Bunu yazan");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        Button closeButton = new Button("Tosun");
        closeButton.setStyle("-fx-font-size: 14px; -fx-padding: 5 10;");
        closeButton.setOnAction(event -> newWindow.close()); 
    
        vbox.getChildren().addAll(label, closeButton);
    
        Scene newScene = new Scene(vbox, 200, 150); 
        newWindow.setScene(newScene);
        newWindow.setTitle("Test Tab");
        newWindow.show(); 
    }

    private int getColumnForDay(String day) {
        switch (day) {
            case "Monday": return 1;
            case "Tuesday": return 2;
            case "Wednesday": return 3;
            case "Thursday": return 4;
            case "Friday": return 5;
            case "Saturday": return 6;
            case "Sunday": return 7;
            default: return 0;
        }
    }

    private int getRowForTime(String time) {
        switch (time) {
            case "08:30": return 2;
            case "09:25": return 3;
            case "10:20": return 4;
            case "11:15": return 5;
            case "12:10": return 6;
            case "13:05": return 7;
            case "14:00": return 8;
            case "14:55": return 9;
            case "15:50": return 10;
            case "16:45": return 11;
            case "17:40": return 12;
            case "18:35": return 13;
            case "19:30": return 14;
            case "20:25": return 15;
            case "21:20": return 16;
            case "22:15": return 17;
            default: return 0;
        }
    }
    
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
