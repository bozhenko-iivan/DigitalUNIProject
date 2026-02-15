package ua.naukma.repository;

import ua.naukma.domain.Teacher;

import java.util.*;

public class InMemoryTeacherRepository implements Repository<Teacher, Integer> {
    private Map<Integer, Teacher> storage = new HashMap<>();

    @Override
    public void save(Teacher t) {
        storage.put(t.getId(), t);
        System.out.println("Teacher saved: " + t.getLastName());
    }

    @Override
    public Optional<Teacher> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void showAll(){
        for (Teacher t : storage.values()) {
            System.out.println(t);
        }
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Teacher deleted: " + id);
    }
}
