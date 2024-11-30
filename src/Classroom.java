public class Classroom {
    private int id;
    private String name;
    private int capacity;
    private boolean isAvailable;
    private Course assignedCourse;


    public Classroom(int id, String name, int capacity, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
        this.assignedCourse = null;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
