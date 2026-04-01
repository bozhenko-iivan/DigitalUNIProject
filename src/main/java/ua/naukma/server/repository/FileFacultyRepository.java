package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ua.naukma.domain.Faculty;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;

@ua.naukma.server.annotation.Repository
public class FileFacultyRepository implements Repository<Faculty, Integer> {
    private final Path filePath = Path.of("data/faculty.json");
    private final Gson gson = JsonAdapter.getCustomGson();
    private static final String errorReadingFileMsg = "Error reading file: ";
    private static final String errorWritingFileMsg = "Error writing file: ";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileFacultyRepository.class);

    private List<Faculty> loadFaculty() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<Faculty>>(){}.getType();
            List<Faculty> faculties = gson.fromJson(reader, listType);
            return faculties != null ? faculties : new ArrayList<>();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
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
            log.info(errorReadingFileMsg, e);
        }
        return faculties;
    }

    @Override
    public void save(Faculty fac) {
        List<Faculty> currentFaculties = new ArrayList<>();
        try {
            currentFaculties = loadFaculty();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        currentFaculties.removeIf(f -> f.getId() ==  fac.getId());
        currentFaculties.add(fac);
        try {
            writeFaculty(currentFaculties);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public Optional<Faculty> findById(Integer id) {
        try {
            List<Faculty> faculties = loadFaculty();
            return faculties.stream().filter(f -> f.getId() == id).findFirst();
        } catch (IOException e) {
            throw new IllegalArgumentException(errorReadingFileMsg + e.getMessage());
        }
    }

    @Override
    public List<Faculty> findAll() {
        try {
            return loadFaculty();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
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
            log.info(errorReadingFileMsg, e);
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
            log.info(errorWritingFileMsg, e);
        }
    }
}
