package ua.naukma.server.service;

import ua.naukma.domain.Department;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DepartmentService {
    private final Repository<Department, Integer> repository;

    public DepartmentService(Repository<Department, Integer> repository) {
        this.repository = repository;
    }

    public void addDepartment(Department d) throws DuplicateEntityException {
        if (repository.findById(d.getId()).isPresent()) {
            throw new DuplicateEntityException("Department with id " + d.getId() + " already exists.");
        }
        repository.save(d);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Department with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }

    public Department findById(int id) throws EntityNotFoundException {
        Optional<Department> department = repository.findById(id);
        if (department.isPresent()) {
            return department.get();
        } else {
            throw new EntityNotFoundException("Department with id " + id + " doesn't exist.");
        }
    }

    public List<Department> findAllByFacultyId(int facultyId) {
        return repository.findAll().stream()
                .filter(d -> d.getFaculty().getId() == facultyId)
                .collect(Collectors.toList());
    }
}