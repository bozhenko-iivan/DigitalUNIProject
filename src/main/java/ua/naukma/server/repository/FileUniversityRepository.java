package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import ua.naukma.domain.University;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileUniversityRepository implements Repository<University, Integer> {
    private final Path filePath = Path.of("data/university.json");
    private final Gson gson = JsonAdapter.getCustomGson();
    private static final String errorReadingFileMsg = "Error reading file: ";
    private static final String errorWritingFileMsg = "Error writing file: ";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileUniversityRepository.class);

    private List<University> loadUniversity() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<University>>(){}.getType();
            List<University> universities = gson.fromJson(reader, listType);
            return universities != null ? universities : new ArrayList<>();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
            return new ArrayList<>();
        }
    }

    private List<University> writeUniversity(List<University> universities) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(universities, writer);
            }
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return universities;
    }

    @Override
    public void save(University uni) {
        List<University> currentUniversities;
        try {
            currentUniversities = loadUniversity();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error loading universities", e);
        }
        currentUniversities.removeIf(u -> u.getId() == uni.getId());
        currentUniversities.add(uni);
        try {
            writeUniversity(currentUniversities);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public Optional<University> findById(Integer id) {
        try {
            List<University> currUni = loadUniversity();
            return currUni.stream().filter(u -> u.getId() == id).findFirst();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<University> findAll() {
        try {
            return loadUniversity();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
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
            throw new IllegalArgumentException(e);
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
            log.info(errorWritingFileMsg, e);
        }
    }
}
