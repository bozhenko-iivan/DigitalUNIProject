package ua.naukma.network.dto;

import ua.naukma.domain.Subject;

import java.io.Serializable;

public record SetStudentGrade(int studentID, int grade, Subject subject) implements Serializable { }