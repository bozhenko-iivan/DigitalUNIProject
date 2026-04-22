package ua.naukma.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class Student extends Person implements Serializable {
    private String recordBookNumber;
    private Group group;
    private StudyForm studyForm;
    private StudentStatus status;

    public Student(int id, String firstName, String lastName, String middleName,
                   LocalDate birthDate, String email, String phoneNumber,
                   String recordBookNumber, Group group,
                   StudyForm studyForm, StudentStatus status)
    {
        super(id, firstName, lastName, middleName, birthDate, email, phoneNumber);
        setRecordBookNumber(recordBookNumber);
        setGroup(group);
        setStudyForm(studyForm);
        setStatus(status);
    }

    public int getCourse() { return group != null ? group.getCourse() : 0; }
    public int getAdmissionYear() { return group != null ? group.getAdmissionYear() : 0; }

    public Group getGroup() { return group; }
    public StudentStatus getStatus() { return status; }
    public StudyForm getStudyForm() { return studyForm; }
    public String getRecordBookNumber() { return recordBookNumber; }

    public void setStatus(StudentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Student status cannot be null.");
        }
        this.status = status;
    }

    public void setStudyForm(StudyForm studyForm) {
        if (studyForm == null) {
            throw new IllegalArgumentException("Study form cannot be null.");
        }
        this.studyForm = studyForm;
    }

    public void setRecordBookNumber(String recordBookNumber) {
        this.recordBookNumber = recordBookNumber;
    }

    // ИЗМЕНЕНИЕ 3: Теперь мы просто меняем группу. Никаких локальных переменных
    // course обновлять не нужно, геттер сам подтянет новый курс.
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student " + getFirstName() + " " + getLastName() + " " + getMiddleName()
                + "\nEmail: " + getEmail() + "\nPhone Number: " + getPhoneNumber() +
                "\nRecord Book Number: " + getRecordBookNumber() + "\nID: " + getId() +
                "\nAdmission year: " + getAdmissionYear() + "\nCourse: " + getCourse() +
                "\nGroupName: " + (group != null ? group.getName() : "None") +
                "\nStudy form: " + getStudyForm() +
                "\nStatus: " + getStatus() +
                "\nFaculty " + (group != null ? group.getFaculty().getName() : "None");
    }

    public String toStringShort() {
        return "Student " + getFirstName() + " " + getLastName() + " " + getMiddleName() +
                "\nCourse: " + getCourse() +
                "\nStudy form: "  + getStudyForm();
    }
}