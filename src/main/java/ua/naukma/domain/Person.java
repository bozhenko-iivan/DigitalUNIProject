package ua.naukma.domain;
import ua.naukma.server.repository.GetId;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Person implements Serializable, GetId {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;

    public Person(int id, String firstName, String lastName, String middleName, LocalDate birthDate, String email, String phoneNumber) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setMiddleName(middleName);
        setBirthDate(birthDate);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }
    @Override
    public int getId() {return id;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getMiddleName() {return middleName;}
    public LocalDate getBirthDate() {return birthDate;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}

    public void setId(int id) {this.id = id;}
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate (LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return id + ": " + lastName + " " + firstName;
    }
}