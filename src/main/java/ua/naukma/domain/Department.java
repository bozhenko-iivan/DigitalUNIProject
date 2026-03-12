package ua.naukma.domain;

import ua.naukma.repository.InMemoryTeacherRepository;
import ua.naukma.service.TeacherService;

public class Department {
    private int id;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String location;
    private String email;
    private InMemoryTeacherRepository teachers = new InMemoryTeacherRepository();
    private TeacherService teacherService = new TeacherService(this);
    public Department(int id, String name, Faculty faculty, Teacher head, String location,  String email) {
        this.id = id;
        setName(name);
        this.faculty = faculty;
        this.head = head;
        setLocation(location);
        this.email = email;
    }

    public TeacherService getTeacherService() {
        return teacherService;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public Faculty getFaculty() { return faculty; }
    public InMemoryTeacherRepository getTeachers() { return teachers; }
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