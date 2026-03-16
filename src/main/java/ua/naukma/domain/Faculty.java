package ua.naukma.domain;

import ua.naukma.exception.IncorrectDataException;
import ua.naukma.repository.InMemoryDepartmentRepository;
import ua.naukma.repository.InMemoryFacultyRepository;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.PersonRepository;
import ua.naukma.service.DepartmentService;

public class Faculty {
    private int id;
    private String name;
    private String shortName;
    private Teacher dean;
    private String email;
    private DepartmentService departmentService;
    private PersonRepository<Student, Integer> globalStudentRepository;

    private InMemoryDepartmentRepository inMemoryDepartmentRepository;

    public Faculty(int id, String name, String shortName, Teacher dean, String email, PersonRepository<Student, Integer> globalStudentRepository) {
        setId(id);
        setName(name);
        setShortName(shortName);
        setDean(dean);
        setEmail(email);
        this.inMemoryDepartmentRepository = new InMemoryDepartmentRepository();
        setGlobalStudentRepository(globalStudentRepository);
        this.departmentService = new DepartmentService(this);
    }

    public PersonRepository<Student, Integer> getGlobalStudentRepository() {
        return globalStudentRepository;
    }

    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getShortName() { return shortName; }
    public Teacher getDean() { return dean; }
    public String getEmail() { return email; }

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

    private void setId(int id) {
        if  (id <= 0) {
            throw new IncorrectDataException("id cannot be 0 or negative.");
        }
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

    private void setGlobalStudentRepository(PersonRepository<Student, Integer> globalStudentRepository) {
        if (globalStudentRepository == null) {
            throw new IncorrectDataException("Global student repository cannot be null.");
        }
        this.globalStudentRepository = globalStudentRepository;
    }

    @Override
    public String toString() {
        return "Faculty's ID: " + getId() + "\nFaculty's fullname: " + getName() + "\nShortName: " + getShortName()
        + "\nFaculty's email: " + getEmail();
    }
}
