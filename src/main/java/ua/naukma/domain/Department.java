package ua.naukma.domain;

import java.io.Serializable;

public class Department implements Serializable {
    private int id;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String location;
    private String email;

    public Department(int id, String name, Faculty faculty, Teacher head, String location, String email) {
        setId(id);
        setName(name);
        setFaculty(faculty);
        setHead(head);
        setLocation(location);
        setEmail(email);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Faculty getFaculty() { return faculty; }
    public Teacher getHead() { return head; }
    public String getLocation() { return location; }
    public String getEmail() { return email; }

    private void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        this.id = id;
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid department name.");
        }
        this.name = name;
    }

    private void setFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Faculty cannot be null.");
        }
        this.faculty = faculty;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public void setLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Invalid location.");
        }
        this.location = location;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty and must contain '@'.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "Department of " + name + " [" + faculty.getShortName() + "]";
    }
}