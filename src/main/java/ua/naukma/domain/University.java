package ua.naukma.domain;


import ua.naukma.repository.InMemoryFacultyRepository;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.InMemoryTeacherRepository;
import ua.naukma.repository.PersonRepository;
import ua.naukma.service.FacultyService;

import java.util.List;
import java.util.ArrayList;

public class University {
    private final int id;
    private String fullName;
    private String shortName;
    private String city;
    private String address;
    private String rectorName;
    private FacultyService facultyService;
    private InMemoryFacultyRepository facultyRepository;
    private final PersonRepository<Student, Integer> globalStudentRepository = new InMemoryStudentRepository();

    public University(int id, String fullName, String shortName, String city, String address) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;
        this.facultyService = new FacultyService(this);
        this.facultyRepository = new InMemoryFacultyRepository();
    }

    public PersonRepository<Student, Integer> getGlobalStudentRepository() {
        return globalStudentRepository;
    }

    public FacultyService getFacultyService() {
        return facultyService;
    }
    public String getFullName() {return fullName;}
    public String getShortName() {return shortName;}
    public String getCity() {return city;}
    public String getAddress() {return address;}
    public int getId() {return id;}

    @Override
    public String toString() {
        return fullName + " (" + city + ")";
    }
}
