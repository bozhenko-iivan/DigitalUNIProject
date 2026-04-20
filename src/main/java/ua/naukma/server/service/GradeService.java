package ua.naukma.server.service;

import ua.naukma.domain.Grade;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;
import java.util.List;

@ua.naukma.server.annotation.Service
public class GradeService extends EntityService<Grade, Integer> {
    private final Repository<Grade, Integer> repository;

    public GradeService(Repository<Grade, Integer> repository, Class<Grade> entityClass) {
        super(repository, entityClass);
        this.repository = repository;
    }
    public List<Grade> findByStudentId(Integer studentId) {
        return repository.findAll().stream().filter(grade -> grade.getStudentId() == studentId).toList();
    }

    public int generateID() {
        return repository.findAll().stream().mapToInt(Grade::getId).max().orElse(-1) + 1;
    }
}
