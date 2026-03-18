package ua.naukma.repository;

import ua.naukma.domain.Faculty;
import ua.naukma.domain.Group;

import java.util.*;

public class InMemoryGroupRepository implements Repository<Group, Integer> {
    private Map<Integer, Group> storage = new HashMap<>();
    @Override
    public void save(Group gr) {
        storage.put(gr.getId(), gr);
    }

    @Override
    public Optional<Group> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }


    public Optional<Group> findByName(String name) {
        return storage.values().stream().filter(g -> g.getName().equals(name)).findFirst();
    }

    @Override
    public List<Group> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void showAll() {
        storage.values().forEach(System.out::println);
    }

    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Deleted group with id: " + id);
    }
}
