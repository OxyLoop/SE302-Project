import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVLoader {

    public void loadClassrooms(String filename, List<Classroom> classrooms) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (!data[0].equals("Classroom")) { // Skip header
                    String name = data[0];
                    int capacity = Integer.parseInt(data[1]);
                    classrooms.add(new Classroom(name, capacity));
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
                    String code = data[0];
                    String timing = data[1];
                    int durationHours = Integer.parseInt(data[2]);
                    String lecturer = data[3];
                    String[] studentNames = extractStudentNames(data);
                    String classroomName = findAvailableClassroom(classrooms);
                    if (classroomName == null) {
                        classroomName = "No Classroom Available";
                        System.out.println("No available classroom for course: " + code);
                   
                    }
                    Course course = new Course(code, lecturer, timing, durationHours, classroomName );
                    courses.add(course);
                    courseMap.put(code, course);
                    if (!classroomName.equals("No Classroom Available")) {
                        assignCourseToClassroom(classroomName, classrooms, course);
                    }

                    for (String studentName : studentNames) {
                        Student student = studentMap.get(studentName);
                        if (student == null) {
                            student = new Student(studentName);
                            studentMap.put(studentName, student);
                        }
                        student.addCourse(course);
                        course.getStudents().add(student);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] extractStudentNames(String[] data) {
        // Extract students starting from the 5th column onwards
        String[] studentNames = new String[data.length - 4];
        System.arraycopy(data, 4, studentNames, 0, studentNames.length);
        return studentNames;
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
            Student newStudent = new Student(studentName);
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


    public void writeCourseToFile(Course course, String filename) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            StringBuilder sb = new StringBuilder();
            
            String timing = course.getDay() + " " + course.getTime();
            
            sb.append(course.getCode()).append(";")
              .append(timing).append(";")  
              .append(course.getDurationHours()).append(";")
              .append(course.getLecturer()).append(";");
    
            
            List<Student> students = course.getEnrolledStudents(); 
            for (Student student : students) {
                sb.append(student.getName()).append(";");
            }
    
            writer.append(sb.toString()).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /*public void rewriteCoursesToFile(List<Course> courses, Course editedCourse, File courseFile) {
        
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCode().equals(editedCourse.getCode())) {
                courses.set(i, editedCourse);  
                break;
            }
        }
    
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(courseFile))) {
            for (Course c : courses) {
                writer.write(c.toCsvString());  
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/ // hatalı tüm dosyayı silip tek editleneni yazıyor, çözemedim
    


    
    
    public void writeClassroomToFile(Classroom classroom, String filename) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            StringBuilder sb = new StringBuilder();
            
            
            sb.append(classroom.getName()).append(";")
              .append(classroom.getCapacity()).append(";\n");
            
            
            writer.append(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exportCSV(String filename, List<Course> courses) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Course course : courses) {
                bw.write(course.toCsvString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error exporting courses and students: " + e.getMessage());
        }
    }


}
