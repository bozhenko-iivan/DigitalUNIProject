package ua.naukma.server.service;

import ua.naukma.exception.EntityNotFoundException;

import java.util.List;

public interface Service <T, ID extends Integer> {
    void add(T entity);
    void deleteById(ID id);
    T findById(ID id);
    List<T> findAll();
}
