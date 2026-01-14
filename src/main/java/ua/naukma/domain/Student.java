package ua.naukma.domain;

import java.time.LocalDate;

public class Student extends Person {
    private String recordBookNumber;
    private int course;
    private String groupName;
    private int admissionYear;
    private StudyForm studyForm;
    private StudentStatus status;
    private Department department;

    public Student(int id, String firstName, String lastName, String middleName, LocalDate birthDate, String email, String phoneNumber,
                   String recordBookNumber, int course, String groupName, int admissionYear, StudyForm studyForm, StudentStatus status) {
        super(id, firstName, lastName, middleName, birthDate, email, phoneNumber);
        this.recordBookNumber = recordBookNumber;
        this.course = course;
        this.groupName = groupName;
        this.admissionYear = admissionYear;
        this.studyForm = studyForm;
        this.status = status;
    }

    public int getCourse() { return course; }
    public String getGroupName() { return groupName; }
    public Department getDepartment() { return department; }

    public void setCourse(int course) { this.course = course; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public void setDepartment(Department department) { this.department = department; }

    @Override
    public String toString() {
        return "Student " + super.toString() + " [" + groupName + "]";
    }
}
