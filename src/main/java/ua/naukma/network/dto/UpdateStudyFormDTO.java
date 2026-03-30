package ua.naukma.network.dto;

import ua.naukma.domain.StudyForm;

import java.io.Serializable;

public record UpdateStudyFormDTO(int studentID, StudyForm studyForm) implements Serializable { }
