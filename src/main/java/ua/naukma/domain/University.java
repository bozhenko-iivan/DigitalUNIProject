package ua.naukma.domain;

import ua.naukma.server.repository.GetId;
import ua.naukma.server.repository.GetName;

import java.io.Serial;
import java.io.Serializable;

public class University implements Serializable, GetId, GetName {
    private final int id;
    private String fullName;
    private String shortName;
    private String city;
    private String address;
    private String rectorName;

    @Serial
    private static final long serialVersionUID = 1L;

    public University(int id, String fullName, String shortName, String city, String address) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return fullName; }

    public String getFullName() { return fullName; }
    public String getShortName() { return shortName; }
    public String getCity() { return city; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return fullName + " (" + city + ")";
    }
}
