package ua.naukma.domain;

import ua.naukma.exception.IncorrectDataException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.IllegalFormatCodePointException;

public class Teacher extends Person implements Serializable {
    private TeacherPosition  position;
    private TeacherDegree  degree;
    private TeacherRank  rank;
    private LocalDate hiringDate;
    private double load;
    private Department department;

    public Teacher(int id, String firstName, String lastName, String middleName, LocalDate birthDate, String email, String phoneNumber,
                   TeacherPosition position, TeacherDegree degree, TeacherRank rank, LocalDate hiringDate, double load, Department department) {
        super(id, firstName, lastName, middleName, birthDate, email, phoneNumber);
        setPosition(position);
        setDegree(degree);
        setRank(rank);
        setHiringDate(hiringDate);
        setLoad(load);
        setDepartment(department);
    }

    private void setDepartment(Department department) {
        this.department = department;
    }

    public TeacherPosition getPosition() {return position;}
    public TeacherDegree getDegree() {return degree;}
    public TeacherRank getRank() {return rank;}
    public LocalDate getHiringDate() {return hiringDate;}
    public double getLoad() {return load;}
    public Department getDepartment() {return department;}

    public void setPosition(TeacherPosition position) {
        this.position = position;
    }
    public void setDegree(TeacherDegree degree) {
        this.degree = degree;
    }
    public void setRank(TeacherRank rank) {
        this.rank = rank;
    }
    private void setHiringDate(LocalDate hiringDate) {
        this.hiringDate = hiringDate;
    }
    public void setLoad(double load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "Teacher " + super.toString() + " [" + degree + "]";
    }
}