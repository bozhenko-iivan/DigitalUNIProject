package ua.naukma.server.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;
import ua.naukma.client.utils.IdVerificator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static ua.naukma.client.utils.AcademicInfoVerificator.ask_admission_year;
import static ua.naukma.client.utils.AcademicInfoVerificator.ask_course;

public class GroupService {
    private final Repository<Group, Integer> groupRepository;

    public GroupService(Repository<Group, Integer> repository) {
        this.groupRepository = repository;
    }

    public void addGroup(Group group) throws DuplicateEntityException {
        Optional<Group> optionalGroup = groupRepository.findById(group.getId());
        if (optionalGroup.isPresent()) {
            throw new DuplicateEntityException("Group already exists");
        }
        groupRepository.save(group);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (groupRepository.findById(id).isEmpty()) {
            throw  new EntityNotFoundException("Group with id " + id + " not found");
        }
        groupRepository.deleteById(id);
    }

    public Group findById(int id) throws EntityNotFoundException {
       Optional<Group> group = groupRepository.findById(id);
       if (group.isPresent()) {
           return group.get();
       }  else {
           throw  new EntityNotFoundException("Group with id " + id + " not found");
       }
    }

    public List<Group> findAllByFacultyId(int facultyId) throws EntityNotFoundException {
        return groupRepository.findAll().stream()
                .filter(g -> g.getFaculty() != null && g.getFaculty().getId() == facultyId)
                .collect(Collectors.toList());
    }
}
