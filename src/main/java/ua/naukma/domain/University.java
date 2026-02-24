package ua.naukma.domain;

import ua.naukma.repository.InMemoryFacultyRepository;

import java.util.HashMap;
import java.util.Map;

public class University {
    private String fullName;
    private String shortName;
    private String city;
    private String address;
    private String rectorName;

    private InMemoryFacultyRepository faculties = new InMemoryFacultyRepository();

    public University(String fullName, String shortName, String city, String address) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;
    }

    public String getFullName() {return fullName;}
    public String getShortName() {return shortName;}
    public String getCity() {return city;}
    public String getAddress() {return address;}
    public InMemoryFacultyRepository getFaculties() {return faculties;}
    @Override
    public String toString() {
        return fullName + " (" + city + ")";
    }
}
