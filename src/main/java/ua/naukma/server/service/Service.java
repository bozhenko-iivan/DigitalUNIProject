package ua.naukma.server.service;
import java.util.List;

@ua.naukma.server.annotation.Service
public interface Service <T, Integer> {
    void add(T entity);
    void deleteById(Integer id);
    T findById(Integer id);
    List<T> findAll();
    List<T> sortByIds();
    List<T> sortByName();
}
