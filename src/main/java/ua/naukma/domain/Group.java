package ua.naukma.domain;

import ua.naukma.server.repository.GetId;
import ua.naukma.server.repository.GetName;

import java.io.Serializable;
import java.time.LocalDate;

public class Group implements Serializable, GetId, GetName {
    private int id;
    private String name;
    private int course;
    private int admissionYear;
    private Faculty faculty;

    public Group(int id, String name, Faculty faculty, int course, int admissionYear) {
        setId(id);
        setName(name);
        setFaculty(faculty);
        setCourse(course);
        setAdmissionYear(admissionYear);
    }
    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public int getCourse() { return course; }
    public int getAdmissionYear() { return admissionYear; }
    public Faculty getFaculty() { return faculty; }

    private void setAdmissionYear(int admissionYear) {
        if (admissionYear < 1991 || admissionYear > LocalDate.now().getYear() ) {
            throw new IllegalArgumentException("Invalid admission year.");
        }
        this.admissionYear = admissionYear;
    }

    public void setCourse(int course) {
        if (course < 1 || course > 6) {
            throw new IllegalArgumentException("Invalid course.");
        }
        this.course = course;
    }

    private void setFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Faculty cannot be null.");
        }
        this.faculty = faculty;
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String facultyName = (faculty != null) ? faculty.getName() : "Unknown";
        return "Group: " + name + " (ID: " + id + ")"
                + "\nCourse: " + course
                + "\nAdmission year: " + admissionYear
                + "\nFaculty: " + facultyName;
    }
}