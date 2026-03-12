package ua.naukma.service;

import java.util.Optional;

public interface Service <T, ID extends Integer> {
    void add();
    void delete();
    T findById();
    void showAll();
}
