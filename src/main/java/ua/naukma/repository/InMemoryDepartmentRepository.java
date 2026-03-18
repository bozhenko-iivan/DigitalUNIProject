package ua.naukma.repository;

import ua.naukma.domain.Department;
import ua.naukma.service.Service;

import java.io.Serializable;
import java.util.*;

public class InMemoryDepartmentRepository implements Repository<Department, Integer>, Serializable {
    private Map<Integer, Department> storage = new HashMap<>();

    @Override
    public void save(Department d) {
        storage.put(d.getId(), d);
        System.out.println("Department saved: " + d.getName());
    }

    @Override
    public Optional<Department> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void showAll(){
        storage.values().forEach(System.out::println);
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Department deleted: " + id);
    }
    @Override
    public List<Department> findAll() {
        return new ArrayList<>(storage.values());
    }
}
