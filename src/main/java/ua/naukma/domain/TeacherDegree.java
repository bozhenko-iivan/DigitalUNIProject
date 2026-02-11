package ua.naukma.domain;

public enum TeacherDegree {
    NONE,
    PHD,
    DOCTOR_OF_SCIENCES;


    public static TeacherDegree fromString (String value) {
        if (value != null) {
            for (TeacherDegree degree : TeacherDegree.values()) {
                if (degree.name().equalsIgnoreCase(value)) {
                    return degree;
                }
            }
        }
        throw new IllegalArgumentException("Invalid teacher degree value: " + value + ". Available: " + java.util.Arrays.toString(TeacherDegree.values()));
    }
}