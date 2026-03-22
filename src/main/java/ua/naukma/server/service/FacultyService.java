package ua.naukma.server.service;

import ua.naukma.domain.Faculty;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FacultyService {
    private final Repository<Faculty, Integer> repository;

    public FacultyService(Repository<Faculty, Integer> repository) {
        this.repository = repository;
    }

    public void addFaculty(Faculty f) throws DuplicateEntityException {
        if (repository.findById(f.getId()).isPresent()) {
            throw new DuplicateEntityException("Faculty with id " + f.getId() + " already exists.");
        }
        repository.save(f);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Faculty with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }

    public Faculty findById(int id) throws EntityNotFoundException {
        Optional<Faculty> faculty = repository.findById(id);
        if (faculty.isPresent()) {
            return faculty.get();
        } else {
            throw new EntityNotFoundException("Faculty with id " + id + " doesn't exist.");
        }
    }

    public List<Faculty> findAllByUniId(int uniId) throws EntityNotFoundException {
        return repository.findAll().stream()
                .filter(f -> f.getUniversity().getId() == uniId)
                .collect(Collectors.toList());
    }
}