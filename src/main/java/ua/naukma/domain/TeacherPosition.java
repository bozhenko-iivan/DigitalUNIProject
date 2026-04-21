package ua.naukma.domain;

import java.util.Arrays;

public enum TeacherPosition {
    ASSISTANT,
    LECTURER,
    SENIOR_LECTURER,
    DOCENT,
    PROFESSOR,
    HEAD_OF_DEPARTMENT,
    DEAN;

    public static TeacherPosition fromString(String value) {
        if (value != null && !value.isBlank()) {
            for (TeacherPosition position : TeacherPosition.values()) {
                if (position.name().equalsIgnoreCase(value)) {
                    return position;
                }
            }
        }
        throw new IllegalArgumentException("Invalid teacher position value: " + value +
                ". Available: " + Arrays.toString(TeacherPosition.values()));
    }
}