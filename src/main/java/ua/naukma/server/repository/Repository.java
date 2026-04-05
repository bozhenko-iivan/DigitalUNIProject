package ua.naukma.server.repository;

import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Repository
public interface Repository<T, ID extends Number> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void showAll();
    void deleteById(ID id);
}
