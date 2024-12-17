
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.stage.FileChooser;
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//asdasdasdasd
public class mainApp extends Application {
    
    private SchoolLecturePlanner planner = new SchoolLecturePlanner(); 


     private List<Course> importCSV(String filePath) {
        List<Course> importedCourses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] courseData = line.split(";");
                
               
                if (courseData.length >= 5) {
                    String courseCode = courseData[0];
                    String timing = courseData[1]; 
                    int durationHours = Integer.parseInt(courseData[2]); 
                    String lecturer = courseData[3];  
                    
                    
                    List<Student> students = new ArrayList<>();
                    for (int i = 4; i < courseData.length; i++) {
                        if (!courseData[i].isEmpty()) {
                            students.add(new Student(courseData[i])); 
                        }
                    }
                    
                    
                    Course course = new Course(courseCode, lecturer, timing, durationHours, ""); 
                    for (Student student : students) {
                        course.addStudent(student);  
                    }
                    
                    importedCourses.add(course);
                } else {
                    System.out.println("Invalid CSV line: " + line); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return importedCourses;
    }

    
    private void exportCSV(String filePath, List<Course> courses) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Course course : courses) {
                writer.write(course.toCsvString());
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void importCSVButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            List<Course> importedCourses = importCSV(file.getAbsolutePath());
            
            for (Course course : importedCourses) {
                String code = course.getCode();
                String lecturer = course.getLecturer();
                String timing = course.getDay() + " " + course.getTime();  // Combine day and time
                int durationHours = course.getDurationHours();
                String classroomName = course.getClassroom();
                
                planner.addCourse(code, lecturer, timing, durationHours, classroomName);
            }
            System.out.println("Courses imported from CSV.");
        }
    }
    
    
    
    private void exportCSVButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            exportCSV(file.getAbsolutePath(), planner.getCourses());  
            System.out.println("Courses exported to CSV.");
        }
    }
    @Override
    //MAIN TAB FOR APPLICATION
    public void start(Stage primaryStage) {
        planner.loadClassrooms("data/ClassroomCapacity.csv");
        planner.loadCourses("data/Courses.csv");
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #001f3f; -fx-padding: 20;");

        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: linear-gradient(to top right, #6fa8dc, #ffffff); ");
        Menu fileMenu = new Menu("File");

        MenuItem importMenuItem = new MenuItem("Import");
        importMenuItem.setOnAction(event -> importCSVButton());

        MenuItem exportMenuItem = new MenuItem("Export Books");
        exportMenuItem.setOnAction(event -> exportCSVButton());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        fileMenu.getItems().addAll(importMenuItem,exportMenuItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutMenuItem = new MenuItem("About PagePal");
        aboutMenuItem.setOnAction(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("About PagePal");
            alert.setContentText(
                    "This application is made by ..... It is the project of the course SE302.");
            alert.setTitle("About");
            alert.showAndWait();
        });
        MenuItem manualMenuItem = new MenuItem("Manual");
        helpMenu.getItems().addAll(aboutMenuItem, manualMenuItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);




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
        

        Button addClassButton = new Button("Add Class");
        addClassButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        addClassButton.setOnAction(event -> openAddClassWindow());

    
        HBox topButtonBox = new HBox(20); 
        topButtonBox.setAlignment(Pos.CENTER);
        topButtonBox.getChildren().addAll(addCourseButton, addClassButton);
        VBox topContainer = new VBox(menuBar,topButtonBox);
        root.setTop(topContainer);



        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-alignment: center;");

        Label title = new Label("Timetable");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-padding: 10;"
        );


        


        
        //Students button
        Button studentButton = new Button("Students");
        studentButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        studentButton.setOnAction(event -> {
            ListingTab studentListTab = new ListingTab(planner, "students"); 
            studentListTab.show();
        });

        // Lectures Button
        Button lecturesButton = new Button("Lectures");
        lecturesButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        lecturesButton.setOnAction(event -> {
            ListingTab listingTab = new ListingTab(planner, "lectures");
            listingTab.show();
        });

        // Classrooms Button
        Button classroomsButton = new Button("Classrooms");
        classroomsButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        classroomsButton.setOnAction(event -> {
            ListingTab listingTab = new ListingTab(planner, "classrooms"); 
            listingTab.show();
        });

        HBox buttonBox = new HBox(20); 
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(studentButton, lecturesButton, classroomsButton);


        vbox.getChildren().addAll(title,buttonBox);

        root.setCenter(vbox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("School Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void openAddCourseWindow() {
    Stage newWindow = new Stage();
    VBox vbox = new VBox(10);
    vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

    Label title = new Label("Add Course");
    title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");

    // Course Code Input
    Label codeLabel = new Label("Code:");
    TextField codeField = new TextField();
    codeField.setPromptText("Enter course code");

    // Lecturer Selection/Addition
    Label lecturerLabel = new Label("Lecturer:");
    ComboBox<String> lecturerComboBox = new ComboBox<>();
    TextField newLecturerField = new TextField();
    newLecturerField.setPromptText("Enter new lecturer name");

    // Populate existing lecturers
    List<String> existingLecturers = planner.getCourses().stream()
                                            .map(Course::getLecturer)
                                            .distinct()
                                            .sorted()
                                            .collect(Collectors.toList());
    lecturerComboBox.getItems().addAll(existingLecturers);

    Button addLecturerButton = new Button("Add Lecturer");
    addLecturerButton.setOnAction(event -> {
        String newLecturer = newLecturerField.getText().trim();
        if (!newLecturer.isEmpty() && !lecturerComboBox.getItems().contains(newLecturer)) {
            lecturerComboBox.getItems().add(newLecturer);
            lecturerComboBox.setValue(newLecturer); // Set the new lecturer as selected
            newLecturerField.clear();
        }
    });

    // Day Selection
    Label dayLabel = new Label("Day:");
    ChoiceBox<String> dayChoiceBox = new ChoiceBox<>();
    dayChoiceBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    dayChoiceBox.setValue("Monday"); // Default selection

    // Time Selection
    Label timeLabel = new Label("Time:");
    ChoiceBox<String> timeChoiceBox = new ChoiceBox<>();
    timeChoiceBox.getItems().addAll(
        "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
        "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
    );
    timeChoiceBox.setValue("08:30"); // Default selection

    // Duration Input
    Label durationLabel = new Label("Duration:");
    ChoiceBox<String> durationChoiceBox = new ChoiceBox<>();
    for (int i = 1; i <= 6; i++) {
        durationChoiceBox.getItems().add(i + " hour(s)");
    }
    durationChoiceBox.setValue("1 hour(s)");

    // Classroom Selection
    Label classroomLabel = new Label("Classroom:");
    ComboBox<String> classroomComboBox = new ComboBox<>();
    classroomComboBox.getItems().addAll(planner.getClassroomNames());

    // Add Button
    Button addButton = new Button("Add Course");
    addButton.setOnAction(event -> {
        String code = codeField.getText().trim();
        String lecturer = lecturerComboBox.getValue();
        String day = dayChoiceBox.getValue();
        String time = timeChoiceBox.getValue();
        String duration = durationChoiceBox.getValue();
        String classroomName = classroomComboBox.getValue();

        if (!code.isEmpty() && !lecturer.isEmpty() && !day.isEmpty() && !time.isEmpty() && !duration.isEmpty() && classroomName != null) {
            try {
                int durationHours = Integer.parseInt(duration.split(" ")[0]);
                String timing = day + " " + time; // Combine day and time
                planner.addCourse(code, lecturer, timing, durationHours, classroomName);

                // Save the new course to file
                CSVLoader loader = new CSVLoader();
                loader.writeCourseToFile(new Course(code, lecturer, timing, durationHours, classroomName), "data/Courses.csv");

                newWindow.close();
            } catch (NumberFormatException e) {
                System.out.println("Duration must be a valid number!");
            }
        } else {
            System.out.println("All fields are required!");
        }
    });

    // Layout
    HBox lecturerBox = new HBox(10, lecturerComboBox, newLecturerField, addLecturerButton);
    lecturerBox.setAlignment(Pos.CENTER);

    vbox.getChildren().addAll(
        title,
        codeLabel, codeField,
        lecturerLabel, lecturerBox,
        dayLabel, dayChoiceBox,
        timeLabel, timeChoiceBox,
        durationLabel, durationChoiceBox,
        classroomLabel, classroomComboBox,
        addButton
    );

    Scene newScene = new Scene(vbox, 400, 600);
    newWindow.setTitle("Add New Course");
    newWindow.setScene(newScene);
    newWindow.show();
}


    
    
        private void openAddClassWindow() {
        Stage newWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Add Class");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");

        Label classNameLabel = new Label("Class Name:");
        classNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField classNameField = new TextField();
        classNameField.setPromptText("Enter class name");

        Label capacityLabel = new Label("Capacity:");
        capacityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField capacityField = new TextField();
        capacityField.setPromptText("Enter class capacity");

        
        
        Button addButton = new Button("Add");
        addButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: #d3d3d3;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        addButton.setOnAction(event -> {
            String className = classNameField.getText().trim();
            String capacity = capacityField.getText().trim();
    
            if (!className.isEmpty() && !capacity.isEmpty()) {
                try {
                    int classCapacity = Integer.parseInt(capacity);
                    planner.addClassroom(className, classCapacity);
    
                    CSVLoader loader = new CSVLoader();
                    loader.writeClassroomToFile(new Classroom(className, classCapacity), "data/ClassroomCapacity.csv");
    
                    newWindow.close();
                } catch (NumberFormatException e) {
                    System.out.println("Capacity must be a valid number!");
                }
            } else {
                System.out.println("All fields are required!");
            }
        });

        vbox.getChildren().addAll(
        title,
        classNameLabel, classNameField,
        capacityLabel, capacityField,
        addButton
        );
            
        Scene newScene = new Scene(vbox, 300, 500);
        newWindow.setTitle("Add New Class");
        newWindow.setScene(newScene);
        newWindow.show();

        
    }
    


    public static void main (String[]args){
        launch(args);
        //SchoolLecturePlanner planner = new SchoolLecturePlanner();
        //planner.loadClassrooms("data/ClassroomCapacity.csv"); //CSVLER DATA FOLDERINDA !!!!!!!!!!
        //planner.loadCourses("data/Courses.csv");

        /* planner.addClassroom(1, "A101", 30);
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
       /*for (Classroom classroom : planner.classrooms) {
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
            */
        

    }
}

    

