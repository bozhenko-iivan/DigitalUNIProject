package ua.naukma.server.repository;

import ua.naukma.domain.Department;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileDepartmentRepository implements Repository<Department, Integer> {
    private final Path filePath = Path.of("data/department.json");

    @SuppressWarnings("unchecked")
    private List<Department> loadDepartment() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            ObjectInputStream ios = new ObjectInputStream(Files.newInputStream(filePath));
            return (List<Department>) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Department> writeDepartment(List<Department> departments) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(departments);
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