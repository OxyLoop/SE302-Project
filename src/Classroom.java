public class Classroom {
    
    private String name;
    private int capacity;
    private boolean isAvailable;
    private Course assignedCourse;


    public Classroom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.isAvailable = true; 
        this.assignedCourse = null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Course getAssignedCourse() {
        return assignedCourse;
    }

    public void setAssignedCourse(Course assignedCourse) {
        this.assignedCourse = assignedCourse;
        this.isAvailable = (assignedCourse == null);
    }
    



}
