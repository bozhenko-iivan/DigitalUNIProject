package ua.naukma.server.service;

import ua.naukma.domain.Grade;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;
import java.util.List;

@ua.naukma.server.annotation.Service
public class GradeService implements Service<Grade, Integer> {
    private final Repository<Grade, Integer> repository;

    public GradeService(Repository<Grade, Integer> repository) {
        this.repository = repository;
    }

    @Override
    public void add(Grade grade) {
        repository.save(grade);
    }

    @Override
    public void deleteById(Integer id) {
        repository.findById(id).ifPresentOrElse(
                (ignored) -> repository.deleteById(id),
                () -> {throw new EntityNotFoundException("Grade with id " + id + " not found!");}
        );
    }

    @Override
    public Grade findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Grade with id " + id + " not found!"));
    }

    @Override
    public List<Grade> findAll() {
        return repository.findAll();
    }

    public List<Grade> findByStudentId(Integer studentId) {
        return repository.findAll().stream().filter(grade -> grade.getStudentId() == studentId).toList();
    }
}
