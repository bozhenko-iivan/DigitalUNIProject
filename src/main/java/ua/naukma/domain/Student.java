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
        setRecordBookNumber(recordBookNumber);
        setCourse(course);
        setGroupName(groupName);
        setAdmissionYear(admissionYear);
        setStudyForm(studyForm);
        setStatus(status);
        setDepartment(department);
    }

    public int getCourse() { return course; }
    public String getGroupName() { return groupName; }
    public Department getDepartment() { return department; }
    public StudentStatus getStatus() { return status; }
    public StudyForm getStudyForm() { return studyForm; }
    public int getAdmissionYear() { return admissionYear; }
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

    public void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }
        this.department = department;
    }
    public void setAdmissionYear(int admissionYear) {
        if (admissionYear < 1991 || admissionYear > LocalDate.now().getYear() ) {
            throw new IllegalArgumentException("Invalid admission year.");
        }
        this.admissionYear = admissionYear;
    }

    public void setRecordBookNumber(String recordBookNumber) {
        if (recordBookNumber != null && recordBookNumber.length() == 8) {
            for (char c : recordBookNumber.toCharArray()) {
                if (!Character.isDigit(c)) {
                    throw new IllegalArgumentException("Record book number can only contain digits");
                }
            }
        } else {
            throw new IllegalArgumentException("Record Book Number must be 8 characters long");
        }
        this.recordBookNumber = recordBookNumber;
    }

    public void setCourse(int course) {
        if (course < 1 || course > 6) {
            throw new IllegalArgumentException("Invalid course number. Only 1-6 courses exist.");
        }
        this.course = course;
    }

    public void setGroupName(String groupName) {
        if (groupName == null || groupName.isEmpty()){
            throw new IllegalArgumentException("Enter group name.");
        }
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Student " + super.toString() + " [" + groupName + "]";
    }
}
