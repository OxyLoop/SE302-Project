import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
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
    private GridPane gridPane;
    private Stage tableStage;
    private CSVLoader csvLoader;
    private String courseFile;

    public TimetableEntry() {}

    public TimetableEntry(String day, String course, String classroom, String lecturer, String time, SchoolLecturePlanner planner) {
        this.day = day;
        this.course = course;
        this.classroom = classroom;
        this.lecturer = lecturer;
        this.time = time;
        this.planner = planner;
    }

    public TimetableEntry(List<Course> filteredCourses, SchoolLecturePlanner planner, CSVLoader csvLoader, String courseFile) {
        this.filteredCourses = filteredCourses;
        this.planner = planner;
        this.csvLoader = csvLoader;
        this.courseFile = courseFile;
    }

    @Override
    public void start(Stage primaryStage) {
        this.tableStage = primaryStage;

        ScrollPane scrollPane = new ScrollPane();
        this.gridPane = createTimetableGrid();
        scrollPane.setContent(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label title = new Label("Timetable");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");
        layout.getChildren().addAll(title, scrollPane);

        Scene scene = new Scene(layout, 1000, 700);
        tableStage.setScene(scene);
        tableStage.setTitle("Timetable");
        tableStage.show();
    }

    private GridPane createTimetableGrid() {
        for (Course course : filteredCourses) {
            System.out.println("Course: " + course.getCode() + ", Time: " + course.getTime() + ", Day: " + course.getDay());
        }

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        for (int col = 0; col <= 7; col++) {
            ColumnConstraints colConst = new ColumnConstraints(120);
            grid.getColumnConstraints().add(colConst);
        }
        for (int row = 0; row <= 16; row++) {
            RowConstraints rowConst = new RowConstraints(50);
            grid.getRowConstraints().add(rowConst);
        }

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int col = 1; col <= days.length; col++) {
            Label dayLabel = new Label(days[col - 1]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #001f3f; -fx-alignment: center;");
            grid.add(dayLabel, col, 0);
        }

        String[] times = {
            "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
            "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
        };
        for (int row = 1; row <= times.length; row++) {
            Label timeLabel = new Label(times[row - 1]);
            timeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #001f3f; -fx-alignment: center;");
            grid.add(timeLabel, 0, row);
        }

        for (Course course : filteredCourses) {
            int col = getColumnForDay(course.getDay());
            if (col == -1) continue;

            int startRow = getRowForTime(course.getTime());
            if (startRow == -1) continue;

            int duration = course.getDurationHours();
            addClassToGrid(grid, course, col, startRow, duration);
        }

        return grid;
    }

    private void addClassToGrid(GridPane grid, Course course, int col, int startRow, int duration) {
        if (startRow == -1) {
            System.err.println("Invalid time detected for course: " + course.getCode() + " (" + course.getTime() + ")");
            return;
        }

        for (int i = 0; i < duration; i++) {
            Button hourButton = new Button(course.getCode() + " (" + (i + 1) + "h)");
            hourButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue; -fx-alignment: center;");

            hourButton.setOnMouseEntered(event -> hourButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightgreen;"));
            hourButton.setOnMouseExited(event -> hourButton.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue;"));

            hourButton.setOnAction(event -> openEditTab(course));

            grid.add(hourButton, col, startRow + i, 1, 1);
        }
    }

    private void openEditTab(Course course) {
        Stage detailStage = new Stage();

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label detailLabel = new Label("Course Details");
        detailLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label codeLabel = new Label("Course Code: " + course.getCode());
        Label lecturerLabel = new Label("Lecturer: " + course.getLecturer());
        Label dayLabel = new Label("Day: " + course.getDay());
        Label timeLabel = new Label("Time: " + course.getTime());
        Label durationLabel = new Label("Duration: " + course.getDurationHours() + " hour(s)");

        Label studentsLabel = new Label("Enrolled Students:");
        studentsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    
        // Fetch enrolled students
        List<String> enrolledStudentNames = course.getEnrolledStudents().stream()
                                                  .map(Student::getName)
                                                  .sorted(String::compareToIgnoreCase)
                                                  .collect(Collectors.toList());
    
        // Display number of students
        Label studentCountLabel = new Label("Number of Students: " + enrolledStudentNames.size());
        studentCountLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    
        ListView<String> studentListView = new ListView<>();
        studentListView.setStyle("-fx-font-size: 14px;");
        studentListView.getItems().addAll(enrolledStudentNames);
        
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-font-size: 14px; -fx-padding: 5 10;");
        editButton.setOnAction(event -> openEditForm(course, detailStage));

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-padding: 5 10; -fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> {
            course.clearSchedule();
            csvLoader.exportCSV(courseFile, planner.getCourses());
            updateTimetable();
            detailStage.close();
        });

        vbox.getChildren().addAll(
            detailLabel, codeLabel, lecturerLabel, dayLabel, timeLabel, durationLabel, 
            studentsLabel,studentCountLabel, studentListView, editButton, deleteButton
        );

        Scene detailScene = new Scene(vbox, 300, 450);
        detailStage.setScene(detailScene);
        detailStage.setTitle("Course Details: " + course.getCode());
        detailStage.show();
    }

    private void openEditForm(Course course, Stage parentStage) {
        Stage editStage = new Stage();

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label editLabel = new Label("Edit Course Details");
        editLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ChoiceBox<String> dayChoice = new ChoiceBox<>();
        dayChoice.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        dayChoice.setValue(course.getDay());

        ChoiceBox<String> timeChoice = new ChoiceBox<>();
        timeChoice.getItems().addAll(
            "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
            "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
        );
        timeChoice.setValue(course.getTime());

        ChoiceBox<String> durationChoice = new ChoiceBox<>();
        for (int i = 1; i <= 6; i++) {
            durationChoice.getItems().add(i + " hour(s)");
        }
        durationChoice.setValue(course.getDurationHours() + " hour(s)");

        Button saveButton = new Button("Save Changes");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 5 10;");
        saveButton.setOnAction(event -> {
            course.setDay(dayChoice.getValue());
            course.setTime(timeChoice.getValue());
            course.setDurationHours(Integer.parseInt(durationChoice.getValue().split(" ")[0]));
            csvLoader.exportCSV(courseFile, planner.getCourses());
            editStage.close();
            parentStage.close();
            updateTimetable();
        });

        vbox.getChildren().addAll(editLabel, new Label("Day:"), dayChoice, new Label("Time:"), timeChoice, new Label("Duration:"), durationChoice, saveButton);

        Scene editScene = new Scene(vbox, 300, 250);
        editStage.setScene(editScene);
        editStage.setTitle("Edit Course");
        editStage.show();
    }

    private void updateTimetable() {
        gridPane.getChildren().removeIf(node -> node instanceof Button && node.getStyle().contains("lightblue"));

        for (Course course : filteredCourses) {
            if (course.getTime() == null || course.getDay() == null) {
                System.err.println("Course has null time or day: " + course.getCode());
                continue;
            }

            int col = getColumnForDay(course.getDay());
            int startRow = getRowForTime(course.getTime());

            if (col == -1 || startRow == -1) {
                System.err.println("Invalid column or row for course: " + course.getCode());
                continue;
            }

            int duration = course.getDurationHours();
            addClassToGrid(gridPane, course, col, startRow, duration);
        }
    }

    private int getColumnForDay(String day) {
        if (day == null) {
            return -1;
        }

        switch (day) {
            case "Monday": return 1;
            case "Tuesday": return 2;
            case "Wednesday": return 3;
            case "Thursday": return 4;
            case "Friday": return 5;
            case "Saturday": return 6;
            case "Sunday": return 7;
            default: return -1;
        }
    }

    private int getRowForTime(String time) {
        if (time == null) {
            System.err.println("Time is null. Returning -1.");
            return -1;
        }

        String[] times = {
            "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
            "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
        };

        if (time.length() == 4) {
            time = "0" + time;
        }

        for (int i = 0; i < times.length; i++) {
            if (times[i].equals(time)) {
                return i + 1;
            }
        }

        System.err.println("Invalid time: " + time);
        return -1;
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
