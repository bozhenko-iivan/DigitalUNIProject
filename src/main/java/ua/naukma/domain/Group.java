package ua.naukma.domain;

import java.time.LocalDate;

public class Group {
    private int id;
    private String name;
    private int course;
    private int admissionYear;
    private Group group;
    private Faculty faculty;

    public Group(int id, String name, Faculty faculty, int course, int admissionYear) {
        setId(id);
        setName(name);
        setFaculty(faculty);
        setCourse(course);
        setAdmissionYear(admissionYear);
    }

    public int getId() { return id; }
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

    private void setCourse(int course) {
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

    private void setId(int id) {
        if (String.valueOf(id).length() != 7 || id <= 0) {
            throw new IllegalArgumentException("Invalid id.");
        }
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