package ua.naukma.server.service;

import ua.naukma.domain.Group;
import ua.naukma.domain.Student;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class StudentService {
    private final PersonRepository<Student, Integer> repository;

    public StudentService(PersonRepository<Student, Integer> repository) {
        this.repository = repository;
    }

    public void addStudent(Student student) throws DuplicateEntityException {
        if (repository.findById(student.getId()).isPresent()) {
            throw new DuplicateEntityException("Student with id " + student.getId() + " already exists.");
        }
        if (student.getRecordbookNumber() == null || student.getRecordbookNumber().isEmpty()) {
            String recordbookNum = generateRecordbookNum(student.getLastName(), student.getId(), student.getAdmissionYear());
            student.setRecordbookNumber(recordbookNum);
        }

        repository.save(student);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Student with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }

    public Student findById(int id) throws EntityNotFoundException {
        Optional<Student> student = repository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new EntityNotFoundException("Student with id " + id + " doesn't exist.");
        }
    }

    public Student findByPIB(String firstName, String lastName, String middleName) throws EntityNotFoundException {
        Optional<Student> student = repository.findByPIB(firstName, lastName, middleName);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new EntityNotFoundException("Student " + lastName + " " + firstName + " doesn't exist.");
        }
    }

    public List<Student> findAllByGroupId(int groupId) {
        return repository.findAll().stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getId() == groupId)
                .collect(Collectors.toList());
    }

    public void updateStudent(Student student) throws EntityNotFoundException {
        if (repository.findById(student.getId()).isEmpty()) {
            throw new EntityNotFoundException("Student with id " + student.getId() + " doesn't exist.");
        }
        repository.save(student);
    }

    private String generateRecordbookNum(String lastName, int id, int year) {
        Random rand = new Random();

        char firstChar = lastName.length() > 0 ? lastName.charAt(0) : 'X';
        char secondChar = lastName.length() > 1 ? lastName.charAt(1) : 'Y';
        int safeYear = (year > 0) ? year : LocalDate.now().getYear();

        return firstChar + "-" + (id % safeYear) + secondChar + "-" + rand.nextInt(999);
    }

    public long getStudentsCount(int groupId) throws EntityNotFoundException {
        return repository.findAll().stream().filter(s -> s.getGroup() != null && s.getGroup().getId() == groupId).count();
    }
}