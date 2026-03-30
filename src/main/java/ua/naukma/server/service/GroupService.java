package ua.naukma.server.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class GroupService implements Service<Group, Integer> {
    private final Repository<Group, Integer> groupRepository;

    public GroupService(Repository<Group, Integer> repository) {
        this.groupRepository = repository;
    }

    @Override
    public void add(Group group) throws DuplicateEntityException {
        Optional<Group> optionalGroup = groupRepository.findById(group.getId());
        if (optionalGroup.isPresent()) {
            throw new DuplicateEntityException("Group already exists");
        }
        groupRepository.save(group);
    }

    @Override
    public void deleteById(Integer id) throws EntityNotFoundException {
        if (groupRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Group with id " + id + " not found");
        }
        groupRepository.deleteById(id);
    }

    @Override
    public Group findById(Integer id) throws EntityNotFoundException {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            return group.get();
        } else {
            throw new EntityNotFoundException("Group with id " + id + " not found");
        }
    }

    @Override
    public List<Group> findAll() {
        if  (groupRepository.findAll().isEmpty()) {
            throw new EntityNotFoundException("No groups have been found!");
        }
        return groupRepository.findAll();
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