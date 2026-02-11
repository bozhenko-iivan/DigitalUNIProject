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
        setFirstName(firstName);
        setLastName(lastName);
        setMiddleName(middleName);
        setBirthDate(birthDate);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public int getId() {return id;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getMiddleName() {return middleName;}
    public LocalDate getBirthDate() {return birthDate;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }
            this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        if (middleName == null || middleName.isEmpty()) {
            throw new IllegalArgumentException("Middle name cannot be empty.");
        }
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() == 10) {
            for (int i=0; i<phoneNumber.length(); i++) {
                if (!Character.isDigit(phoneNumber.charAt(i))) {
                    throw new IllegalArgumentException("Only digits are allowed. Don't use space to separate numbers. Example: 0981231234");
                }
            }
            this.phoneNumber = phoneNumber;

        } else {
            throw new IllegalArgumentException("Phone number must contain 10 digits. Example: 0981231234");
        }
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email cannot be empty and must contain '@'.");
        }
        this.email = email;
    }

    public void setBirthDate (LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be empty.");
        }

        if (birthDate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Future fiction? Future dates not allowed.");
        }

        if (birthDate.isBefore(LocalDate.of(1900,1,1))) {
            throw new IllegalArgumentException("Can't be. Set a birth date not as old.");
        }
    }

    @Override
    public String toString() {
        return id + ": " + lastName + " " + firstName;
    }
}
