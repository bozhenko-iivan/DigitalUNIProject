package ua.naukma.repository;

import ua.naukma.domain.SystemUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<SystemUser, Integer> {
    private final List<SystemUser> users = new ArrayList<>();

    @Override
    public void save(SystemUser user) {
        users.add(user);
    }

    @Override
    public Optional<SystemUser> findById(Integer integer) {
        return users.stream().filter(u -> u.getId() == integer).findFirst();
    }

    @Override
    public List<SystemUser> findAll() {
        return users;
    }

    @Override
    public void showAll() {
        users.forEach(System.out::println);
    }

    public Optional<SystemUser> findByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public void deleteById(Integer integer) {
        users.removeIf(u -> u.getId() == integer);
    }
}
