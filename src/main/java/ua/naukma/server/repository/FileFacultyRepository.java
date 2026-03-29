package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.University;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileFacultyRepository implements Repository<Faculty, Integer> {
    private final Path filePath = Path.of("data/faculty.json");
    private final Gson gson = JsonAdapter.getCustomGson();

    @SuppressWarnings("unchecked")
    private List<Faculty> loadFaculty() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<Faculty>>(){}.getType();
            List<Faculty> faculties = gson.fromJson(reader, listType);
            return faculties != null ? faculties : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Faculty> writeFaculty(List<Faculty> faculties) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(faculties, writer);
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
