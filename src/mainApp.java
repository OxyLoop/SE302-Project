
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
        

        Button importButton = new Button("Import Courses");
    importButton.setStyle(
        "-fx-font-size: 14px;" +
        "-fx-background-color: white;" +
        "-fx-text-fill: #001f3f;" +
        "-fx-border-radius: 10;" +
        "-fx-background-radius: 10;" +
        "-fx-padding: 5 20;"
    );
    importButton.setOnAction(event -> importCSVButton());

    // Create Export Button
    Button exportButton = new Button("Export Courses");
    exportButton.setStyle(
        "-fx-font-size: 14px;" +
        "-fx-background-color: white;" +
        "-fx-text-fill: #001f3f;" +
        "-fx-border-radius: 10;" +
        "-fx-background-radius: 10;" +
        "-fx-padding: 5 20;"
    );
    exportButton.setOnAction(event -> exportCSVButton());

    
    HBox topButtonBox = new HBox(20); 
    topButtonBox.setAlignment(Pos.CENTER);
    topButtonBox.getChildren().addAll(addCourseButton, addClassButton, importButton, exportButton);
    root.setTop(topButtonBox);



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
    
        Label codeLabel = new Label("Code:");
        codeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField codeField = new TextField();
        codeField.setPromptText("Enter course code");
    
        Label lecturerLabel = new Label("Lecturer:");
        lecturerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField lecturerField = new TextField();
        lecturerField.setPromptText("Enter lecturer name");
    
        Label timingLabel = new Label("Timing:");
        timingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField timingField = new TextField();
        timingField.setPromptText("Enter day ,space and time in _:_ format , e.g Monday 10:00)");
    
        Label durationLabel = new Label("Duration:");
        durationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField durationField = new TextField();
        durationField.setPromptText("Enter duration in hours");
    
        Label classroomLabel = new Label("Classroom:");
        classroomLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        ComboBox<String> classroomComboBox = new ComboBox<>();
        classroomComboBox.setStyle("-fx-text-fill: #001f3f;");
        List<String> classroomNames = planner.getClassroomNames();
        classroomComboBox.getItems().addAll(classroomNames);

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
            String code = codeField.getText().trim();
            String lecturer = lecturerField.getText().trim();
            String timing = timingField.getText().trim();
            String duration = durationField.getText().trim();
            String classroomName = classroomComboBox.getValue(); 
    
            if (!code.isEmpty() && !lecturer.isEmpty() && !timing.isEmpty() && !duration.isEmpty() && classroomName != null) {
                try {
                    int durationHours = Integer.parseInt(duration);
    
                    
                    planner.addCourse(code, lecturer, timing, durationHours, classroomName);
    
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
    
        vbox.getChildren().addAll(
            title,
            codeLabel, codeField,
            lecturerLabel, lecturerField,
            timingLabel, timingField,
            durationLabel, durationField,
            classroomLabel, classroomComboBox,
            addButton
        );

        Scene newScene = new Scene(vbox, 300, 500);
        newWindow.setTitle("Add New Course Tab");
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

    

