package ua.naukma.domain;
import java.time.LocalDate;

public abstract class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;

    public Person(int id, String firstName, String lastName, String middleName, LocalDate birthDate, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {return id;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getMiddleName() {return middleName;}
    public LocalDate getBirthDate() {return birthDate;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}

    @Override
    public String toString() {
        return id + ": " + lastName + " " + firstName;
    }
}
