package ua.naukma.server.service;

import ua.naukma.domain.Teacher;
import ua.naukma.domain.University;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class TeacherService implements Service<Teacher, Integer> {
    private final PersonRepository<Teacher, Integer> repository;

    public TeacherService(PersonRepository<Teacher, Integer> repository) {
        this.repository = repository;
    }

    @Override
    public void add(Teacher teacher) throws DuplicateEntityException {
        if (repository.findById(teacher.getId()).isPresent()) {
            throw new DuplicateEntityException("Teacher with id " + teacher.getId() + " already exists.");
        }
        repository.save(teacher);
    }

    @Override
    public void deleteById(Integer id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Teacher with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }

    @Override
    public Teacher findById(Integer id) throws EntityNotFoundException {
        Optional<Teacher> teacher = repository.findById(id);
        if (teacher.isPresent()) {
            return teacher.get();
        } else {
            throw new EntityNotFoundException("Teacher with id " + id + " doesn't exist.");
        }
    }

//    public Teacher findByPIB(String firstName, String lastName, String middleName) throws EntityNotFoundException {
//        Optional<Teacher> teacher = repository.findByPIB(firstName, lastName, middleName);
//        if (teacher.isPresent()) {
//            return teacher.get();
//        } else {
//            throw new EntityNotFoundException("Teacher " + lastName + " " + firstName + " doesn't exist.");
//        }
//    }

    @Override
    public List<Teacher> findAll() {
        if  (repository.findAll().isEmpty()) {
            throw new EntityNotFoundException("No teacher has been found!");
        }
        return repository.findAll();
    }

    public List<Teacher> findAllByDepartmentId(int departmentId) {
        return repository.findAll().stream()
                .filter(t -> t.getDepartment() != null && t.getDepartment().getId() == departmentId)
                .collect(Collectors.toList());
    }

    public int getAllTeachersCount(int departmentId) {
        return (int) repository.findAll().stream().filter(t -> t.getDepartment().getId() == departmentId).count();
    }
}