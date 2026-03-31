package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import ua.naukma.domain.SystemUser;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileUserRepository implements Repository<SystemUser, Integer> {
    private final Path filePath = Path.of("data/users.json");
    private final Gson gson = JsonAdapter.getCustomGson();
    private static final String errorReadingFileMsg = "Error reading file: ";
    private static final String errorWritingFileMsg = "Error writing file: ";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileUserRepository.class);

    private List<SystemUser> loadUsers() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<SystemUser>>(){}.getType();
            List<SystemUser> systemUsers = gson.fromJson(reader, listType);
            return systemUsers != null ? systemUsers : new ArrayList<>();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
            return new ArrayList<>();
        }
    }

    private List<SystemUser> writeUsers(List<SystemUser> users) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(users, writer);
            }
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return users;
    }

    @Override
    public void save(SystemUser user) {
        List<SystemUser> currentUsers;
        try {
            currentUsers = loadUsers();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        currentUsers.removeIf(us -> us.getId() == user.getId());
        currentUsers.add(user);
        try {
            writeUsers(currentUsers);
        } catch (IOException e) {
            throw  new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<SystemUser> findById(Integer id) {
        try {
            List<SystemUser> users = loadUsers();
            return users.stream().filter(u -> u.getId() == id).findFirst();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<SystemUser> findAll() {
        try {
            return loadUsers();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<SystemUser> users;
        try {
            users = loadUsers();
            users.forEach(System.out::println);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        List<SystemUser> users;
        try {
            users = loadUsers();
            users.removeIf(u -> u.getId() == id);
            writeUsers(users);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }
}