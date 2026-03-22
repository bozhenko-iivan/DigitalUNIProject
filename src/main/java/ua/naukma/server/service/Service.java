package ua.naukma.server.service;

import ua.naukma.domain.Student;

import java.util.List;

public interface Service <T, ID extends Integer> {
    void add();
    void delete();
    T findById();
    void showAll();
}
