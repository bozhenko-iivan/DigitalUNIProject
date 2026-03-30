package ua.naukma.network.dto;

import java.io.Serializable;

public record UpdateContactsDTO(int studentId, String phoneNumber, String email) implements Serializable {}