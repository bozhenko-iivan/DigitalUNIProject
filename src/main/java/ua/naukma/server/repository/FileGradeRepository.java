package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import ua.naukma.domain.Grade;
import ua.naukma.domain.Group;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileGradeRepository implements Repository<Grade, Integer> {
    private final Path filePath = Path.of("data/grade.json");
    private final Gson gson = JsonAdapter.getCustomGson();
    private static final String errorReadingFileMsg = "Error reading file: ";
    private static final String errorWritingFileMsg = "Error writing file: ";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileGradeRepository.class);

    private List<Grade> loadGrade() throws IOException {
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
        try (Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<Grade>>() {}.getType();
            List<Grade> grades = gson.fromJson(reader, listType);
            return grades != null ? grades : new ArrayList<>();
        }
        catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Grade> writeGrade(List<Grade> grades) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(grades, writer);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return grades;
    }

    @Override
    public void save(Grade grade) {
        List<Grade> grades = new ArrayList<>();
        try {
            grades = loadGrade();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        grades.removeIf(currGr -> currGr.getGradeId() == grade.getGradeId());
        if (grade.getGradeId() == 0) {
            int nextId = generateNextGradeID(grades);
            grade.setGradeId(nextId);
        }
        grades.add(grade);
        try {
            writeGrade(grades);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public Optional<Grade> findById(Integer integer) {
        try {
            List<Grade> grades = loadGrade();
            return grades.stream().filter(currGr -> currGr.getGradeId() == integer).findFirst();
        }
        catch (IOException e) {
            log.info(errorReadingFileMsg, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Grade> findAll() {
        try {
            return loadGrade();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<Grade> grades;
        try {
            grades = loadGrade();
            grades.forEach(System.out::println);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public void deleteById(Integer integer) {
        List<Grade> grades;
        try {
            grades = loadGrade();
            grades.removeIf(currGr -> currGr.getGradeId() == integer);
            writeGrade(grades);
        } catch (IOException e) {
            log.info(errorWritingFileMsg, e);
        }
    }

    private int generateNextGradeID(List<Grade> grades) {
        return grades.stream().mapToInt(Grade::getGradeId).max().orElse(0) + 1;
    }
}
