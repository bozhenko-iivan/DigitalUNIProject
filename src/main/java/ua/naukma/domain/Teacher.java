package ua.naukma.domain;

import java.time.LocalDate;

public class Teacher extends Person {
    private String position;
    private String degree;
    private String rank;
    private LocalDate hiringDate;
    private double load;

    public Teacher(int id, String firstName, String lastName, String middleName, LocalDate birthDate, String email, String phoneNumber,
                   String position, String degree, String rank, LocalDate hiringDate, double load) {
        super(id, firstName, lastName, middleName, birthDate, email, phoneNumber);
        this.position = position;
        this.degree = degree;
        this.rank = rank;
        this.hiringDate = hiringDate;
        this.load = load;
    }

    public String getPosition() {return position;}
    public String getDegree() {return degree;}
    public String getRank() {return rank;}
    public LocalDate getHiringDate() {return hiringDate;}
    public double getLoad() {return load;}

    @Override
    public String toString() {
        return "Teacher " + super.toString() + " [" + degree + "]";
    }
}