package ua.naukma.repository;

import java.util.Optional;

public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void showAll();
    void deleteById(ID id);
}
