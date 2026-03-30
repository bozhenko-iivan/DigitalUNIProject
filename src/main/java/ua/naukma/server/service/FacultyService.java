package ua.naukma.server.service;

import ua.naukma.domain.Faculty;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class FacultyService implements Service<Faculty, Integer> {
    private final Repository<Faculty, Integer> repository;

    public FacultyService(Repository<Faculty, Integer> repository) {
        this.repository = repository;
    }

    @Override
    public void add(Faculty f) throws DuplicateEntityException {
        if (repository.findById(f.getId()).isPresent()) {
            throw new DuplicateEntityException("Faculty with id " + f.getId() + " already exists.");
        }
        repository.save(f);
    }

    @Override
    public void deleteById(Integer id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Faculty with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }

    @Override
    public Faculty findById(Integer id) throws EntityNotFoundException {
        Optional<Faculty> faculty = repository.findById(id);
        if (faculty.isPresent()) {
            return faculty.get();
        } else {
            throw new EntityNotFoundException("Faculty with id " + id + " doesn't exist.");
        }
    }

    @Override
    public List<Faculty> findAll() {
        if  (repository.findAll().isEmpty()) {
            throw new EntityNotFoundException("No faculties have been found!");
        }
        return repository.findAll();
    }

    public List<Faculty> findAllByUniId(int uniId) throws EntityNotFoundException {
        return repository.findAll().stream()
                .filter(f -> f.getUniversity().getId() == uniId)
                .collect(Collectors.toList());
    }

    public int getAllFacultiesCount(int uniId) throws EntityNotFoundException {
        return (int) repository.findAll().stream().filter(f -> f.getUniversity() != null
                && f.getUniversity().getId() == uniId).count();
    }
}