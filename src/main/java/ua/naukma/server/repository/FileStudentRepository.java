package ua.naukma.server.repository;

import ua.naukma.domain.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileStudentRepository implements PersonRepository<Student, Integer> {
    private final Path filePath = Path.of("data/student.dat");

    private List<Student> loadStudent() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            ObjectInputStream ios = new ObjectInputStream(Files.newInputStream(filePath));
            return (List<Student>) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Student> writeStudent(List<Student> students) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(students);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return students;
    }

    @Override
    public void save(Student student) {
        List<Student> currentStudents = new ArrayList<>();
        try {
            currentStudents = loadStudent();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        currentStudents.removeIf(s -> s.getId() == student.getId());
        currentStudents.add(student);
        try {
            writeStudent(currentStudents);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Student> findById(Integer id) {
        try {
            List<Student> students = loadStudent();
            return students.stream().filter(s -> s.getId() == id).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> findAll() {
        try {
            return loadStudent();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<Student> students;
        try {
            students = loadStudent();
            students.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        List<Student> currStudents;
        try {
            currStudents = loadStudent();
            currStudents.removeIf(s -> s.getId() == id);
            writeStudent(currStudents);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Student> findByPIB(String firstName, String lastName, String middleName) {
        return Optional.empty();
    }
}