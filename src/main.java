

/*public class main extends Application{
     @Override
    public void start(Stage primaryStage) {
        // Create a button
        Button button = new Button("Click Me!");
        button.setOnAction(e -> System.out.println("Hello, JavaFX!"));

        // Create a layout and add the button
        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        // Create a scene and set it to the stage
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Hello JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/
public class main {
    public static void main(String[] args) {
        //launch(args);
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

    

