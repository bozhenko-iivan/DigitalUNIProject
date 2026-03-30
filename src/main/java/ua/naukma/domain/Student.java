package ua.naukma.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class Student extends Person implements Serializable {
    private String recordBookNumber;
    private int course;
    private Group group;
    private int admissionYear;
    private StudyForm studyForm;
    private StudentStatus status;

    public Student(int id, String firstName, String lastName, String middleName,
                   LocalDate birthDate, String email, String phoneNumber,
                   String recordBookNumber, int course, Group groupName,
                   int admissionYear, StudyForm studyForm, StudentStatus status)
    {
        super(id, firstName, lastName, middleName, birthDate, email, phoneNumber);
        setRecordBookNumber(recordBookNumber);
        setCourse(course);
        setGroup(groupName);
        setAdmissionYear(admissionYear);
        setStudyForm(studyForm);
        setStatus(status);
    }
    public int getCourse() { return group.getCourse(); }
    public Group getGroup() { return group; }
    public StudentStatus getStatus() { return status; }
    public StudyForm getStudyForm() { return studyForm; }
    public int getAdmissionYear() { return group.getAdmissionYear(); }
    public String getRecordBookNumber() { return recordBookNumber; }


    public void setStatus(StudentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Student status cannot be null.");
        }
        this.status = status;
    }

    public String getRecordbookNumber() {
        return recordBookNumber;
    }

    public void setRecordbookNumber(String recordbookNum) {
        this.recordBookNumber = recordbookNum;
    }

    public void setStudyForm(StudyForm studyForm) {
        if (studyForm == null) {
            throw new IllegalArgumentException("Study form cannot be null.");
        }
        this.studyForm = studyForm;
    }

    public void setAdmissionYear(int admissionYear) {
        if (admissionYear < 1991 || admissionYear > LocalDate.now().getYear() ) {
            throw new IllegalArgumentException("Invalid admission year.");
        }
        this.admissionYear = admissionYear;
    }

    public void setRecordBookNumber(String recordBookNumber) {
//        if (recordBookNumber == null) {
//      if (recordBookNumber != null && recordBookNumber.length() == 8) {
//            for (char c : recordBookNumber.toCharArray()) {
//                if (!Character.isDigit(c)) {
//                      throw new IllegalArgumentException("Record book number can only contain digits");
//                }
//            }
//        } else {
//            throw new IllegalArgumentException("Record Book Number must be 8 characters long");
//        }
        this.recordBookNumber = recordBookNumber;
    }

    public void setCourse(int course) {
        if (course < 1 || course > 6) {
            throw new IllegalArgumentException("Invalid course number. Only 1-6 courses exist.");
        }
        this.course = course;
    }

    public void setGroup(Group group) {
        if (group == null){
            throw new IllegalArgumentException("Enter group name.");
        }
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student " + getFirstName() + " " + getLastName() + " " + getMiddleName()
                + "\nEmail: " + getEmail() + "\nPhone Number: " + getPhoneNumber() +
                "\nRecord Book Number: " + getRecordBookNumber() + "\nID: " + getId() +
                "\nAdmission year: " + getAdmissionYear() + "\nCourse: " + getCourse() +
                "\nGroupName: " + group.getName() + "\nStudy form: " + getStudyForm() +
                "\nStatus: " + getStatus() + "\nFaculty " + group.getFaculty();
    }

    public String toStringShort() {
        return "Student " + getFirstName() + " " + getLastName() + " " + getMiddleName() + "\nCourse: " + getCourse() +
                "\nStudy form: "  + getStudyForm();
    }
}
