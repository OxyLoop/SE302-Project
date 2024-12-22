
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.stage.FileChooser;
import java.io.File;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import java.io.InputStream;
import javafx.stage.Stage;
import java.awt.Desktop;
//asdasdasdasd
public class mainApp extends Application {
    
    private SchoolLecturePlanner planner = new SchoolLecturePlanner();
    
    private Label totalStudentsLabel = new Label("Total Students: 0");
    private Label totalCoursesLabel = new Label("Total Courses: 0");
    private Label totalClassroomsLabel = new Label("Total Classrooms: 0");
    private void updateDashboard() {
        totalStudentsLabel.setText(String.valueOf(planner.getStudents().size()));
        totalCoursesLabel.setText(String.valueOf(planner.getCourses().size()));
        totalClassroomsLabel.setText(String.valueOf(planner.getClassrooms().size()));
    }


    private List<Course> importCSV(String filepath) {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) { 
                    String code = parts[0];
                    String[] timingParts = parts[1].split(" ");
                    String day = timingParts[0];
                    String time = timingParts[1];
                    int durationHours = Integer.parseInt(parts[2]);
                    String lecturer = parts[3];
    
                    
                    List<Student> students = new ArrayList<>();
                    for (int i = 4; i < parts.length; i++) {
                        students.add(new Student(parts[i])); 
                    }
    
                   
                    Course course = new Course(code, lecturer, day + " " + time, durationHours, students);
                    courses.add(course);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }
    

    
    /*private void exportCSV(String filepath, List<Course> courses) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Course course : courses) {
                
                StringBuilder line = new StringBuilder();
                line.append(course.getCode()).append(";")  // Course code
                    .append(course.getDay()).append(" ").append(course.getTime()).append(";")  // Day and time
                    .append(course.getDurationHours()).append(";")  // Duration
                    .append(course.getLecturer());  // Lecturer
    
                
                for (Student student : course.getEnrolledStudents()) {
                    line.append(";").append(student.getName());
                }
    
                
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void exportCSV(String filename, List<Course> courses) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Course course : courses) {
                StringBuilder studentNames = new StringBuilder();
                for (Student student : course.getStudents()) {
                    if (studentNames.length() > 0) {
                        studentNames.append(";");
                    }
                    studentNames.append(student.getName());
                }
                bw.write(String.format("%s;%s;%d;%s;%s\n",
                        course.getCode(),
                        course.getTiming(),
                        course.getDurationHours(),
                        course.getLecturer(),
                        studentNames.toString()));
            }
        } catch (IOException e) {
            System.err.println("Error exporting courses and students: " + e.getMessage());
        }
    }*/

    public void exportCSV(String filename, List<Course> courses) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Course course : courses) {
                bw.write(course.toCsvString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error exporting courses and students: " + e.getMessage());
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
                String timing = course.getDay() + " " + course.getTime(); 
                int durationHours = course.getDurationHours();
                String classroomName = course.getClassroom();
    
                
                List<Student> enrolledStudents = new ArrayList<>();
                for (Student student : course.getEnrolledStudents()) {
                    Student existingStudent = planner.findStudentByName(student.getName());
                    if (existingStudent != null) {
                        enrolledStudents.add(existingStudent);
                    } else {
                        
                        Student newStudent = new Student(student.getName());
                        planner.addStudent(newStudent);
                        enrolledStudents.add(newStudent);
                    }
                }
    
                
                planner.addCourse(code, lecturer, timing, durationHours, classroomName, enrolledStudents);
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
        primaryStage.setTitle("School Lecture Planner");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #001f3f");

        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: linear-gradient(to top right, #6fa8dc, #ffffff); ");
        Menu fileMenu = new Menu("File");

        MenuItem importMenuItem = new MenuItem("Import");
        importMenuItem.setOnAction(event -> importCSVButton());

        MenuItem exportMenuItem = new MenuItem("Export");
        exportMenuItem.setOnAction(event -> exportCSVButton());

        fileMenu.getItems().addAll(importMenuItem,exportMenuItem);

        Menu helpMenu = new Menu("Help");

        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setOnAction(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("About");
            alert.setContentText(
                    "This application is made by Arda Sarı, Akın Savaşçı, Bekir Can Türkmen, İpek Sude Yavaş and Ege Orhan. It is the project of the course SE302.");
            alert.setTitle("About");
            alert.showAndWait();
        });
        MenuItem manualMenuItem = new MenuItem("Manual");

        manualMenuItem.setOnAction(e -> {
            try {
                File pdfFile = new File("src/resources/UserManual.pdf");
                if (pdfFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        System.out.println("Awt Desktop is not supported!");
                    }
                } else {
                    System.out.println("File does not exist!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    
        helpMenu.getItems().addAll(aboutMenuItem, manualMenuItem);

        Menu addMenu = new Menu("New");

        MenuItem addCourseItem = new MenuItem("Add Course");
        addCourseItem.setOnAction(event -> openAddCourseWindow());

        MenuItem addClassItem = new MenuItem("Add Class");
        addClassItem.setOnAction(event -> openAddClassWindow());
        MenuItem assignCourseItem = new MenuItem("Assign Course");
assignCourseItem.setOnAction(event -> openAssignCourseWindow());
addMenu.getItems().add(assignCourseItem); // Add menu item to "New" menu
MenuItem unassignCourseItem = new MenuItem("Unassign Course");
unassignCourseItem.setOnAction(event -> openUnassignCourseWindow());
addMenu.getItems().add(unassignCourseItem);

        addMenu.getItems().addAll(addCourseItem,addClassItem);

        menuBar.getMenus().addAll(fileMenu,addMenu,helpMenu);

    
        HBox topButtonBox = new HBox(20); 
        VBox topContainer = new VBox(menuBar,topButtonBox);

        HBox dashboard = new HBox(20);
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setStyle("-fx-padding: 20;");
        VBox studentsBox = new VBox(5);
        studentsBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
        Label studentsLabel = new Label("Total Students");
        studentsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        totalStudentsLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: black;");
        studentsBox.getChildren().addAll(studentsLabel, totalStudentsLabel);
        VBox coursesBox = new VBox(5);
        coursesBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
        Label coursesLabel = new Label("Total Courses");
        coursesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        totalCoursesLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: black;");
        coursesBox.getChildren().addAll(coursesLabel, totalCoursesLabel);
        VBox classroomsBox = new VBox(5);
        classroomsBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
        Label classroomsLabel = new Label("Total Classrooms");
        classroomsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        totalClassroomsLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: black;");
        classroomsBox.getChildren().addAll(classroomsLabel, totalClassroomsLabel);
        dashboard.getChildren().addAll(studentsBox, coursesBox, classroomsBox);
        VBox dashboardContainer = new VBox(dashboard);
        dashboardContainer.setAlignment(Pos.CENTER);
        dashboardContainer.setStyle("-fx-padding: 20;");
        topContainer.getChildren().add(dashboardContainer);

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

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("School Management");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        updateDashboard();
    }
    private void openUnassignCourseWindow() {
        Stage unassignWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");
    
        Label title = new Label("Unassign Students");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");
    
        // Ders Seçimi
        Label courseLabel = new Label("Select Course:");
        ChoiceBox<String> courseChoiceBox = new ChoiceBox<>();
        courseChoiceBox.getItems().addAll(
            planner.getCourses().stream().map(Course::getCode).collect(Collectors.toList())
        );
    
 
        Label classroomLabel = new Label("Select Classroom:");
        ChoiceBox<String> classroomChoiceBox = new ChoiceBox<>();
        classroomChoiceBox.getItems().addAll(
            planner.getClassroomNames()
        );
    
  
        Label availableStudentsLabel = new Label("Students in Course:");
        ListView<String> availableStudentListView = new ListView<>();
        availableStudentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    

        Label selectedStudentsLabel = new Label("Selected Students for Unassign:");
        ListView<String> selectedStudentListView = new ListView<>();
        ObservableList<String> selectedStudents = FXCollections.observableArrayList();
        selectedStudentListView.setItems(selectedStudents);
    

        Button addStudentButton = new Button("Add to Unassign List");
        addStudentButton.setOnAction(event -> {
            String selectedStudent = availableStudentListView.getSelectionModel().getSelectedItem();
            if (selectedStudent != null && !selectedStudents.contains(selectedStudent)) {
                selectedStudents.add(selectedStudent); 
            }
        });
    

        Button removeStudentButton = new Button("Remove from List");
        removeStudentButton.setOnAction(event -> {
            String selectedStudent = selectedStudentListView.getSelectionModel().getSelectedItem();
            selectedStudents.remove(selectedStudent); 
        });
    

        courseChoiceBox.setOnAction(event -> updateStudentList(courseChoiceBox, classroomChoiceBox, availableStudentListView));
        classroomChoiceBox.setOnAction(event -> updateStudentList(courseChoiceBox, classroomChoiceBox, availableStudentListView));
    

        Button unassignButton = new Button("Unassign");
        unassignButton.setOnAction(event -> {
            String selectedCourseCode = courseChoiceBox.getValue();
            String selectedClassroomName = classroomChoiceBox.getValue();
    
            if (selectedCourseCode == null || selectedClassroomName == null || selectedStudents.isEmpty()) {
                System.out.println("Please select a course, classroom, and at least one student.");
                return;
            }
    
            Course course = planner.findCourseByCode(selectedCourseCode);
            if (course != null) {
                for (String studentName : selectedStudents) {
                    Student student = planner.findStudentByName(studentName);
                    if (student != null) {
                        course.removeStudent(student); 
                    }
                }
                System.out.println("Selected students removed from course: " + selectedCourseCode);
                updateDashboard();
            }
    
            selectedStudents.clear(); 
            unassignWindow.close();
        });
    
  
        HBox studentButtons = new HBox(10, addStudentButton, removeStudentButton);
        studentButtons.setStyle("-fx-alignment: center;");
    
        vbox.getChildren().addAll(
            title,
            courseLabel, courseChoiceBox,
            classroomLabel, classroomChoiceBox,
            availableStudentsLabel, availableStudentListView,
            studentButtons,
            selectedStudentsLabel, selectedStudentListView,
            unassignButton
        );
    
        Scene scene = new Scene(vbox, 400, 700);
        unassignWindow.setScene(scene);
        unassignWindow.setTitle("Unassign Students");
        unassignWindow.show();
    }
    
    
    private void updateStudentList(ChoiceBox<String> courseChoiceBox, ChoiceBox<String> classroomChoiceBox, ListView<String> studentListView) {
        String selectedCourseCode = courseChoiceBox.getValue();
        String selectedClassroomName = classroomChoiceBox.getValue();
    
        if (selectedCourseCode != null && selectedClassroomName != null) {
            Course course = planner.findCourseByCode(selectedCourseCode);
            if (course != null) {
                studentListView.getItems().setAll(
                    course.getEnrolledStudents().stream().map(Student::getName).collect(Collectors.toList())
                );
            } else {
                studentListView.getItems().clear();
            }
        } else {
            studentListView.getItems().clear();
        }
    }
    

    private void openAssignCourseWindow() {
        Stage assignWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");
    
        Label title = new Label("Assign Course");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");
    

        Label courseLabel = new Label("Select Course:");
        ChoiceBox<String> courseChoiceBox = new ChoiceBox<>();
        courseChoiceBox.getItems().addAll(
            planner.getCourses().stream().map(Course::getCode).collect(Collectors.toList())
        );
    
 
        Label classroomLabel = new Label("Select Classroom:");
        ChoiceBox<String> classroomChoiceBox = new ChoiceBox<>();
        classroomChoiceBox.getItems().addAll(planner.getClassroomNames());
    
     
        Label studentLabel = new Label("Select Students:");
        ListView<String> studentListView = new ListView<>();
        studentListView.getItems().addAll(planner.getAllStudentNames());
 
        Label selectedLabel = new Label("Selected Students:");
        ListView<String> selectedStudentsListView = new ListView<>();
        ObservableList<String> selectedStudentsList = FXCollections.observableArrayList();
        selectedStudentsListView.setItems(selectedStudentsList);
    
   
        Button addStudentButton = new Button("Add Student");
        addStudentButton.setOnAction(event -> {
            String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
            if (selectedStudent != null && !selectedStudentsList.contains(selectedStudent)) {
                selectedStudentsList.add(selectedStudent);
            }
        });
    

        Button removeStudentButton = new Button("Remove Student");
        removeStudentButton.setOnAction(event -> {
            String selectedStudent = selectedStudentsListView.getSelectionModel().getSelectedItem();
            selectedStudentsList.remove(selectedStudent);
        });
    
 
        Button assignButton = new Button("Assign");
        assignButton.setOnAction(event -> {
            String selectedCourseCode = courseChoiceBox.getValue();
            String selectedClassroomName = classroomChoiceBox.getValue();
            if (selectedCourseCode != null && selectedClassroomName != null && !selectedStudentsList.isEmpty()) {
                Course course = planner.findCourseByCode(selectedCourseCode);
                Classroom classroom = planner.findClassroomByName(selectedClassroomName);
    
                if (course != null && classroom != null) {
                    for (String studentName : selectedStudentsList) {
                        Student student = planner.findStudentByName(studentName);
                        if (student != null) {
                            course.addStudent(student); 
                        }
                    }
                    System.out.println("Course successfully assigned to selected students and classroom!");
                }
            } else {
                System.out.println("Please select a course, classroom, and at least one student.");
            }
    
            assignWindow.close();
        });
    
        HBox studentButtons = new HBox(10, addStudentButton, removeStudentButton);
        studentButtons.setStyle("-fx-alignment: center;");
    
        vbox.getChildren().addAll(
            title,
            courseLabel, courseChoiceBox,
            classroomLabel, classroomChoiceBox,
            studentLabel, studentListView,
            studentButtons,
            selectedLabel, selectedStudentsListView,
            assignButton
        );
    
        Scene scene = new Scene(vbox, 400, 600);
        assignWindow.setScene(scene);
        assignWindow.setTitle("Assign Course");
        assignWindow.show();
    }
    
    
    
    
    private void openAddCourseWindow() {
    Stage newWindow = new Stage();
    VBox vbox = new VBox(10);
    vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

    Label title = new Label("Add Course");
    title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");


    Label codeLabel = new Label("Code:");
    TextField codeField = new TextField();
    codeField.setPromptText("Enter course code");

    Label lecturerLabel = new Label("Lecturer:");
    ComboBox<String> lecturerComboBox = new ComboBox<>();
    TextField newLecturerField = new TextField();
    newLecturerField.setPromptText("Enter new lecturer name");


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
            lecturerComboBox.setValue(newLecturer);
            newLecturerField.clear();
        }
    });
    


    Label dayLabel = new Label("Day:");
    ChoiceBox<String> dayChoiceBox = new ChoiceBox<>();
    dayChoiceBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    dayChoiceBox.setValue("Monday"); 

 
    Label timeLabel = new Label("Time:");
    ChoiceBox<String> timeChoiceBox = new ChoiceBox<>();
    timeChoiceBox.getItems().addAll(
        "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
        "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
    );
    timeChoiceBox.setValue("08:30"); 

  
    Label durationLabel = new Label("Duration:");
    ChoiceBox<String> durationChoiceBox = new ChoiceBox<>();
    for (int i = 1; i <= 6; i++) {
        durationChoiceBox.getItems().add(i + " hour(s)");
    }
    durationChoiceBox.setValue("1 hour(s)");


    Label classroomLabel = new Label("Classroom:");
    ComboBox<String> classroomComboBox = new ComboBox<>();
    classroomComboBox.getItems().addAll(planner.getClassroomNames());


 
    Label studentLabel = new Label("Students:");
    ListView<String> studentListView = new ListView<>();
    studentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    studentListView.getItems().addAll(planner.getStudentNames());

    Button addStudentButton = new Button("Add Student");
    TextField newStudentField = new TextField();
    newStudentField.setPromptText("Enter new student name");

    addStudentButton.setOnAction(event -> {
        String newStudent = newStudentField.getText().trim();
        if (!newStudent.isEmpty() && !studentListView.getItems().contains(newStudent)) {
            studentListView.getItems().add(newStudent);
            newStudentField.clear();
        }
    });

    Button removeStudentButton = new Button("Remove Selected");
    removeStudentButton.setOnAction(event -> {
        studentListView.getItems().removeAll(studentListView.getSelectionModel().getSelectedItems());
    });

    HBox studentControls = new HBox(10, newStudentField, addStudentButton, removeStudentButton);
    studentControls.setAlignment(Pos.CENTER);

    
    Button addButton = new Button("Add Course");
    addButton.setOnAction(event -> {
    String code = codeField.getText().trim();
    String lecturer = lecturerComboBox.getValue();
    String day = dayChoiceBox.getValue();
    String time = timeChoiceBox.getValue();
    String duration = durationChoiceBox.getValue();
    String classroom = classroomComboBox.getValue();

    
    ObservableList<String> selectedStudentNames = FXCollections.observableArrayList(studentListView.getSelectionModel().getSelectedItems());
    List<Student> selectedStudents = new ArrayList<>();

    if (!code.isEmpty() && !lecturer.isEmpty() && !day.isEmpty() && !time.isEmpty() && !duration.isEmpty() && classroom != null) {
        try {
            int durationHours = Integer.parseInt(duration.split(" ")[0]);
            String timing = day + " " + time; 

           
            for (String studentName : selectedStudentNames) {
                
                Student existingStudent = planner.findStudentByName(studentName);
                if (existingStudent != null) {
                    selectedStudents.add(existingStudent); 
                } else {
                    
                    Student newStudent = new Student(studentName);
                    planner.addStudent(newStudent);
                    selectedStudents.add(newStudent);
                }
            }

           
            planner.addCourse(code, lecturer, timing, durationHours, classroom, selectedStudents);

           
            CSVLoader loader = new CSVLoader();
            Course course = new Course(code, lecturer, timing, durationHours, classroom, selectedStudents);
            loader.writeCourseToFile(course, "data/Courses.csv");
            updateDashboard();
            newWindow.close(); 
        } catch (NumberFormatException e) {
            System.out.println("Duration must be a valid number!");
        }
    } else {
        System.out.println("All fields are required!");
    }
});

    


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
        studentLabel, studentListView, studentControls,
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
                    updateDashboard();
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

    

