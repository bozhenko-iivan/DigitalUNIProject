package ua.naukma.domain;

import ua.naukma.repository.InMemoryGroupRepository;
import ua.naukma.repository.InMemoryTeacherRepository;
import ua.naukma.repository.PersonRepository;
import ua.naukma.service.GroupService;
import ua.naukma.service.TeacherService;

public class Department {
    private int id;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String location;
    private String email;

    private PersonRepository<Student, Integer> globalStudentRepository;
    private InMemoryTeacherRepository teachers = new InMemoryTeacherRepository();
    private TeacherService teacherService;

    private InMemoryGroupRepository inMemoryGroupRepository = new InMemoryGroupRepository();
    private GroupService groupService;

    public Department(int id, String name, Faculty faculty, Teacher head, String location, String email, PersonRepository<Student, Integer> globalStudentRepository) {
        setId(id);
        setName(name);
        setFaculty(faculty);
        setHead(head);
        setLocation(location);
        setEmail(email);

        if (globalStudentRepository == null) {
            throw new IllegalArgumentException("Global student repository cannot be null.");
        }
        this.globalStudentRepository = globalStudentRepository;

        this.groupService = new GroupService(inMemoryGroupRepository, this);
        this.teacherService = new TeacherService(teachers, this);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Faculty getFaculty() { return faculty; }
    public Teacher getHead() { return head; }
    public String getLocation() { return location; }
    public String getEmail() { return email; }

    public PersonRepository<Student, Integer> getGlobalStudentRepository() { return globalStudentRepository; }
    public TeacherService getTeacherService() { return teacherService; }
    public GroupService getGroupService() { return groupService; }
    public InMemoryTeacherRepository getTeachers() { return teachers; }

    private void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        this.id = id;
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid department name.");
        }
        this.name = name;
    }

    private void setFaculty(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Faculty cannot be null.");
        }
        this.faculty = faculty;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public void setLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Invalid location.");
        }
        this.location = location;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty and must contain '@'.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "Department of " + name + " [" + faculty.getShortName() + "]";
    }
}