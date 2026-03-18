package ua.naukma.repository;

import ua.naukma.domain.Student;
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
        storage.values().forEach(t -> System.out.println(t.toString()));
    }

    @Override
    public Optional<Teacher> findByPIB(String firstName, String lastName, String middleName){
        return storage.values().stream().filter(t -> t.getMiddleName().equals(middleName)).findFirst();
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Teacher deleted: " + id);
    }
    @Override
    public List<Teacher> findAll() {
        return new ArrayList<>(storage.values());
    }
}
