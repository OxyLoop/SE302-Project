import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import java.io.InputStream;
import javafx.stage.FileChooser;
import java.awt.Desktop;
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
import javafx.stage.Stage;
//asdasdasdasd
public class mainApp extends Application {
    
    private SchoolLecturePlanner planner = new SchoolLecturePlanner();
    private TimetableEntry timetableEntry;
    private CSVLoader csvLoader = new CSVLoader();
    private String courseFile = "data/Courses.csv";
    
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
                if (parts.length >= 4) { 
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
                //String classroomName = course.getClassroom();
    
                
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
    
                
                planner.addCourse(code, lecturer, timing, durationHours, "No Classroom", enrolledStudents);
            }
            updateDashboard(); 

            ListingTab listingTab = new ListingTab(planner, "lectures");
             listingTab.show();


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
        MenuItem assignStudentsMenuItem = new MenuItem("Assign Students");
        assignStudentsMenuItem.setOnAction(event -> openAssignWindow());
        addMenu.getItems().add(assignStudentsMenuItem); 

        MenuItem unassignCourseItem = new MenuItem("Unassign Course");
        unassignCourseItem.setOnAction(event -> openUnassignCourseWindow());
        addMenu.getItems().add(unassignCourseItem);

        addMenu.getItems().addAll(addCourseItem,addClassItem);
        MenuItem addStudentItem = new MenuItem("Add Student");
        addStudentItem.setOnAction(event -> openAddStudentWindow());
        addMenu.getItems().add(addStudentItem);
        menuBar.getMenus().addAll(fileMenu,addMenu,helpMenu);

    
        VBox topContainer = new VBox(menuBar);

        HBox dashboard = new HBox(20);
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setStyle("-fx-padding: 10;");
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
        dashboardContainer.setStyle("-fx-padding: 10;");
        topContainer.getChildren().add(dashboardContainer);

        root.setTop(topContainer);
        



        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center;");

        Label title = new Label("Timetable");
        title.setStyle(
            "-fx-font-size: 100px;" +
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

        // Ders seçimi
        Label courseLabel = new Label("Select Course:");
        ComboBox<String> courseChoiceBox = new ComboBox<>();
        courseChoiceBox.getItems().addAll(
            planner.getCourses().stream().map(Course::getCode).collect(Collectors.toList())
        );

        // Dersin kayıtlı öğrencileri listesi
        Label enrolledStudentsLabel = new Label("Enrolled Students:");
        ListView<String> enrolledStudentListView = new ListView<>();
        enrolledStudentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Çıkarılacak öğrenciler listesi
        Label studentsToRemoveLabel = new Label("Students to Unassign:");
        ListView<String> studentsToRemoveListView = new ListView<>();
        studentsToRemoveListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Ders seçildiğinde öğrenci listesini güncelle
        courseChoiceBox.setOnAction(e -> {
            String selectedCourse = courseChoiceBox.getValue();
            if (selectedCourse != null) {
                Course course = planner.findCourseByCode(selectedCourse);
                if (course != null) {
                    enrolledStudentListView.getItems().setAll(
                        course.getEnrolledStudents().stream()
                            .map(Student::getName)
                            .collect(Collectors.toList())
                    );
                }
            }
        });

        // Öğrenci transfer butonları
        Button addButton = new Button(">>");
        addButton.setOnAction(e -> {
            ObservableList<String> selectedItems = enrolledStudentListView.getSelectionModel().getSelectedItems();
            studentsToRemoveListView.getItems().addAll(selectedItems);
            enrolledStudentListView.getItems().removeAll(selectedItems);
        });

        Button removeButton = new Button("<<");
        removeButton.setOnAction(e -> {
            ObservableList<String> selectedItems = studentsToRemoveListView.getSelectionModel().getSelectedItems();
            enrolledStudentListView.getItems().addAll(selectedItems);
            studentsToRemoveListView.getItems().removeAll(selectedItems);
        });

        VBox buttonBox = new VBox(10, addButton, removeButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Liste görünümlerini yatay olarak düzenle
        HBox listBox = new HBox(10);
        listBox.setAlignment(Pos.CENTER);
        listBox.getChildren().addAll(
            new VBox(5, enrolledStudentsLabel, enrolledStudentListView),
            buttonBox,
            new VBox(5, studentsToRemoveLabel, studentsToRemoveListView)
        );

        // Toplu çıkarma butonu
        Button unassignButton = new Button("Unassign Selected Students");
        unassignButton.setOnAction(e -> {
            String selectedCourseCode = courseChoiceBox.getValue();
            if (selectedCourseCode != null && !studentsToRemoveListView.getItems().isEmpty()) {
                Course course = planner.findCourseByCode(selectedCourseCode);
                if (course != null) {
                    for (String studentName : studentsToRemoveListView.getItems()) {
                        Student student = planner.findStudentByName(studentName);
                        if (student != null) {
                            course.removeStudent(student);
                            student.removeCourse(course);
                        }
                    }
                    updateCSVFile("data/Courses.csv", planner.getCourses());
                }
                unassignWindow.close();
            } else {
                System.out.println("Please select a course and at least one student");
            }
        });

        vbox.getChildren().addAll(
            title,
            courseLabel, courseChoiceBox,
            listBox,
            unassignButton
        );

        Scene scene = new Scene(vbox, 600, 500);
        unassignWindow.setScene(scene);
        unassignWindow.setTitle("Unassign Students");
        unassignWindow.show();
    }
    private void updateCSVFile(String filePath, List<Course> courses) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Course course : courses) {
                
                StringBuilder line = new StringBuilder();
                line.append(course.getCode()).append(";")
                    .append(course.getDay()).append(" ").append(course.getTime()).append(";")
                    .append(course.getDurationHours()).append(";")
                    .append(course.getLecturer()).append(";");
    
                
                List<Student> students = course.getEnrolledStudents();
                for (int i = 0; i < students.size(); i++) {
                    line.append(students.get(i).getName());
                    if (i < students.size() - 1) {
                        line.append(";");
                    }
                }
    
                bw.write(line.toString());
                bw.newLine();
            }
            System.out.println("CSV updated successfully.");
        } catch (IOException e) {
            System.err.println("Error updating CSV file: " + e.getMessage());
        }
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
    private void openAddStudentWindow() {
        Stage addStudentStage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");
    
        Label titleLabel = new Label("Add Student");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        Label nameLabel = new Label("Student Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter student name");
    
        Button addButton = new Button("Add Student");
        addButton.setOnAction(event -> {
            String studentName = nameField.getText().trim();
            if (!studentName.isEmpty()) {
                Student newStudent = new Student(studentName);
                planner.addStudent(newStudent); 
    
                updateDashboard(); 
                addStudentStage.close(); 
                System.out.println("Student added: " + studentName);
            } else {
                System.out.println("Student name cannot be empty!");
            }
        });
    
        vbox.getChildren().addAll(titleLabel, nameLabel, nameField, addButton);
    
        Scene scene = new Scene(vbox, 300, 200);
        addStudentStage.setScene(scene);
        addStudentStage.setTitle("Add Student");
        addStudentStage.show();
    }
    
    private void openAssignWindow() {
        Stage assignWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label title = new Label("Assign Students");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");

        // Ders seçimi
        Label courseLabel = new Label("Select Course:");
        ComboBox<String> courseChoiceBox = new ComboBox<>();
        courseChoiceBox.getItems().addAll(
            planner.getCourses().stream().map(Course::getCode).collect(Collectors.toList())
        );

        // Mevcut öğrenciler listesi
        Label availableStudentsLabel = new Label("Available Students:");
        ListView<String> availableStudentListView = new ListView<>();
        availableStudentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availableStudentListView.getItems().addAll(
            planner.getStudents().stream().map(Student::getName).collect(Collectors.toList())
        );

        // Seçilen öğrenciler listesi
        Label selectedStudentsLabel = new Label("Students to Assign:");
        ListView<String> selectedStudentListView = new ListView<>();
        selectedStudentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Öğrenci transfer butonları
        Button addButton = new Button(">>");
        addButton.setOnAction(e -> {
            ObservableList<String> selectedItems = availableStudentListView.getSelectionModel().getSelectedItems();
            selectedStudentListView.getItems().addAll(selectedItems);
            availableStudentListView.getItems().removeAll(selectedItems);
        });

        Button removeButton = new Button("<<");
        removeButton.setOnAction(e -> {
            ObservableList<String> selectedItems = selectedStudentListView.getSelectionModel().getSelectedItems();
            availableStudentListView.getItems().addAll(selectedItems);
            selectedStudentListView.getItems().removeAll(selectedItems);
        });

        VBox buttonBox = new VBox(10, addButton, removeButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Liste görünümlerini yatay olarak düzenle
        HBox listBox = new HBox(10);
        listBox.setAlignment(Pos.CENTER);
        listBox.getChildren().addAll(
            new VBox(5, availableStudentsLabel, availableStudentListView),
            buttonBox,
            new VBox(5, selectedStudentsLabel, selectedStudentListView)
        );

        // Toplu atama butonu
        Button assignButton = new Button("Assign All Selected Students");
        assignButton.setOnAction(e -> {
            String selectedCourse = courseChoiceBox.getValue();
            if (selectedCourse != null && !selectedStudentListView.getItems().isEmpty()) {
                planner.assignStudentsToCourse(
                    selectedCourse,
                    new ArrayList<>(selectedStudentListView.getItems()),
                    "data/Courses.csv"
                );
                assignWindow.close();
            } else {
                System.out.println("Please select a course and at least one student");
            }
        });

        vbox.getChildren().addAll(
            title,
            courseLabel, courseChoiceBox,
            listBox,
            assignButton
        );

        Scene scene = new Scene(vbox, 600, 500);
        assignWindow.setScene(scene);
        assignWindow.setTitle("Assign Students");
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

        Button addButton = new Button("Add Course");
        addButton.setOnAction(event -> {
            String code = codeField.getText().trim();
            String lecturer = lecturerComboBox.getValue();
            String day = dayChoiceBox.getValue();
            String time = timeChoiceBox.getValue();
            String duration = durationChoiceBox.getValue();
            String classroom = classroomComboBox.getValue();

            if (!code.isEmpty() && !lecturer.isEmpty() && !day.isEmpty() && !time.isEmpty() && !duration.isEmpty() && classroom != null) {
                try {
                    int durationHours = Integer.parseInt(duration.split(" ")[0]);
                    String timing = day + " " + time;

                    planner.addCourse(code, lecturer, timing, durationHours, classroom, new ArrayList<>());
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
            addButton
        );

        Scene newScene = new Scene(vbox, 400, 500);
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
        

    }
}

    

