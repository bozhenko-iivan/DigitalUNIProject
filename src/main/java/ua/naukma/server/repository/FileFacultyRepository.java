package ua.naukma.server.repository;

import ua.naukma.domain.Faculty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileFacultyRepository implements Repository<Faculty, Integer> {
    private final Path filePath = Path.of("data/faculty.dat");

    private List<Faculty> loadFaculty() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            ObjectInputStream ios = new ObjectInputStream(Files.newInputStream(filePath));
            return (List<Faculty>) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Faculty> writeFaculty(List<Faculty> faculties) throws IOException {
        try {
            if (filePath.getParent() != null || !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(faculties);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return faculties;
    }

    @Override
    public void save(Faculty fac) {
        List<Faculty> currentFaculties = new ArrayList<>();
        try {
            currentFaculties = loadFaculty();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        currentFaculties.removeIf(f -> f.getId() ==  fac.getId());
        currentFaculties.add(fac);
        try {
            writeFaculty(currentFaculties);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Faculty> findById(Integer id) {
        try {
            List<Faculty> faculties = loadFaculty();
            return faculties.stream().filter(f -> f.getId() == id).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Faculty> findAll() {
        try {
            return loadFaculty();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<Faculty> faculties;
        try {
            faculties = loadFaculty();
            faculties.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer integer) {
        List<Faculty> currFaculties;
        try {
            currFaculties = loadFaculty();
            currFaculties.removeIf(f -> f.getId() == integer);
            writeFaculty(currFaculties);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
