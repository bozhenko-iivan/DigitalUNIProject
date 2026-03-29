package ua.naukma.server.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.SystemUser;
import ua.naukma.domain.University;
import ua.naukma.server.service.UserService;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileUserRepository implements Repository<SystemUser, Integer> {
    private final Path filePath = Path.of("data/users.json");
    private final Gson gson = JsonAdapter.getCustomGson();

    @SuppressWarnings("unchecked")
    private List<SystemUser> loadUsers() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<SystemUser>>(){}.getType();
            List<SystemUser> systemUsers = gson.fromJson(reader, listType);
            return systemUsers != null ? systemUsers : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
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
            System.out.println("Error writing file: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void save(SystemUser user) {
        List<SystemUser> currentUsers;
        try {
            currentUsers = loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentUsers.add(user);
        try {
            writeUsers(currentUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SystemUser> findById(Integer id) {
        try {
            List<SystemUser> users = loadUsers();
            return users.stream().filter(u -> u.getId() == id).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SystemUser> findAll() {
        try {
            return loadUsers();
        } catch (IOException e) {
            System.out.println("Error reading users file" + e.getMessage());
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
            System.out.println("Error reading users file" + e.getMessage());
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
            System.out.println("Error reading users file" + e.getMessage());
        }
    }
}