package ua.naukma.server.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class GroupService extends EntityService<Group, Integer> {
    private final Repository<Group, Integer> groupRepository;
    private final PersonRepository<Student, Integer> studentRepository;

    public GroupService(Repository<Group, Integer> repository, PersonRepository<Student, Integer> studentRepository, Class<Group> clazz) {
        super(repository, clazz);
        this.groupRepository = repository;
        this.studentRepository = studentRepository;
    }
    public List<Group> findAllByFacultyId(Integer facultyId) {
        return groupRepository.findAll().stream()
                .filter(g -> g.getFaculty() != null && g.getFaculty().getId() == facultyId)
                .collect(Collectors.toList());
    }
    public int getGroupCount(int facultyId) {
        return findAllByFacultyId(facultyId).size();
    }
}