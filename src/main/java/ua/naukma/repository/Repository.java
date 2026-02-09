package ua.naukma.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    void showAll();
    void deleteById(ID id);
}
