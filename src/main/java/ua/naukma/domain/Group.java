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
    private Department department;

    public Group(int id, String name, Faculty faculty, Department department,int course, int admissionYear) {
        setId(id);
        setName(name);
        setFaculty(faculty);
        setDepartment(department);
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
    public Department getDepartment() { return department; }

    public void setDepartment(Department department) {
        this.department = department;
    }

    private void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    private void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    private void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String facultyName = (faculty != null) ? faculty.getName() : "Unknown Faculty";
        String deptName = (department != null) ? department.getName() : "Unknown Department";
        return "Group: " + name + " (ID: " + id + ")"
                + "\nCourse: " + course
                + "\nAdmission year: " + admissionYear
                + "\nFaculty: " + facultyName
                + "\nDepartment: " + deptName;
    }
}