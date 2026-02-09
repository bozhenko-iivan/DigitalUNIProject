package ua.naukma.repository;

import ua.naukma.domain.Department;
import ua.naukma.domain.Teacher;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryDepartmentRepository implements Repository<Department, Integer> {
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
        for (Department d : storage.values()) {
            System.out.println(d);
        }
    }
    @Override
    public void deleteById(Integer id) {
        storage.remove(id);
        System.out.println("Department deleted: " + id);
    }
}
