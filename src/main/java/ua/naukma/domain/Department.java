package ua.naukma.domain;

public class Department {
    private int id;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String location;

    public Department(int id, String name, Faculty faculty, Teacher head, String location) {
        this.id = id;
        setName(name);
        this.faculty = faculty;
        this.head = head;
        setLocation(location);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Faculty getFaculty() { return faculty; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid department name.");
        }
        this.name = name;
    }

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Invalid location.");
        }
        this.location = location;
    }

    @Override
    public String toString() {
        return "Department of " + name + " [" + faculty.getShortName() + "]";
    }
}