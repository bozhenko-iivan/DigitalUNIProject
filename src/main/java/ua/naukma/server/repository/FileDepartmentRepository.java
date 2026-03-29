package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ua.naukma.domain.Department;
import ua.naukma.domain.Faculty;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileDepartmentRepository implements Repository<Department, Integer> {
    private final Path filePath = Path.of("data/department.json");
    private final Gson gson = JsonAdapter.getCustomGson();

    @SuppressWarnings("unchecked")
    private List<Department> loadDepartment() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<Department>>(){}.getType();
            List<Department> departments = gson.fromJson(reader, listType);
            return departments != null ? departments : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Department> writeDepartment(List<Department> departments) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(departments, writer);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return departments;
    }

    @Override
    public void save(Department dep) {
        List<Department> currentDepartments = new ArrayList<>();
        try {
            currentDepartments = loadDepartment();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        currentDepartments.removeIf(d -> d.getId() == dep.getId());
        currentDepartments.add(dep);
        try {
            writeDepartment(currentDepartments);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Department> findById(Integer id) {
        try {
            List<Department> departments = loadDepartment();
            return departments.stream().filter(d -> d.getId() == id).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Department> findAll() {
        try {
            return loadDepartment();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<Department> departments;
        try {
            departments = loadDepartment();
            departments.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        List<Department> currDepartments;
        try {
            currDepartments = loadDepartment();
            currDepartments.removeIf(d -> d.getId() == id);
            writeDepartment(currDepartments);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}