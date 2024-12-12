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
    public void start(Stage tableStage) {
    GridPane gridPane = new GridPane();
    gridPane.setGridLinesVisible(true); 

    
    String[] days = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
    for (int col = 1; col <= days.length; col++) {
        Label dayLabel = new Label(days[col - 1]);
        dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-pref-width: 100;");
        gridPane.add(dayLabel, col, 0);
    }

    
    String[] times = {
        "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
        "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
    };
    for (int row = 1; row <= times.length; row++) {
        Label timeLabel = new Label(times[row - 1]);
        timeLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-pref-width: 80;");
        gridPane.add(timeLabel, 0, row);
    }

    
    for (int col = 0; col <= days.length; col++) {
        for (int row = 0; row <= times.length; row++) {
            StackPane cell = new StackPane();
            cell.setStyle("-fx-border-color: lightgray; -fx-border-width: 0.5; -fx-pref-height: 40; -fx-pref-width: 100;");
            gridPane.add(cell, col, row);
        }
    }

    // Gpt yazdığı test cases
    addClassToGrid(gridPane, "CE 323", 2, 1, 1); // Salı 08:30-10:20
    addClassToGrid(gridPane, "EEE 242", 2, 5, 1); // Salı 12:10-13:05
    addClassToGrid(gridPane, "CE 315", 4, 1, 1); // Perşembe 08:30-09:25
    addClassToGrid(gridPane, "MATH 250", 5, 4, 1); // Cuma 11:15-13:05
    addClassToGrid(gridPane, "SE 302", 5, 8, 1); // Cuma 14:00-16:45

    gridPane.setHgap(1);
    gridPane.setVgap(1);
    gridPane.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: white;");

    Scene scene = new Scene(gridPane, 900, 600);
    tableStage.setScene(scene);
    tableStage.setTitle("Timetable");
    tableStage.show();
}


    private void addClassToGrid(GridPane gridPane, String className, int col, int row, int rowSpan) {
        Button classButton = new Button(className);
        classButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue; -fx-alignment: center;");
        classButton.setOnMouseEntered(event -> classButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightgreen;"));
        classButton.setOnMouseExited(event -> classButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue;"));

        
        classButton.setOnAction(event -> {
            System.out.println("Button clicked for: " + className);
            testTab();
        });
        
        gridPane.add(classButton, col, row, 1, rowSpan);
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
