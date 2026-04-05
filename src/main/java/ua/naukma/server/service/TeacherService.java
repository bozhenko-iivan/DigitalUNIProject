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
public class TeacherService extends EntityService<Teacher, Integer> {
    private final PersonRepository<Teacher, Integer> repository;
    public TeacherService(PersonRepository<Teacher, Integer> repository, Class<Teacher> clazz) {
        super(repository, clazz);
        this.repository = repository;
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