package ua.naukma.domain;

import ua.naukma.repository.InMemoryDepartmentRepository;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.service.DepartmentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Faculty {
    private int id;
    private String name;
    private String shortName;
    private Teacher dean;
    private String email;
    private DepartmentService departmentService;

    private InMemoryStudentRepository inMemoryStudentRepository;
    private InMemoryDepartmentRepository inMemoryDepartmentRepository;

    public Faculty(int id, String name, String shortName, Teacher dean, String email) {
        this.id = id;
        setName(name);
        setShortName(shortName);
        this.dean = dean;
        this.email = email;
        this.inMemoryStudentRepository = new InMemoryStudentRepository();
        this.inMemoryDepartmentRepository = new InMemoryDepartmentRepository();

        this.departmentService = new DepartmentService(this);
    }

    public DepartmentService getDepartmentService() {
        return departmentService;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getShortName() { return shortName; }
    public Teacher getDean() { return dean; }
    public String getEmail() { return email; }


    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public void setShortName(String shortName) {
        if (shortName == null || shortName.isBlank()) {
            throw new IllegalArgumentException("shortname cannot be empty.");
        }
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return "Faculty's ID: " + getId() + "\nFaculty's fullname: " + getName() + "\nShortName: " + getShortName()
        + "\nFaculty's email: " + getEmail();
    }
}
