import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SchoolLecturePlanner {

    List<Student> students;
    List<Classroom> classrooms;
    List<Course> courses;
    private Map<String, Course> courseMap; // Course code to Course object map
    private Map<String, Student> studentMap; // Student name to Student object map
    private CSVLoader csvLoader;

    public SchoolLecturePlanner() {
        this.students = new ArrayList<>();
        this.classrooms = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.courseMap = new HashMap<>();
        this.studentMap = new HashMap<>();
        this.csvLoader = new CSVLoader();
    }

    public void loadClassrooms(String filename) {
        csvLoader.loadClassrooms(filename, classrooms);
    }

    public void loadCourses(String filename) {
        csvLoader.loadCourses(filename, courses, classrooms, courseMap, studentMap);
        students.addAll(studentMap.values());
    }

    public boolean isClassroomAvailable(String classroomName, String day, String startTime, int durationHours) {
       
        String[] times = {
            "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
            "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
        };
    
        
        int startIndex = findTimeIndex(times, startTime);
        if (startIndex == -1) {
            System.out.println("Invalid start time.");
            return false; 
        }
    
        
        int endIndex = startIndex + durationHours - 1;
        if (endIndex >= times.length) {
            System.out.println("Duration exceeds available lecture slots.");
            return false; 
        }
    
        for (Course course : courses) {
            if (course.getClassroom().equals(classroomName) && course.getDay().equals(day)) {
                int courseStartIndex = findTimeIndex(times, course.getTime());
                int courseEndIndex = courseStartIndex + course.getDurationHours() - 1;
    
                if ((startIndex <= courseEndIndex) && (endIndex >= courseStartIndex)) {
                    return false; 
                }
            }
        }
    
        return true; 
    }
    
    private int findTimeIndex(String[] times, String time) {
        for (int i = 0; i < times.length; i++) {
            if (times[i].equals(time)) {
                return i; 
            }
        }
        return -1; 
    }
    
    
    public List<Course> searchCourses(String searchText) {
        List<Course> matchingCourses = new ArrayList<>();
        
        // Search by course code
        for (Course course : courses) {
            if (course.getCode().toLowerCase().contains(searchText.toLowerCase())||
                course.getLecturer().toLowerCase().contains(searchText.toLowerCase())) {
                matchingCourses.add(course);
            }
        }
        
        return matchingCourses;
    }

    // bu fonksiyon öğrencilerin ortak zamanını görmek için.  GUI de bunun için buton olmalı ordan öğrenciler seçilebilmeli ve ortak zamanları bulunmalı
    /*public List<String> findJointFreeTimes(List<Integer> studentIds) { 
        List<Course> allEnrolledCourses = new ArrayList<>();
        for (int studentId : studentIds) {
            Student student = findStudentById(studentId);
            if (student != null) {
                allEnrolledCourses.addAll(student.getEnrolledCourses());
            }
        }
    
        
        List<String> allTimeSlots = new ArrayList<>(Arrays.asList(
            "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55", "15:50", 
            "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
        ));
    
        
        Set<String> busyTimes = new HashSet<>();
        for (Course course : allEnrolledCourses) {
            busyTimes.add(course.getTiming()); 
        }
    
        
        allTimeSlots.removeAll(busyTimes);
    
        return allTimeSlots; 
    }
        */
    


        public void addCourse(String code, String lecturer, String timing, int durationHours, String classroomName) {
            Classroom classroom = findClassroomByName(classroomName);
            if (classroom == null) {
                System.out.println("Classroom does not exist!");
                return;
            }
        
            
            if (durationHours <= 0) {
                System.out.println("Invalid duration! Duration must be a positive number of hours.");
                return;
            }
        
            
            String[] timingParts = timing.split(" ");
            if (timingParts.length != 2) {
                System.out.println("Invalid timing format! It should be 'Day Time'.");
                return;
            }
            String day = timingParts[0];
            String time = timingParts[1];
        
            
            if (!isClassroomAvailable(classroomName, day, time, durationHours)) {
                System.out.println("Scheduling conflict detected! Another course is already scheduled in this classroom at this time.");
                return;
            }
        
           
            Course course = new Course(code, lecturer, timing, durationHours, classroomName);
            courses.add(course); 
            classroom.setAvailable(false); 
            courseMap.put(code, course); 
        
            System.out.println("Course added: " + course);
            
        }
        
    
    
    public void removeCourse(String code) {
        Course courseToRemove = findCourseByCode(code);
        if (courseToRemove != null) {
            courses.remove(courseToRemove);
            Classroom classroom = findClassroomByName(courseToRemove.getClassroom());
            if (classroom != null) {
                if (courses.stream().noneMatch(course -> course.getClassroom().equals(classroom.getName()))) {
                    classroom.setAvailable(true);
                }
            }
            System.out.println("Course removed: " + code);
        } else {
            System.out.println("Course not found!");
        }
    }

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
            course.addStudent(student);
            System.out.println("Student enrolled: " + student.getName() + " ---> " + course.getCode());
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

    public List<String> getAllStudentNames() {
        List<String> studentNames = new ArrayList<>();
        for (Student student : students) {
            studentNames.add(student.getName());
        }
        return studentNames;
    }

    public Student findStudentByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null; 
    }

    public List<Course> getCourses() {
    return courses; 
    }

    public List<Classroom> getClassrooms() {
    return classrooms; 
    }
    public List<String> getClassroomNames() {
        List<String> classroomNames = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            classroomNames.add(classroom.getName()); 
        }
        return classroomNames;
    }

    public void addClassroom(String name, int capacity) {
        Classroom classroom = new Classroom(name, capacity);
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
            System.out.println(course.getCode());
        }
    }

    public void listStudents() {
        System.out.println("Students:");
        for (Student student : students) {
            System.out.println(student.getName());
        }
    }

    public void listClassrooms() {
        System.out.println("Classrooms:");
        for (Classroom classroom : classrooms) {
            System.out.println(classroom.getName());
        }
    }

}
