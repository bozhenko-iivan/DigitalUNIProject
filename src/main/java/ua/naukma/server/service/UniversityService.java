package ua.naukma.server.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ua.naukma.server.annotation.Service
public class UniversityService implements Service<University, Integer> {
    private final Repository<University, Integer> repository;

    public UniversityService(Repository<University, Integer> repository) {
        this.repository = repository;
    }

    @Override
    public void add(University university) throws DuplicateEntityException {
        if (repository.findById(university.getId()).isPresent()) {
            throw new DuplicateEntityException("University with id " + university.getId() + " already exists.");
        }
        repository.save(university);
    }

    @Override
    public void deleteById(Integer id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("University with id " + id + " not found!");
        }
        repository.deleteById(id);
    }

    @Override
    public University findById(Integer id) throws EntityNotFoundException {
        Optional<University> university = repository.findById(id);
        if (university.isPresent()) {
            return university.get();
        } else {
            throw new EntityNotFoundException("University with id " + id + " not found!");
        }
    }

    @Override
    public List<University> findAll() {
        if (repository.findAll().isEmpty()) {
            throw new EntityNotFoundException("No universities have been found!");
        }
        return repository.findAll();
    }

    public int getAllUniversitiesCount() {
        return repository.findAll().size();
    }

//    public void transferStudent(University currentUni, int studentID, String newGroupName) throws EntityNotFoundException, DuplicateEntityException {
//        Optional<Student> optionalStudent = currentUni.getStudentRepository().findById(studentID);
//        if (optionalStudent.isEmpty()) {
//            throw new EntityNotFoundException("Student with id " + studentID + " not found!");
//        }
//        Student studentToTransfer = optionalStudent.get();
//        Group currentGroup = studentToTransfer.getGroup();
//        Group targetGroup = findGroupByName(currentUni, newGroupName);
//        if (targetGroup == null) {
//            throw new EntityNotFoundException("Group with name " + newGroupName + " not found!");
//        }
//        if (currentGroup != null && currentGroup.equals(targetGroup)) {
//            throw new DuplicateEntityException("Student already studies in this group: " + targetGroup.getName());
//        }
//        studentToTransfer.setGroup(targetGroup);
//        currentUni.getStudentRepository().save(studentToTransfer);
//    }
}