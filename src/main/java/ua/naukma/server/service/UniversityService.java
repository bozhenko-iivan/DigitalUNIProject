package ua.naukma.server.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;

public class UniversityService {
    private final Repository<University, Integer> repository;

    public UniversityService(Repository<University, Integer> repository) {
        this.repository = repository;
    }

    public void addUniversity(University university) throws DuplicateEntityException {
        if (repository.findById(university.getId()).isPresent()) {
            throw new DuplicateEntityException("University with id " + university.getId() + " already exists.");
        }
        repository.save(university);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("University with id " + id + " not found!");
        }
        repository.deleteById(id);
    }

    public University findById(int id) throws EntityNotFoundException {
        Optional<University> university = repository.findById(id);
        if (university.isPresent()) {
            return university.get();
        } else {
            throw new EntityNotFoundException("University with id " + id + " not found!");
        }
    }

    public List<University> getAllUniversities() {
        return repository.findAll();
    }

    public void update(University currentUni) {
        repository.save(currentUni);
    }

    public void transferStudent(University currentUni, int studentID, String newGroupName) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Student> optionalStudent = currentUni.getStudentRepository().findById(studentID);
        if (optionalStudent.isEmpty()) {
            throw new EntityNotFoundException("Student with id " + studentID + " not found!");
        }
        Student studentToTransfer = optionalStudent.get();
        Group currentGroup = studentToTransfer.getGroup();
        Group targetGroup = findGroupByName(currentUni, newGroupName);
        if (targetGroup == null) {
            throw new EntityNotFoundException("Group with name " + newGroupName + " not found!");
        }
        if (currentGroup != null && currentGroup.equals(targetGroup)) {
            throw new DuplicateEntityException("Student already studies in this group: " + targetGroup.getName());
        }
        studentToTransfer.setGroup(targetGroup);
        currentUni.getStudentRepository().save(studentToTransfer);
    }

    public Group findGroupByName(University currentUni, String newGroupName) {
        for (Group group : currentUni.getGroupRepository().findAll()) {
            if (group.getName().equals(newGroupName)) {
                return group;
            }
        }
        return null;
    }
}