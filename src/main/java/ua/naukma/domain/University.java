package ua.naukma.domain;


import ua.naukma.server.repository.*;
import ua.naukma.server.service.FacultyService;

import java.io.Serial;
import java.io.Serializable;

public class University implements Serializable {
    private final int id;
    private String fullName;
    private String shortName;
    private String city;
    private String address;
    private String rectorName;
    private FacultyService facultyService;

    private final InMemoryFacultyRepository facultyRepository = new InMemoryFacultyRepository();
    private final InMemoryDepartmentRepository departmentRepository = new InMemoryDepartmentRepository();
    private final InMemoryGroupRepository groupRepository = new InMemoryGroupRepository();
    private final InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();
    private final PersonRepository<Student, Integer> studentRepository = new InMemoryStudentRepository();

    @Serial
    private static final long serialVersionUID = 1L;

    public University(int id, String fullName, String shortName, String city, String address) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;
    }

    public InMemoryFacultyRepository getFacultyRepository() { return facultyRepository; }
    public InMemoryDepartmentRepository getDepartmentRepository() { return departmentRepository; }
    public InMemoryGroupRepository getGroupRepository() { return groupRepository; }
    public InMemoryTeacherRepository getTeacherRepository() { return teacherRepository; }
    public PersonRepository<Student, Integer> getStudentRepository() { return studentRepository; }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getShortName() { return shortName; }
    public String getCity() { return city; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return fullName + " (" + city + ")";
    }
}
