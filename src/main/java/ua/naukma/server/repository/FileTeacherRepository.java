package ua.naukma.server.repository;

import ua.naukma.domain.Teacher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileTeacherRepository implements PersonRepository<Teacher, Integer> {
    private final Path filePath = Path.of("data/teacher.dat");

    private List<Teacher> loadTeacher() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            ObjectInputStream ios = new ObjectInputStream(Files.newInputStream(filePath));
            return (List<Teacher>) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Teacher> writeTeacher(List<Teacher> teachers) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(teachers);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return teachers;
    }

    @Override
    public void save(Teacher teacher) {
        List<Teacher> currentTeachers = new ArrayList<>();
        try {
            currentTeachers = loadTeacher();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        currentTeachers.removeIf(t -> t.getId() == teacher.getId());
        currentTeachers.add(teacher);
        try {
            writeTeacher(currentTeachers);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Teacher> findById(Integer id) {
        try {
            List<Teacher> teachers = loadTeacher();
            return teachers.stream().filter(t -> t.getId() == id).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Teacher> findAll() {
        try {
            return loadTeacher();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<Teacher> teachers;
        try {
            teachers = loadTeacher();
            teachers.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        List<Teacher> currTeachers;
        try {
            currTeachers = loadTeacher();
            currTeachers.removeIf(t -> t.getId() == id);
            writeTeacher(currTeachers);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Teacher> findByPIB(String firstName, String lastName, String middleName) {
        return Optional.empty();
    }
}