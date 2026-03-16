package ua.naukma.domain;

import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.PersonRepository;
import ua.naukma.service.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private int id;
    private String name;
    private Department department;
    private int course;
    private int admissionYear;
    private List<Student> students;

    private InMemoryStudentRepository inMemoryStudentRepository;
    private StudentService studentService;

    public Group(int id, String name, Department department, int course, int admissionYear) {
        setId(id);
        setName(name);
        setDepartment(department);
        setCourse(course);
        setAdmissionYear(admissionYear);

        this.inMemoryStudentRepository = new InMemoryStudentRepository();
        this.studentService = new StudentService(this.inMemoryStudentRepository, Group.this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public int getCourse() {
        return course;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public List<Student> getStudents() {
        return inMemoryStudentRepository.findAll();
    }

    public StudentService getStudentService() {
        return studentService;
    }

    private void setAdmissionYear(int admissionYear) {
        if (admissionYear < 1991 || admissionYear > LocalDate.now().getYear() ) {
            throw new IllegalArgumentException("Invalid admission year.");
        }
        this.admissionYear = admissionYear;

    }

    private void setCourse(int course) {
        if (course < 1 || course > 6) {
            throw new IllegalArgumentException("Invalid course.");
        }
        this.course = course;
    }

    private void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }
        this.department = department;
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name = name;
    }

    private void setId(int id) {
        if (String.valueOf(id).length() != 7 || id <= 0) {
            throw new IllegalArgumentException("Invalid id.");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        String deptName = (department != null) ? department.getName() : "Unknown";
        return "Group: " + name + " (ID: " + id + ")"
                + "\nCourse: " + course
                + "\nAdmission year: " + admissionYear
                + "\nDepartment: " + deptName;
    }
}