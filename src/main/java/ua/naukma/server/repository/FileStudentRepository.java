package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.Student;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileStudentRepository implements PersonRepository<Student, Integer> {
    private final Path filePath = Path.of("data/student.json");
    private final Gson gson = JsonAdapter.getCustomGson();

    @SuppressWarnings("unchecked")
    private List<Student> loadStudent() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<Student>>(){}.getType();
            List<Student> students = gson.fromJson(reader, listType);
            return students != null ? students : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Student> writeStudent(List<Student> students) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(students, writer);
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