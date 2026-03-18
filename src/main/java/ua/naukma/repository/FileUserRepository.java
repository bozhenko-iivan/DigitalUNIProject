package ua.naukma.repository;

import ua.naukma.domain.SystemUser;
import ua.naukma.service.UserService;
import ua.naukma.utils.IdVerificator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserRepository implements Repository<SystemUser, Integer> {
    private final Path filePath = Path.of("data/users.dat");

    private List<SystemUser> loadUsers() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ios = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (List<SystemUser>) ios.readObject();
        } catch (IOException  | ClassNotFoundException e) {
            System.out.println("Error reading users file " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<SystemUser> writeUsers(List<SystemUser> users) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(users);
            }
        } catch (IOException e) {
            System.out.println("Error writing users file: " + e.getMessage());
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