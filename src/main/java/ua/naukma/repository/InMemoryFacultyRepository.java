package ua.naukma.repository;

import ua.naukma.domain.Faculty;
import ua.naukma.domain.Student;
import ua.naukma.domain.Teacher;

import java.util.*;

public class InMemoryFacultyRepository implements Repository<Faculty, Integer> {
    private Map<Integer, Faculty> storage = new HashMap<>();

    @Override
    public void save(Faculty f) {
        storage.put(f.getId(), f);
        System.out.println("Faculty saved: " + f.getName());
    }

    @Override
    public Optional<Faculty> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void showAll(){
        for (Faculty f : storage.values()) {
            System.out.println(f.getName());
        }
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Faculty deleted: " + id);
    }
    @Override
    public List<Faculty> findAll() {
        return new ArrayList<>(storage.values());
    }
}
