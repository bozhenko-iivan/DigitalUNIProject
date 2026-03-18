package ua.naukma.service;

import ua.naukma.domain.Department;
import ua.naukma.domain.SystemUser;
import ua.naukma.domain.SystemUserRoles;
import ua.naukma.domain.University;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.exception.IncorrectDataException;
import ua.naukma.repository.Repository;
import ua.naukma.repository.UserRepository;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.InitScanner;
import ua.naukma.utils.SystemUserVerificator;

import java.util.Optional;
import java.util.Scanner;

public class UserService implements Service<SystemUser, Integer> {
    private final Repository<SystemUser, Integer> repository;

    public void initUser() {
        if (repository.findAll().isEmpty()) {
            SystemUser user = new SystemUser(1111111, "admin", "Adm!1234", SystemUserRoles.ADMIN);
            repository.save(user);
        }
    }

    public SystemUser login() {
        System.out.println("Login user");
//        Scanner scanner = InitScanner.try_init_scanner();
//        scanner.nextLine();
        while (true) {
            String login = SystemUserVerificator.askLogin();
            String password = SystemUserVerificator.askPassword();
            Optional<SystemUser> foundUser = repository.findAll().stream().filter(user
                    -> user.getLogin().equals(login) &&
                    user.getPassword().equals(password)).findFirst();
            if (foundUser.isPresent()) {
                System.out.println("Login successful" + foundUser.get().getLogin());
                return foundUser.get();
            } else {
                System.out.println("Invalid login or password. Please try again.");
            }
        }
    }

    public UserService(Repository<SystemUser, Integer> repository) {
        this.repository = repository;
    }

    @Override
    public void add() {
        while (true) {
            try {
                SystemUser newUser = systemUser_validate_all();
                tryAddUser(newUser);
                return;
            } catch (DuplicateEntityException e) {
                System.out.println(e.getMessage() + " Please try again with different data.");
            }
        }
    }

    public void tryAddUser(SystemUser user) throws DuplicateEntityException {
        Optional<SystemUser> optionalUser = repository.findById(user.getId());
        if (optionalUser.isPresent()) {
            throw new DuplicateEntityException("System user with id " + user.getId() + " already exists.");
        }
        repository.save(user);
        System.out.println("System user with id " + user.getId() + " successfully added.");
    }

    @Override
    public void delete() {
        int id = IdVerificator.ask_id();
        try {
            repository.deleteById(id);
            System.out.println("User has been successfully deleted");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SystemUser findById() {
        int id = IdVerificator.ask_id();
        Optional<SystemUser> user = repository.findById(id);
        if (user.isPresent()) {
            System.out.println(user.get());
            return user.get();
        }  else {
            System.out.println("User with id " + id + " not found");
            return null;
        }
    }

    @Override
    public void showAll() {
        repository.findAll().forEach(System.out::println);
    }

    private SystemUser systemUser_validate_all() {
        int id = IdVerificator.ask_id();
        Optional<SystemUser> user = repository.findById(id);
        if (user.isPresent()) {
            throw new DuplicateEntityException("System user with id " + id + " already exists.");
        }
        String login = SystemUserVerificator.askLogin();
        String password = SystemUserVerificator.askPassword();
        SystemUserRoles role = SystemUserVerificator.askRole();
        return new SystemUser(id, login, password, role);
    }
}
