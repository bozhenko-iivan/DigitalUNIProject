package ua.naukma.server.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.GetId;
import ua.naukma.server.repository.GetName;
import ua.naukma.server.repository.Repository;


import java.io.Serializable;
import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


public class EntityService <T extends Serializable & GetId & GetName, Number> implements Service<T, Integer> {
    private static final Logger log = LoggerFactory.getLogger(EntityService.class);
    protected final Repository<T, Integer> repository;
    private final Class<T> clazz;
    public EntityService(Repository<T, Integer> repository, Class<T> clazz) {
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
        int newUniqueId = generateNextId();
        try {
            e.getClass().getMethod("setId", int.class).invoke(e, newUniqueId);
        } catch (Exception ex) {
            EntityService.log.error("Could not automatically set ID for " + getClassName() + ". Make sure it has a setId(int) method.");
        }
        repository.save(e);
    }
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(getClassName() + " with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }
    @Override
    public T findById(Integer id) throws EntityNotFoundException {
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

    @Override
    public List<T> sortByIds() {
        return findAll().stream()
                .sorted(java.util.Comparator.comparing(GetId::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<T> sortByName() {
        Collator ukrainianCollator = Collator.getInstance(new Locale("uk", "UA"));

        return findAll().stream()
                .sorted((e1, e2) -> ukrainianCollator.compare(e1.getName(), e2.getName()))
                .collect(Collectors.toList());
    }

    private int generateNextId() {
        return repository.findAll().stream()
                .mapToInt(GetId::getId)
                .max()
                .orElse(1000000) + 1;
    }
}
