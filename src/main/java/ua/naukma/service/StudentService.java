package ua.naukma.service;

import ua.naukma.domain.Student;
import ua.naukma.repository.Repository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class StudentService {
    private final Repository<Student, Integer> repository;

    public StudentService(Repository<Student, Integer> repository) {
        this.repository = repository;
    }

    public void addStudent(Student student) {
        if (repository.findById(student.getId()).isPresent()) {
            throw new IllegalStateException("Student already exists");
        }
        int age = Period.between(student.getBirthDate(), LocalDate.now()).getYears();
        if (age < 16) {
            System.out.println("Student age is too young " + age + " years. Minimal age - 16");
            return;
        }

        System.out.println("Student is successfully added");
    }

//    public List<Student> getAllStudents() {
//        return repository.findAll();
//    }

    public Optional<Student> findById(Integer id) {
        return repository.findById(id);
    }

//    public List<Student> findByGroupName(String groupName) {
//        return repository.findAll().stream()
//                .filter(student -> student.getGroupName().equals(groupName)).toList();
//    }
}
