import java.util.ArrayList;
import java.util.List;

public class SchoolLecturePlanner {

    private List<Student> students;
    private List<Classroom> classrooms;
    private List<Course> courses;


    public SchoolLecturePlanner() {
        this.students = new ArrayList<>();
        this.classrooms = new ArrayList<>();
        this.courses = new ArrayList<>();
    }


    public void addCourse(String name, String code, String lecturer, String timing, String classroomName) {
        Classroom classroom = findClassroomByName(classroomName);
        if (classroom == null || !classroom.isAvailable()) {
            System.out.println("Classroom is not available or does not exist!");
            return;
        }
        Course course = new Course(name, code, lecturer, timing, classroomName);
        courses.add(course);
        classroom.setAvailable(false);
        System.out.println("Course added: " + course);
    }

    public void removeCourse(String code) {
        Course courseToRemove = findCourseByCode(code);
        if (courseToRemove != null) {
            courses.remove(courseToRemove);
            Classroom classroom = findClassroomByName(courseToRemove.getClassroom());
            if (classroom != null) {
                classroom.setAvailable(true);
            }
            System.out.println("Course removed: " + code);
        } else {
            System.out.println("Course not found!");
        }
    }
//
    public Course findCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

     public void addStudent(int id, String name) {
        Student student = new Student(id, name);
        students.add(student);
        System.out.println("Student added: " + student);
    }

    public void enrollStudentToCourse(int studentId, String courseCode) {
        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);
        if (student != null && course != null) {
            student.addCourse(course);
            System.out.println("Student enrolled: " + student.getName() + " ---> " + course.getName());
        } else {
            System.out.println("Student or Course not found!");
        }
    }

    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }


    public void addClassroom(int id, String name, int capacity) {
        Classroom classroom = new Classroom(id, name, capacity, true);
        classrooms.add(classroom);
        System.out.println("Classroom added: " + classroom);
    }

    public Classroom findClassroomByName(String name) {
        for (Classroom classroom : classrooms) {
            if (classroom.getName().equals(name)) {
                return classroom;
            }
        }
        return null;
    }


    public void listCourses() {
        System.out.println("Courses:");
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    public void listStudents() {
        System.out.println("Students:");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void listClassrooms() {
        System.out.println("Classrooms:");
        for (Classroom classroom : classrooms) {
            System.out.println(classroom);
        }
    }

}
