package ua.naukma.domain;

import java.util.Arrays;

public enum TeacherRank {
    NONE,
    DOCENT,
    PROFESSOR,
    SENIOR_RESEARCHER;

    public static TeacherRank fromString (String value) {
        if (value != null && !value.isBlank()) {
            for (TeacherRank rank : TeacherRank.values()) {
                if (rank.name().equalsIgnoreCase(value)) {
                    return rank;
                }
            }
        }
        throw new IllegalArgumentException("Invalid teacher rank value: " + value +
                ". Available: " + Arrays.toString(TeacherRank.values()));
    }
}