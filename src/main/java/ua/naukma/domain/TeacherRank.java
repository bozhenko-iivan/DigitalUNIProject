package ua.naukma.domain;

public enum TeacherRank {
    DOCENT,
    PROFESSOR,
    SENIOR_RESEARCHER;

    public static TeacherRank fromString (String value) {
        if (value != null && !value.isEmpty()) {
            for (TeacherRank rank : TeacherRank.values()) {
                if (rank.name().equalsIgnoreCase(value)) {
                    return rank;
                }
            }
        }
        throw new IllegalArgumentException("Invalid teacher rank value: " + value + ". Available: " + java.util.Arrays.toString(TeacherRank.values()));
    }
}
