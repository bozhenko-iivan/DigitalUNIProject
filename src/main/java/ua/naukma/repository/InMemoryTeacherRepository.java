package ua.naukma.repository;

import ua.naukma.domain.Teacher;

import java.util.*;

public class InMemoryTeacherRepository implements PersonRepository<Teacher, Integer> {
    private final Map<Integer, Teacher> storage = new HashMap<>();

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
    public Optional<Teacher> findByPIB(String firstName, String lastName, String middleName){
        for (Teacher t : storage.values()) {
            if (t.getFirstName().equals(firstName) &&
                    t.getLastName().equals(lastName) &&
                    t.getMiddleName().equals(middleName)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Teacher deleted: " + id);
    }
}
