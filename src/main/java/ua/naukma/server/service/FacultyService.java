package ua.naukma.server.service;

import ua.naukma.domain.Faculty;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class FacultyService extends EntityService<Faculty, Integer> {
    private final Repository<Faculty, Integer> repository;

    public FacultyService(Repository<Faculty, Integer> repository, Class<Faculty> faculty) {
        super(repository, faculty);
        this.repository = repository;
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