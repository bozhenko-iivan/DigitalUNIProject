package ua.naukma.server.repository;

import java.util.Optional;

public interface PersonRepository<T, ID> extends Repository<T, ID> {
    Optional<T> findByPIB(String firstName, String lastName, String middleName);
}
