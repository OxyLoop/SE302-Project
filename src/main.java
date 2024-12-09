
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class main extends Application {
    @Override
    //MAIN TAB FOR APPLICATION
    public void start(Stage primaryStage) {
     BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #001f3f; -fx-padding: 20;");

        Button addCourseButton = new Button("Add Course");
        addCourseButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        addCourseButton.setOnAction(event -> openAddCourseWindow());
        BorderPane.setAlignment(addCourseButton, Pos.TOP_LEFT);
        root.setTop(addCourseButton);

        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-alignment: center;");

        Label title = new Label("Timetable");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-padding: 10;"
        );

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for classes");
        searchBar.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-alignment: center;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 50 5 50;"
        );
        searchBar.setPrefColumnCount(10);

        Button searchButton = new Button("Search");
        searchButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        searchButton.setOnAction(event -> {
            String searchText = searchBar.getText();
            searchWindow(searchText);
        });

        vbox.getChildren().addAll(title, searchBar, searchButton);

        root.setCenter(vbox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Timetable");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //SEARCHED CLASS TAB
    private void searchWindow(String searchText) {
        Stage newWindow = new Stage();
        TimetableEntry timetableEntry = new TimetableEntry();
        try {
            timetableEntry.start(newWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        newWindow.setTitle("Timetable Search Result: " + searchText);
        newWindow.show();
    }

    //FOUND CLASS TAB
    private void timetableWindow(String[] args){
        TimetableEntry.launch(TimetableEntry.class, args);
    }

    private void openAddCourseWindow() {
        Stage newWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Add Course");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField nameField = new TextField();
        nameField.setPromptText("");

        Label codeLabel = new Label("Code:");
        codeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField codeField = new TextField();
        codeField.setPromptText("");

        Label timingLabel = new Label("Timing:");
        timingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField timingField = new TextField();
        timingField.setPromptText("");

        Label lecturerLabel = new Label("Lecturer:");
        lecturerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField lecturerField = new TextField();
        lecturerField.setPromptText("");

        Button addButton = new Button("Add");
        addButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: #d3d3d3;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        addButton.setOnAction(event -> newWindow.close());

        vbox.getChildren().addAll(
            title,
            nameLabel, nameField,
            codeLabel, codeField,
            timingLabel, timingField,
            lecturerLabel, lecturerField,
            addButton
        );

        Scene newScene = new Scene(vbox, 300, 400);
        newWindow.setTitle("Add New Course Tab");
        newWindow.setScene(newScene);
        newWindow.show();
    }
    public static void main (String[]args){
        launch(args);
        SchoolLecturePlanner planner = new SchoolLecturePlanner();

        planner.addClassroom(1, "A101", 30);
        planner.addClassroom(2, "B202", 25);

        planner.addCourse("Math 101", "MATH101", "Dr. Smith", "09:00-11:00", "A101");
        planner.addCourse("Physics 101", "PHYS101", "Dr. Brown", "11:00-13:00", "B202");

        planner.addStudent(1, "Alice");
        planner.addStudent(2, "Bob");

        planner.enrollStudentToCourse(1, "MATH101");
        planner.enrollStudentToCourse(2, "PHYS101");

        System.out.println("-- al lessons --");
        planner.listCourses();

        System.out.println("-- all stnds --");
        planner.listStudents();

        System.out.println("-- all clss --");
        planner.listClassrooms();


        System.out.println("removemath101");
        planner.removeCourse("MATH101");

        System.out.println("-- güncel lesson list --");
        planner.listCourses();
        System.out.println("-- Güncel class list --");
        planner.listClassrooms();


        //csvden cekme testleri lutfen sonra silin!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        planner.loadClassrooms("data/ClassroomCapacity.csv"); //CSVLER DATA FOLDERINDA !!!!!!!!!!
        planner.loadCourses("data/Courses.csv");

        // Print loaded data for verification
        for (Classroom classroom : planner.classrooms) {
            System.out.println("Classroom: " + classroom.getName() + ", Capacity: " + classroom.getCapacity() + ", Available: " + classroom.isAvailable());
        }
        for (Course course : planner.courses) {
            System.out.println("Course: " + course.getName() + ", Timing: " + course.getTiming() + ", Lecturer: " + course.getLecturer() + ", Classroom: " + course.getClassroom());
        }
        for (Student student : planner.students) {
            System.out.print("Student: " + student.getName() + " (ID: " + student.getId() + ") Enrolled Courses: ");
            for (Course course : student.getEnrolledCourses()) {
                System.out.print(course.getName() + " ");
            }
            System.out.println();
        }
        

    }
}

    

