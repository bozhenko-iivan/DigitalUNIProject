package ua.naukma.server.repository;

import ua.naukma.domain.University;
import ua.naukma.exception.DuplicateEntityException;

import java.io.Serializable;
import java.util.*;

public class InMemoryUniversityRepository implements Repository<University, Integer>, Serializable {
    private Map<Integer, University> storage = new HashMap<>();
    @Override
    public void save(University u) {
        if (storage.containsKey(u.getId())) {
            throw new DuplicateEntityException("University with id " + u.getId() + " already exists");
        }
        storage.put(u.getId(), u);
        System.out.println("University with id " + u.getId() + " has been saved");
    }

    @Override
    public Optional<University> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<University> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void showAll() {
        storage.values().forEach(System.out::println);
    }

    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("University with id " + id + " has been deleted");
    }
}
