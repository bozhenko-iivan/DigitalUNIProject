package ua.naukma.server.service;

import ua.naukma.domain.Department;
import ua.naukma.domain.Teacher;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.annotation.Service;
import ua.naukma.server.repository.PersonRepository;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService extends EntityService<Department, Integer> {
    private final Repository<Department, Integer> repository;
    private final PersonRepository<Teacher, Integer> teacherRepository;

    public DepartmentService(Repository<Department, Integer> repository,
                             PersonRepository<Teacher, Integer> teacherRepository,
                             Class<Department> clazz) {
        super(repository, clazz);
        this.repository = repository;
        this.teacherRepository = teacherRepository;
    }

    public List<Department> findAllByFacultyId(int facultyId) {
        return repository.findAll().stream()
                .filter(d -> d.getFaculty().getId() == facultyId)
                .collect(Collectors.toList());
    }
    public int getAllDepartmentsCount(int facultyId) {
        return (int) repository.findAll().stream().filter(d -> d.getFaculty() != null
                && d.getFaculty().getId() == facultyId).count();
    }

    public Teacher setHead(int departmentId, int teacherId) {
        if (repository.findById(departmentId).isPresent()) {
            if (teacherRepository.findById(teacherId).isPresent()) {
                Department department = repository.findById(departmentId).get();
                Teacher teacher = teacherRepository.findById(teacherId).get();
                department.setHead(teacher);
                repository.save(department);
                return teacher;

            } else {
                throw new EntityNotFoundException("Teacher with id " + teacherId + " doesn't exist.");
            }
        } else {
            throw new EntityNotFoundException("Department with id " + departmentId + " doesn't exist.");
        }
    }
}