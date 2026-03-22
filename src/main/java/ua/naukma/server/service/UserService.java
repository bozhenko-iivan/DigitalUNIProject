package ua.naukma.server.service;

import ua.naukma.domain.SystemUser;
import ua.naukma.domain.SystemUserRoles;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final Repository<SystemUser, Integer> repository;

    public UserService(Repository<SystemUser, Integer> repository) {
        this.repository = repository;
    }

    public void initUser() {
        if (repository.findAll().isEmpty()) {
            SystemUser user = new SystemUser(1111111, "admin", "Adm!1234", SystemUserRoles.ADMIN);
            repository.save(user);
        }
    }

    public SystemUser authenticate(SystemUser credentials) throws EntityNotFoundException {
        String inputLogin = credentials.getLogin();
        String inputPassword = credentials.getPassword();

        Optional<SystemUser> foundUser = repository.findAll().stream()
                .filter(user -> user.getLogin().equals(inputLogin) && user.getPassword().equals(inputPassword))
                .findFirst();

        if (foundUser.isEmpty()) {
            throw new EntityNotFoundException("Invalid login or password");
        }
        return foundUser.get();
    }

    public void addUser(SystemUser user) throws DuplicateEntityException {
        Optional<SystemUser> optionalUser = repository.findById(user.getId());
        if (optionalUser.isPresent()) {
            throw new DuplicateEntityException("System user with id " + user.getId() + " already exists.");
        }
        repository.save(user);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("User with id " + id + " doesn't exist.");
        }
        repository.deleteById(id);
    }

    public SystemUser findById(int id) throws EntityNotFoundException {
        Optional<SystemUser> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public List<SystemUser> getAllUsers() {
        return repository.findAll();
    }
}