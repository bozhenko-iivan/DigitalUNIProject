package ua.naukma.network.dto;

import ua.naukma.domain.StudentStatus;

import java.io.Serializable;

public record UpdateStudentStatusDTO(int studentID, StudentStatus status) implements Serializable { }
