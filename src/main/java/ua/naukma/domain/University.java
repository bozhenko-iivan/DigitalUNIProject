package ua.naukma.domain;

public class University {
    private String fullName;
    private String shortName;
    private String city;
    private String address;
    private String rectorName;

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

    @Override
    public String toString() {
        return fullName + " (" + city + ")";
    }
}
