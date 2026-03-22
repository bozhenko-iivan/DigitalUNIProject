package ua.naukma.server.repository;

import ua.naukma.domain.Student;

import java.io.Serializable;
import java.util.*;

public class InMemoryStudentRepository implements PersonRepository<Student, Integer>, Serializable {
    final private Map<Integer, Student> storage = new HashMap<>();

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
        storage.values().forEach(s -> System.out.println(s.toStringShort()));
    }
    @Override
    public Optional<Student> findByPIB(String firstName, String lastName, String middleName) {
//        for (Student s : storage.values()) {
//            if (s.getFirstName().equals(firstName) &&
//                    s.getLastName().equals(lastName) &&
//                    s.getMiddleName().equals(middleName)) {
//                return Optional.of(s);
//            }
//        }
//        return Optional.empty();
        return storage.values().stream().filter(s -> s.getMiddleName().equals(middleName)).findFirst();
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Student deleted: " + id);
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(storage.values());
    }
}