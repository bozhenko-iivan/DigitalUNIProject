package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import ua.naukma.domain.Group;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileGroupRepository implements Repository<Group, Integer> {
    private final Path filePath = Path.of("data/group.json");
    private final Gson gson = JsonAdapter.getCustomGson();
    private static final String errorReadingFileMsg = "Error reading file: ";
    private static final String errorWritingFileMsg = "Error writing file: ";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileGroupRepository.class);

    private List<Group> loadGroup() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<Group>>(){}.getType();
            List<Group> groups = gson.fromJson(reader, listType);
            return groups != null ? groups : new ArrayList<>();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
            return new ArrayList<>();
        }
    }

    private List<Group> writeGroup(List<Group> groups) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(groups, writer);
            }
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return groups;
    }

    @Override
    public void save(Group group) {
        List<Group> currentGroups = new ArrayList<>();
        try {
            currentGroups = loadGroup();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        currentGroups.removeIf(g -> g.getId() == group.getId());
        currentGroups.add(group);
        try {
            writeGroup(currentGroups);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public Optional<Group> findById(Integer id) {
        try {
            List<Group> groups = loadGroup();
            return groups.stream().filter(g -> g.getId() == id).findFirst();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Group> findAll() {
        try {
            return loadGroup();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<Group> groups;
        try {
            groups = loadGroup();
            groups.forEach(System.out::println);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        List<Group> currGroups;
        try {
            currGroups = loadGroup();
            currGroups.removeIf(g -> g.getId() == id);
            writeGroup(currGroups);
        } catch (IOException e) {
            log.info(errorWritingFileMsg, e);
        }
    }
}