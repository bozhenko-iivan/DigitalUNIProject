package ua.naukma.domain;

public class Department {
    private int id;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String location;

    public Department(int id, String name, Faculty faculty, Teacher head, String location) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;
        this.head = head;
        this.location = location;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Faculty getFaculty() { return faculty; }

    @Override
    public String toString() {
        return "Department of " + name + " [" + faculty.getShortName() + "]";
    }
}