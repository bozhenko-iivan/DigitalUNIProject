package ua.naukma.repository;

import ua.naukma.domain.Student;
import java.util.*;

public class InMemoryStudentRepository implements Repository<Student, Integer> {
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
    public void findByPIB(String firstName, String lastName, String middleName) {
        for (Student s : storage.values()) {
            if (s.getFirstName().equals(firstName) &&
                    s.getLastName().equals(lastName) &&
                    s.getMiddleName().equals(middleName)) {
                System.out.println(s.toString());
            }
        }
        System.out.println("Student with such PIB is not found");
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Student deleted: " + id);
    }
}