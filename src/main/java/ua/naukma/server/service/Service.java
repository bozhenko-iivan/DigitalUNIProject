package ua.naukma.server.service;
import java.util.List;

@ua.naukma.server.annotation.Service
public interface Service <T, ID extends Integer> {
    void add(T entity);
    void deleteById(ID id);
    T findById(ID id);
    List<T> findAll();
}
