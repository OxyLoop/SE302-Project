import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CSVLoader {

    public void loadClassrooms(String filename, List<Classroom> classrooms) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (!data[0].equals("Classroom")) { // Skip header
                    String name = data[0];
                    int capacity = Integer.parseInt(data[1]);
                    classrooms.add(new Classroom(classrooms.size() + 1, name, capacity, true));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCourses(String filename, List<Course> courses, List<Classroom> classrooms, Map<String, Course> courseMap, Map<String, Student> studentMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (!data[0].equals("Course")) { // Skip header
                    String name = data[0];
                    String timing = data[1];
                    String lecturer = data[3];
                    String[] studentNames = data[4].split(";"); // Students start from the 5th column
                    String classroomName = findAvailableClassroom(classrooms);
                    if (classroomName == null) {
                        System.out.println("No available classroom for course: " + name);
                        continue;
                    }
                    Course course = new Course(name, name, lecturer, timing, classroomName);
                    courses.add(course);
                    courseMap.put(name, course);
                    assignCourseToClassroom(classroomName, classrooms, course);
                    assignStudentsToCourse(studentNames, course, studentMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void assignStudentsToCourse(String[] studentNames, Course course, Map<String, Student> studentMap) {
        for (String studentName : studentNames) {
            if (studentName.isEmpty()) continue;
            Student student = findOrCreateStudent(studentName.trim(), studentMap);
            student.addCourse(course);
        }
    }

    private Student findOrCreateStudent(String studentName, Map<String, Student> studentMap) {
        if (studentMap.containsKey(studentName)) {
            return studentMap.get(studentName);
        } else {
            Student newStudent = new Student(studentMap.size() + 1, studentName);
            studentMap.put(studentName, newStudent);
            return newStudent;
        }
    }

    private String findAvailableClassroom(List<Classroom> classrooms) {
        for (Classroom classroom : classrooms) {
            if (classroom.isAvailable()) {
                return classroom.getName();
            }
        }
        return null;
    }

    private void assignCourseToClassroom(String classroomName, List<Classroom> classrooms, Course course) {
        for (Classroom classroom : classrooms) {
            if (classroom.getName().equals(classroomName)) {
                classroom.setAssignedCourse(course);
                classroom.setAvailable(false);
                break;
            }
        }
    }
}
