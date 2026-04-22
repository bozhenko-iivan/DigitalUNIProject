package ua.naukma.domain;

import ua.naukma.exception.IncorrectDataException;
import ua.naukma.server.repository.GetId;
import ua.naukma.server.repository.GetName;

import java.io.Serial;
import java.io.Serializable;

public class Faculty implements Serializable, GetId, GetName {
    private int id;
    private String name;
    private String shortName;
    private Teacher dean;
    private String email;
    private University university;

    @Serial
    private static final long serialVersionUID = 1L;

    public Faculty(int id, String name, String shortName, Teacher dean, String email, University university) {
        setId(id);
        setName(name);
        setShortName(shortName);
        setDean(dean);
        setEmail(email);
        setUniversity(university);
    }
    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    public String getShortName() { return shortName; }
    public Teacher getDean() { return dean; }
    public String getEmail() { return email; }
    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    private void setShortName(String shortName) {
        if (shortName == null || shortName.isBlank()) {
            throw new IllegalArgumentException("shortname cannot be empty.");
        }
        this.shortName = shortName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDean(Teacher dean) {
        this.dean = dean;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IncorrectDataException("Email cannot be empty.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "Faculty's ID: " + getId() + "\nFaculty's fullname: " + getName() + "\nShortName: " + getShortName()
        + "\nFaculty's email: " + getEmail();
    }
}
