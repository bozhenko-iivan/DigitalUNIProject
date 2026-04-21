package ua.naukma.network.dto;

import java.io.Serializable;

public record UpdateFacultyDTO(int facultyId, String newEmail) implements Serializable {

}
