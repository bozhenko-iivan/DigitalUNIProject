package ua.naukma.service;

import ua.naukma.domain.Student;
import ua.naukma.domain.University;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.repository.Repository;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.UniversityVerificator;

import java.util.List;
import java.util.Optional;

public class UniversityService implements Service<University, Integer> {
    private final Repository<University, Integer> repository;

    public UniversityService(Repository<University, Integer> repository) {
        this.repository = repository;
    }

    public void try_add_university(University entity) {
        try {
            repository.save(entity);
            System.out.println("University added successfully!");
        } catch (DuplicateEntityException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void add() {
        University newU = university_validate_all();
        try_add_university(newU);
    }

    @Override
    public void delete() {
        int id = IdVerificator.ask_id();
        try {
            repository.deleteById(id);
            System.out.println("University deleted successfully!");
        } catch (EntityNotFoundException e) {
            System.out.println("University not found!");
        }
    }

    @Override
    public University findById() {
        int id = IdVerificator.ask_id();
        System.out.println("Finding University with id " + id);
        Optional<University> u = repository.findById(id);
        if (u.isPresent()) {
            System.out.println(u.get());
            return u.get();
        } else {
            System.out.println("University with id " + id + " not found!");
            return null;
        }
    }

    @Override
    public void showAll() {
        System.out.println("All universities");
        repository.findAll().forEach(System.out::println);
    }

    private University university_validate_all() {
        int id = IdVerificator.ask_id();
        String fullName = UniversityVerificator.ask_full_name();
        String shortName = UniversityVerificator.ask_short_name();
        String city = UniversityVerificator.ask_city();
        String address = UniversityVerificator.ask_address();
        University u = new University(id, fullName, shortName, city, address);
        return u;
    }
}
