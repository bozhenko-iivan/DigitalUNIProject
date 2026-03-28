package ua.naukma.server.repository;

import ua.naukma.domain.Group;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public class FileGroupRepository implements Repository<Group, Integer> {
    private final Path filePath = Path.of("data/group.json");

    @SuppressWarnings("unchecked")
    private List<Group> loadGroup() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            ObjectInputStream ios = new ObjectInputStream(Files.newInputStream(filePath));
            return (List<Group>) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Group> writeGroup(List<Group> groups) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(groups);
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        return groups;
    }

    @Override
    public void save(Group group) {
        List<Group> currentGroups = new ArrayList<>();
        try {
            currentGroups = loadGroup();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        currentGroups.removeIf(g -> g.getId() == group.getId());
        currentGroups.add(group);
        try {
            writeGroup(currentGroups);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    @Override
    public Optional<Group> findById(Integer id) {
        try {
            List<Group> groups = loadGroup();
            return groups.stream().filter(g -> g.getId() == id).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> findAll() {
        try {
            return loadGroup();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
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
            System.out.println("Error reading file: " + e.getMessage());
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
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}