package ua.naukma.server.repository;

import ua.naukma.domain.Student;

import java.io.Serializable;
import java.util.Optional;

public class FilePersonRepository<T extends Serializable & GetId, ID extends Number> extends FileRepository<T, ID> implements PersonRepository<T, ID> {
    public FilePersonRepository(Class<T> clazz) {
        super(clazz);
    }
    /*To be implemented later*/
    @Override
    public Optional<T> findByPIB(String firstName, String lastName, String middleName) {
        return Optional.empty();
    }
}
