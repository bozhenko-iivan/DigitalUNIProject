package ua.naukma.domain;

import ua.naukma.exception.IncorrectDataException;

import java.time.LocalDate;
import java.util.IllegalFormatCodePointException;

public class Teacher extends Person {
    private TeacherPosition  position;
    private TeacherDegree  degree;
    private TeacherRank  rank;
    private LocalDate hiringDate;
    private double load;

    public Teacher(int id, String firstName, String lastName, String middleName, LocalDate birthDate, String email, String phoneNumber,
                   TeacherPosition position, TeacherDegree degree, TeacherRank rank, LocalDate hiringDate, double load) {
        super(id, firstName, lastName, middleName, birthDate, email, phoneNumber);
        setPosition(position);
        setDegree(degree);
        setRank(rank);
        setHiringDate(hiringDate);
        setLoad(load);
    }

    public TeacherPosition getPosition() {return position;}
    public TeacherDegree getDegree() {return degree;}
    public TeacherRank getRank() {return rank;}
    public LocalDate getHiringDate() {return hiringDate;}
    public double getLoad() {return load;}

    public void setPosition(TeacherPosition position) {
        if (position == null) {
            throw new NullPointerException("Position is null");
        }
        this.position = position;
    }
    public void setDegree(TeacherDegree degree) {
        if (degree == null) {
            throw new NullPointerException("Degree is null");
        }
        this.degree = degree;
    }
    public void setRank(TeacherRank rank) {
        if (rank == null) {
            throw new NullPointerException("Rank is null");
        }
        this.rank = rank;
    }
    private void setHiringDate(LocalDate hiringDate) {
        if (hiringDate == null || hiringDate.isAfter(LocalDate.now())) {
            throw new IncorrectDataException("Hiring Date is invalid.");
        }
        this.hiringDate = hiringDate;
    }
    public void setLoad(double load) {
        if (load < 0) {
            throw new IncorrectDataException("Load cannot be negative.");
        }
        this.load = load;
    }

    @Override
    public String toString() {
        return "Teacher " + super.toString() + " [" + degree + "]";
    }
}