package ua.naukma.server.service;

import ua.naukma.domain.Teacher;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeacherService {
    private final PersonRepository<Teacher, Integer> repository;

    public TeacherService(PersonRepository<Teacher, Integer> repository) {
        this.repository = repository;
    }

    public void addTeacher(Teacher teacher) throws DuplicateEntityException {
        if (repository.findById(teacher.getId()).isPresent()) {
            throw new DuplicateEntityException("Teacher with id " + teacher.getId() + " already exists.");
        }
        repository.save(teacher);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Teacher with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }

    public Teacher findById(int id) throws EntityNotFoundException {
        Optional<Teacher> teacher = repository.findById(id);
        if (teacher.isPresent()) {
            return teacher.get();
        } else {
            throw new EntityNotFoundException("Teacher with id " + id + " doesn't exist.");
        }
    }

    public Teacher findByPIB(String firstName, String lastName, String middleName) throws EntityNotFoundException {
        Optional<Teacher> teacher = repository.findByPIB(firstName, lastName, middleName);
        if (teacher.isPresent()) {
            return teacher.get();
        } else {
            throw new EntityNotFoundException("Teacher " + lastName + " " + firstName + " doesn't exist.");
        }
    }

    public List<Teacher> findAllByDepartmentId(int departmentId) {
        return repository.findAll().stream()
                .filter(t -> t.getDepartment() != null && t.getDepartment().getId() == departmentId)
                .collect(Collectors.toList());
    }

    public void updateTeacher(Teacher teacher) throws EntityNotFoundException {
        if (repository.findById(teacher.getId()).isEmpty()) {
            throw new EntityNotFoundException("Teacher with id " + teacher.getId() + " doesn't exist.");
        }
        repository.save(teacher);
    }
}