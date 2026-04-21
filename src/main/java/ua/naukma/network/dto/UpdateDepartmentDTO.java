package ua.naukma.network.dto;

import java.io.Serializable;

public record UpdateDepartmentDTO(int departmentId, String location, String email) implements Serializable {

}
