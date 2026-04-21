package ua.naukma.network.dto;

public record UpdateTeacherContactsDTO(int teacherId, String phoneNumber, String email) implements java.io.Serializable {

}
