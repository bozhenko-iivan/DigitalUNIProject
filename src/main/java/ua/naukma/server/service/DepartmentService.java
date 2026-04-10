package ua.naukma.server.service;

import ua.naukma.domain.Department;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class DepartmentService extends EntityService<Department, Integer> {
    private final Repository<Department, Integer> repository;

    public DepartmentService(Repository<Department, Integer> repository, Class<Department> entityClass) {
        super(repository, entityClass);
        this.repository = repository;
    }
    public List<Department> findAllByFacultyId(int facultyId) {
        return repository.findAll().stream()
                .filter(d -> d.getFaculty().getId() == facultyId)
                .collect(Collectors.toList());
    }
    public int getAllDepartmentsCount(int facultyId) {
        return (int) repository.findAll().stream().filter(d -> d.getFaculty() != null
                && d.getFaculty().getId() == facultyId).count();
    }
}