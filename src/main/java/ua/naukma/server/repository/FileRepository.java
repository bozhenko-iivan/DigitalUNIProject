package ua.naukma.server.repository;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import ua.naukma.server.service.util.JsonAdapter;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileRepository <T extends Serializable & GetId, ID extends Number> implements Repository<T, ID>, IOWork<T> {
    private final Class<T> clazz;
    public FileRepository(Class<T> clazz) {
        this.clazz = clazz;
    }
    private final Path filePath = Path.of("data/" + getClassName() +".json");
    private final Gson gson = JsonAdapter.getCustomGson();
    private static final String errorReadingFileMsg = "Error reading file: ";
    private static final String errorWritingFileMsg = "Error writing file: ";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileRepository.class);

    private String getClassName(){
        String className;
        if (clazz == null){
            className = "unknown_class";
        }
        else className = clazz.getSimpleName();
        return className.toLowerCase();
    }
    @Override
    public List<T> load() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(filePath)) {
            Type listType = new TypeToken<List<T>>(){}.getType();
            List<T> entities = gson.fromJson(reader, listType);
            return entities != null ? entities : new ArrayList<>();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
            return new ArrayList<>();
        }
    }
    @Override
    public List<T> write(List<T> list) throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(list, writer);
            }
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return list;
    }
    @Override
    public void save(T t) {
        List<T> current = new ArrayList<>();
        try {
            current = load();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        current.removeIf(d -> d.getId() == t.getId());
        current.add(t);
        try {
            write(current);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            List<T> entities = load();
            return entities.stream().filter(d -> d.getId() == id.intValue()).findFirst();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return load();
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
        return new ArrayList<>();
    }

    @Override
    public void showAll() {
        List<T> entities;
        try {
            entities = load();
            entities.forEach(System.out::println);
        } catch (IOException e) {
            log.info(errorReadingFileMsg, e);
        }
    }

    @Override
    public void deleteById(ID id) {
        List<T> current;
        try {
            current = load();
            current.removeIf(d -> d.getId() == id.intValue());
            write(current);
        } catch (IOException e) {
            log.info(errorWritingFileMsg, e);
        }
    }
}
