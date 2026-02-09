package ua.naukma.repository;

import ua.naukma.domain.Student;
import java.util.*;

public class InMemoryStudentRepository implements Repository<Student, Integer> {
    private Map<Integer, Student> storage = new HashMap<>();

    @Override
    public void save(Student student) {
        storage.put(student.getId(), student);
        System.out.println("Student saved: " + student.getLastName());
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }
    @Override
    public void showAll(){
        for (Student s : storage.values()) {
            System.out.println(s);
        }
    }

    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Student deleted: " + id);
    }
}