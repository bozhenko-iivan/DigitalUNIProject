package ua.naukma.domain;

public enum StudentStatus {
    STUDYING,
    ACADEMIC_LEAVE,
    EXPELLED;

    public static StudentStatus fromString (String value) {
        if (value != null) {
            for (StudentStatus status : StudentStatus.values()) {
                if (status.name().equalsIgnoreCase(value)) {
                    return status;
                }
            }
        }
        throw new IllegalArgumentException("Invalid study status value: " + value + ". Available: " + java.util.Arrays.toString(StudentStatus.values()));
    }

}
