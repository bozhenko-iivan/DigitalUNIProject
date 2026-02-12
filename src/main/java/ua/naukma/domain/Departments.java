package ua.naukma.domain;

public enum Departments {
    INFORMATICS,
    MATHEMATICS,
    FINANCE,
    MARKETING,
    HISTORY,
    LITERATURE,
    PHILOLOGY;

    public static Departments fromString (String value) {
        if (value != null && !value.trim().isEmpty()) {
            for (Departments department : Departments.values()) {
                if (department.name().equalsIgnoreCase(value)) {
                    return department;
                }
            }
        }
        throw new IllegalArgumentException("Invalid department value: " + value + ". Available: " + java.util.Arrays.toString(Departments.values()));
    }

}