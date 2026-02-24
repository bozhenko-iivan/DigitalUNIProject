package ua.naukma.domain;

import java.time.LocalDate;

public class Teacher extends Person {
    private TeacherPosition  position;
    private TeacherDegree  degree;
    private TeacherRank  rank;
    private LocalDate hiringDate;
    private double load;

    public Teacher(int id, String firstName, String lastName, String middleName, LocalDate birthDate, String email, String phoneNumber,
                   TeacherPosition position, TeacherDegree degree, TeacherRank rank, LocalDate hiringDate, double load) {
        super(id, firstName, lastName, middleName, birthDate, email, phoneNumber);
        this.position = position;
        this.degree = degree;
        this.rank = rank;
        this.hiringDate = hiringDate;
        this.load = load;
    }

    public TeacherPosition getPosition() {return position;}
    public TeacherDegree getDegree() {return degree;}
    public TeacherRank getRank() {return rank;}
    public LocalDate getHiringDate() {return hiringDate;}
    public double getLoad() {return load;}

    @Override
    public String toString() {
        return "Teacher " + super.toString() + " [" + degree + "]";
    }
}