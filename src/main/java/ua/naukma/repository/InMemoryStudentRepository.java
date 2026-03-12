package ua.naukma.repository;

import ua.naukma.domain.Student;
import java.util.*;

public class InMemoryStudentRepository implements PersonRepository<Student, Integer> {
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
        for (Student s : storage.values()) {
            System.out.println(s.toString());
        }
    }
    @Override
    public Optional<Student> findByPIB(String firstName, String lastName, String middleName) {
        for (Student s : storage.values()) {
            if (s.getFirstName().equals(firstName) &&
                    s.getLastName().equals(lastName) &&
                    s.getMiddleName().equals(middleName)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
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