package ua.naukma.server.repository;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface IOWork <T extends Serializable> {
    List<T> load() throws IOException;
    List<T> write(List<T> list) throws IOException;
}
