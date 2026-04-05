package ua.naukma.server.service;

import ua.naukma.domain.Department;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.GetId;
import ua.naukma.server.repository.Repository;

import javax.management.ObjectInstance;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityService <T extends Serializable & GetId, ID extends Integer> implements Service<T, ID> {
    private final Repository<T, ID> repository;
    private final Class<T> clazz;
    public EntityService(Repository<T, ID> repository, Class<T> clazz) {
        this.clazz = clazz;
        this.repository = repository;
    }
    private String getClassName(){
        String className;
        if (clazz == null){
            className = "unknown_class";
        }
        else className = clazz.getSimpleName();
        return className.toLowerCase();
    }
    @Override
    public void add(T e) throws DuplicateEntityException {
        ID id = (ID)((Integer)e.getId());
        if (repository.findById(id).isPresent()) {
            throw new DuplicateEntityException( getClassName() +" with id " + e.getId() + " already exists.");
        }
        repository.save(e);
    }
    @Override
    public void deleteById(ID id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(getClassName() + " with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }
    @Override
    public T findById(ID id) throws EntityNotFoundException {
        Optional<T> entity = repository.findById(id);
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new EntityNotFoundException(getClassName() + " with id " + id + " doesn't exist.");
        }
    }
    @Override
    public List<T> findAll() {
        if  (repository.findAll().isEmpty()) {
            throw new EntityNotFoundException("No " + getClassName().toLowerCase() + " have been found!");
        }
        return repository.findAll();
    }
}
