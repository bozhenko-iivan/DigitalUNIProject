package ua.naukma.repository;

import ua.naukma.domain.Department;
import ua.naukma.domain.Teacher;
import ua.naukma.domain.University;
import ua.naukma.exception.DuplicateEntityException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryUniversityRepository implements Repository<University, Integer> {
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
        return List.of();
    }

    @Override
    public void showAll() {
        for (University u : storage.values()) {
            System.out.println(u);
        }
    }

    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("University with id " + id + " has been deleted");
    }
}
