package ua.naukma.domain;

import java.util.Arrays;

public enum TeacherDegree {
    NONE,
    MASTER,
    POSTGRADUATE,
    PHD,
    DOCTOR_OF_SCIENCES;

    public static TeacherDegree fromString(String value) {
        if (value != null && !value.isBlank()) {
            for (TeacherDegree degree : TeacherDegree.values()) {
                if (degree.name().equalsIgnoreCase(value)) {
                    return degree;
                }
            }
        }
        throw new IllegalArgumentException("Invalid teacher degree value: " + value +
                ". Available: " + Arrays.toString(TeacherDegree.values()));
    }
}