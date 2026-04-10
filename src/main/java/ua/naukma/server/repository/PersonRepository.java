package ua.naukma.server.repository;

import java.util.Optional;

@ua.naukma.server.annotation.Repository
public interface PersonRepository<T, ID extends Number> extends Repository<T, ID> {
    Optional<T> findByPIB(String firstName, String lastName, String middleName);
}
