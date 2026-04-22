package ua.naukma.domain;

import ua.naukma.server.repository.GetId;
import ua.naukma.server.repository.GetName;

import java.io.Serializable;

public class Department implements Serializable, GetId, GetName {
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
    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    public Faculty getFaculty() { return faculty; }
    public Teacher getHead() { return head; }
    public String getLocation() { return location; }
    public String getEmail() { return email; }

    public void setId(int id) {
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
        String facultyName = (faculty != null) ? faculty.getShortName() : "Unknown Faculty";
        String headName = (head != null) ? head.getLastName() + " " + head.getFirstName() : "No head";

        return "Department: " + name + " (ID: " + id + ")" +
                "\nLocation: " + location +
                "\nEmail: " + email +
                "\nFaculty: " + facultyName +
                "\nHead: " + headName;
    }

    public String findDepartment() {
        return "Deps's ID: " + getId() + "\nFaculty's fullname: " + getName()
                + "\nDeps's email: " + getEmail() + "\nLocation: " + getLocation();
    }
}