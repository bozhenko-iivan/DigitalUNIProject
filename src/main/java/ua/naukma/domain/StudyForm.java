package ua.naukma.domain;

public enum StudyForm {
    BUDGET,
    CONTRACT;

    public static StudyForm fromString (String value) {
        if (value != null) {
            for (StudyForm form : StudyForm.values()) {
                if (form.name().equalsIgnoreCase(value)) {
                    return form;
                }
            }
        }
        throw new IllegalArgumentException("Invalid studyform value: " + value + ". Available: " + java.util.Arrays.toString(StudyForm.values()));
    }
}
