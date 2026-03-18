package ua.naukma.repository;

import ua.naukma.domain.SystemUser;
import ua.naukma.domain.University;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUniversityRepository implements Repository<University, Integer> {
    private final Path filePath = Path.of("data/university.dat");

    private List<University> loadUniversity() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            ObjectInputStream ios = new ObjectInputStream(Files.newInputStream(filePath));
            return (List<University>) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<University> writeUniversity(List<University> university) throws IOException {
        try {
            if (filePath.getParent() != null || !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(university);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return university;
    }

    @Override
    public void save(University uni) {
        List<University> currentUniversities;
        try {
            currentUniversities = loadUniversity();
        } catch (IOException e) {
            throw new RuntimeException("Error loading universities", e);
        }
        currentUniversities.removeIf(u -> u.getId() == uni.getId());
        currentUniversities.add(uni);
        try {
            writeUniversity(currentUniversities);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<University> findById(Integer id) {
        try {
            List<University> currUni = loadUniversity();
            return currUni.stream().filter(u -> u.getId() == id).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<University> findAll() {
        try {
            return loadUniversity();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<University> currUni;
        try {
            currUni = loadUniversity();
            currUni.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer integer) {
        List<University> currUni;
        try {
            currUni = loadUniversity();
            currUni.removeIf(u -> u.getId() == integer);
            writeUniversity(currUni);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
