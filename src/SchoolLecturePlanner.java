import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    private Map<String, Course> courseMap; 
    private Map<String, Student> studentMap; 
    private CSVLoader csvLoader;
    private mainApp app;

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

    public boolean isClassroomAvailable(String day, String time, int durationHours, String courseCode, String classroomName, String lecturer) {
        String[] times = {
            "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
            "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
        };
        
        int newStartIndex = findTimeIndex(times, time);
        if (newStartIndex == -1) return false;  // Geçersiz zaman
        int newEndIndex = newStartIndex + durationHours - 1;
        if (newEndIndex >= times.length) return false;  // Süre çok uzun
        
        for (Course existingCourse : courses) {
            if (existingCourse.getCode().equals(courseCode)) continue;
            
            // Sınıf çakışması kontrolü
            if (existingCourse.getClassroom().equals(classroomName) && 
                existingCourse.getDay().equals(day)) {
                
                int existingStartIndex = findTimeIndex(times, existingCourse.getTime());
                int existingEndIndex = existingStartIndex + existingCourse.getDurationHours() - 1;
                
                if ((newStartIndex <= existingEndIndex) && (newEndIndex >= existingStartIndex)) {
                    System.out.println("Classroom conflict: " + classroomName + " is already booked for " + 
                                     existingCourse.getCode() + " at this time");
                    return false;
                }
            }
            
            // Hoca çakışması kontrolü
            if (existingCourse.getLecturer().equals(lecturer) && 
                existingCourse.getDay().equals(day)) {
                
                int existingStartIndex = findTimeIndex(times, existingCourse.getTime());
                int existingEndIndex = existingStartIndex + existingCourse.getDurationHours() - 1;
                
                if ((newStartIndex <= existingEndIndex) && (newEndIndex >= existingStartIndex)) {
                    System.out.println("Lecturer conflict: " + lecturer + " is already teaching " + 
                                     existingCourse.getCode() + " at this time");
                    return false;
                }
            }
        }
        return true;  // Çakışma yok
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
        

        for (Course course : courses) {
            if (course.getCode().toLowerCase().contains(searchText.toLowerCase())||
                course.getLecturer().toLowerCase().contains(searchText.toLowerCase())) {
                matchingCourses.add(course);
            }
        }
        
        return matchingCourses;
    }
    

    public List<String> getStudentNames() {
        List<String> studentNames = new ArrayList<>();
        for (Student student : students) { 
            studentNames.add(student.getName());
        }
        return studentNames;
    }

    public List<Student> getStudents() {
        return students; 
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
    

    
        public void addCourse(String code, String lecturer, String timing, int durationHours, String classroomName, List<Student> students) {
            Classroom classroom = findClassroomByName(classroomName);
            if (classroom == null) {
                System.out.println("Classroom not found: " + classroomName);
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
        
            if (!isClassroomAvailable(day, time, durationHours, code, classroomName, lecturer)) {
                return;  // The error message is already printed in isClassroomAvailable
            }

            Course course = new Course(code, lecturer, timing, durationHours, classroomName, students);
            courses.add(course);
            courseMap.put(code, course);
            
            exportCSV("data/Courses.csv", courses);
            System.out.println("Course added successfully: " + code);
        }

        public void assignStudentsToCourse(String courseCode, List<String> studentNames, String csvFilePath) {
            Course courseToAssign = findCourseByCode(courseCode);
            if (courseToAssign == null) {
                System.out.println("Course not found: " + courseCode);
                return;
            }

            for (String studentName : studentNames) {
                Student student = findStudentByName(studentName);
                if (student != null) {
                    // Öğrencinin mevcut derslerinde çakışma kontrolü
                    boolean hasConflict = false;
                    for (Course enrolledCourse : student.getEnrolledCourses()) {
                        if (enrolledCourse.getDay().equals(courseToAssign.getDay())) {
                            int existingStartIndex = findTimeIndex(getTimes(), enrolledCourse.getTime());
                            int existingEndIndex = existingStartIndex + enrolledCourse.getDurationHours() - 1;
                            
                            int newStartIndex = findTimeIndex(getTimes(), courseToAssign.getTime());
                            int newEndIndex = newStartIndex + courseToAssign.getDurationHours() - 1;
                            
                            if ((newStartIndex <= existingEndIndex) && (newEndIndex >= existingStartIndex)) {
                                System.out.println("Time conflict for student " + studentName + 
                                    ": Already enrolled in " + enrolledCourse.getCode() + 
                                    " at the same time.");
                                hasConflict = true;
                                break;
                            }
                        }
                    }
                    
                    if (!hasConflict) {
                        courseToAssign.addStudent(student);
                        student.addCourse(courseToAssign);
                    }
                } else {
                    System.out.println("Student not found: " + studentName);
                }
            }

            exportCSV(csvFilePath, getCourses());
            System.out.println("CSV updated after assigning students to course: " + courseCode);
        }

        // Yardımcı metod - zaman dizisini döndürür
        private String[] getTimes() {
            return new String[] {
                "08:30", "09:25", "10:20", "11:15", "12:10", "13:05", "14:00", "14:55",
                "15:50", "16:45", "17:40", "18:35", "19:30", "20:25", "21:20", "22:15"
            };
        }

        public void exportCSV(String filepath, List<Course> courses) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
        for (Course course : courses) {
            StringBuilder line = new StringBuilder();
            line.append(course.getCode()).append(";")
                .append(course.getDay()).append(" ").append(course.getTime()).append(";")
                .append(course.getDurationHours()).append(";")
                .append(course.getLecturer());

            for (Student student : course.getEnrolledStudents()) {
                line.append(";").append(student.getName());
            }

            writer.write(line.toString());
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error writing to CSV: " + e.getMessage());
    }
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

    /*public void addStudent(String name) {
        Student student = new Student(name);
        students.add(student);
        System.out.println("Student added: " + student);
    }*/

    public void addStudent(Student student) {
        if (!students.contains(student)) { 
            students.add(student);
        }
    }

    /*public void enrollStudentToCourse(int studentId, String courseCode) {
        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);
        if (student != null && course != null) {
            student.addCourse(course);
            course.addStudent(student);
            System.out.println("Student enrolled: " + student.getName() + " ---> " + course.getCode());
        } else {
            System.out.println("Student or Course not found!");
        }
    }*/
    


    public List<String> getAllStudentNames() {
        List<String> studentNames = new ArrayList<>();
        for (Student student : students) {
            studentNames.add(student.getName());
        }
        return studentNames;
    }
    public void unassignStudentFromCourse(String courseCode, String studentName) {
        Course course = findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course not found: " + courseCode);
            return;
        }
    
        Student student = findStudentByName(studentName);
        if (student != null) {
            course.removeStudent(student); 
            System.out.println("Student " + studentName + " removed from course: " + courseCode);
        } else {
            System.out.println("Student not found: " + studentName);
        }
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
