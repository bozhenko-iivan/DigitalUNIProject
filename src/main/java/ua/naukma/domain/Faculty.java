package ua.naukma.domain;

public class Faculty {
    private int id;
    private String name;
    private String shortName;
    private Teacher dean;
    private String contacts;

    public Faculty(int id, String name, String shortName, Teacher dean, String contacts) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.dean = dean;
        this.contacts = contacts;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getShortName() { return shortName; }
    public Teacher getDean() { return dean; }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public void setShortName(String shortName) {
        if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("shortname cannot be empty.");
        }
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        String deanName = (dean != null) ? dean.getLastName() : "Vacant";
        return shortName + " (Dean: " + deanName + ")";
    }
}
