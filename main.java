public class main {
    public static void main(
            String[] args) {
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
    }
}

    

