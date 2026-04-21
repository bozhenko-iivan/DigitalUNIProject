package ua.naukma.network.dto;

public record UpdateTeacherAcademicDTO(int teacherId, ua.naukma.domain.TeacherPosition position, ua.naukma.domain.TeacherDegree degree, ua.naukma.domain.TeacherRank rank, double load) implements java.io.Serializable {

}
