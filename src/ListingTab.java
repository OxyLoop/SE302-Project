import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ListingTab {

    private SchoolLecturePlanner planner;
    private String type; // "students", "lectures", or "classrooms"
    private VBox courseListContainer;

    public ListingTab(SchoolLecturePlanner planner, String type) {
        this.planner = planner;
        this.type = type;
        this.courseListContainer = new VBox();
    }

    public void show() {
        Stage newWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label(getTitle());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");

        ListView<String> listView = new ListView<>();
        listView.setStyle("-fx-font-size: 14px;");

        List<String> data = fetchData();

        listView.getItems().addAll(data);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            List<String> filteredData = data.stream()
                    .filter(item -> item.toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            listView.getItems().setAll(filteredData);

            if (filteredData.isEmpty()) {
                listView.getItems().add("No items found");
            }
        });

        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && !selectedItem.equals("No items found")) {
                handleItemClick(selectedItem);
            }
        });


        vbox.getChildren().addAll(title, searchBar, listView);

        Scene scene = new Scene(vbox, 400, 500);
        newWindow.setTitle(getTitle());
        newWindow.setScene(scene);
        newWindow.show();
    }

    private String getTitle() {
        switch (type) {
            case "students": return "All Students";
            case "lectures": return "All Lectures";
            case "classrooms": return "All Classrooms";
            default: return "Listing";
        }
    }
    

    private List<String> fetchData() {
        switch (type) {
            case "students":
                return planner.getAllStudentNames().stream()
                        .sorted(String::compareToIgnoreCase)
                        .collect(Collectors.toList());
            case "lectures":
                return planner.getCourses().stream()
                        .map(course -> course.getCode() + " - " + course.getLecturer())
                        .sorted(String::compareToIgnoreCase)
                        .collect(Collectors.toList());
            case "classrooms":
                return planner.getClassrooms().stream()
                        .map(classroom -> classroom.getName() + " (Capacity: " + classroom.getCapacity() + ")")
                        .sorted(String::compareToIgnoreCase)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private void handleItemClick(String selectedItem) {
        switch (type) {
            case "students":
                Student student = planner.findStudentByName(selectedItem);
                if (student != null) {
                    showStudentTimetable(student);
                }
                break;
            case "lectures":
                String lectureCode = selectedItem.split(" - ")[0]; 
                Course course = planner.findCourseByCode(lectureCode);
                if (course != null) {
                    showLectureTimetable(course);
                }
                break;
            case "classrooms":
                String classroomName = selectedItem.split(" \\(Capacity:")[0]; 
                Classroom classroom = planner.findClassroomByName(classroomName);
                if (classroom != null) {
                    showClassroomTimetable(classroom);
                }
                break;
        }
    }

    private void showStudentTimetable(Student student) {
        List<Course> courses = student.getEnrolledCourses();
        TimetableEntry timetableEntry = new TimetableEntry(courses, planner);
        Stage timetableStage = new Stage();
        try {
            timetableEntry.start(timetableStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLectureTimetable(Course course) {
        TimetableEntry timetableEntry = new TimetableEntry(List.of(course), planner);
        Stage timetableStage = new Stage();
        try {
            timetableEntry.start(timetableStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showClassroomTimetable(Classroom classroom) {
        List<Course> courses = planner.getCourses().stream()
                .filter(course -> course.getClassroom() != null && course.getClassroom().equals(classroom.getName()))
                .collect(Collectors.toList());
        TimetableEntry timetableEntry = new TimetableEntry(courses, planner);
        Stage timetableStage = new Stage();
        try {
            timetableEntry.start(timetableStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void refreshCourseList() {
        courseListContainer.getChildren().clear();

        List<Course> courses = planner.getCourses();

        for (Course course : courses) {
            Label courseLabel = new Label(course.getCode() + " - " + course.getLecturer());
            courseListContainer.getChildren().add(courseLabel);
        }

        Scene scene = new Scene(courseListContainer, 300, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Course List");
        stage.show();
    }
}
