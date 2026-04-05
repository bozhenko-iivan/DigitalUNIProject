package ua.naukma.server.service;

import ua.naukma.domain.SystemUser;
import ua.naukma.domain.SystemUserRoles;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Service
public class UserService extends EntityService<SystemUser, Integer> {
    private final Repository<SystemUser, Integer> repository;
    public UserService(Repository<SystemUser, Integer> repository, Class<SystemUser> entityClass) {
        super(repository, entityClass);
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
    public int getUserCount() {
        return repository.findAll().size();
    }
}