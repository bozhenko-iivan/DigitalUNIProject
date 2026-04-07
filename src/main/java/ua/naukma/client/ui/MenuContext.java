package ua.naukma.client.ui;

import ua.naukma.domain.*;

public class MenuContext {
    private MenuLevel current_level;
    private Faculty current_faculty;
    private Department current_department;
    private University current_university;
    private Group current_group;
    private final SystemUser current_user;
    private Student current_student;
    public MenuContext(SystemUser current_user, MenuLevel current_level) {
        this.current_user = current_user;
        this.current_level = current_level;
    }
    public MenuLevel getCurrent_level() {
        return current_level;
    }
    public void setCurrent_level(MenuLevel current_level) {
        this.current_level = current_level;
    }
    public Faculty getCurrent_faculty() {
        return current_faculty;
    }
    public void setCurrent_faculty(Faculty current_faculty) {
        this.current_faculty = current_faculty;
    }
    public Department getCurrent_department() {
        return current_department;
    }
    public void setCurrent_department(Department current_department) {
        this.current_department = current_department;
    }
    public University getCurrent_university() {
        return current_university;
    }
    public void setCurrent_university(University current_university) {
        this.current_university = current_university;
    }
    public Group getCurrent_group() {
        return current_group;
    }
    public void setCurrent_group(Group current_group) {
        this.current_group = current_group;
    }
    public Student getCurrent_student() {
        return current_student;
    }
    public void setCurrent_student(Student current_student) {
        this.current_student = current_student;
    }
    public SystemUser getCurrent_user() {
        return current_user;
    }
}
