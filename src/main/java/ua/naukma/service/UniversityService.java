package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.repository.Repository;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.UniversityVerificator;

import javax.sql.rowset.serial.SerialArray;
import javax.sql.rowset.serial.SerialJavaObject;
import java.util.List;
import java.util.Optional;

public class UniversityService implements Service<University, Integer> {
    private final Repository<University, Integer> repository;

    public UniversityService(Repository<University, Integer> repository) {
        this.repository = repository;
    }

    public void try_add_university(University entity) {
        try {
            repository.save(entity);
            System.out.println("University added successfully!");
        } catch (DuplicateEntityException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void add() {
        University newU = university_validate_all();
        try_add_university(newU);
    }

    @Override
    public void delete() {
        int id = IdVerificator.ask_id();
        try {
            repository.deleteById(id);
            System.out.println("University deleted successfully!");
        } catch (EntityNotFoundException e) {
            System.out.println("University not found!");
        }
    }

    @Override
    public University findById() {
        int id = IdVerificator.ask_id();
        System.out.println("Finding University with id " + id);
        Optional<University> u = repository.findById(id);
        if (u.isPresent()) {
            System.out.println(u.get());
            return u.get();
        } else {
            System.out.println("University with id " + id + " not found!");
            return null;
        }
    }

    public void update(University currentUni) {
        try {
            repository.save(currentUni);
        } catch (Exception e) {
            System.out.println("Error updating university: " + e.getMessage());
        }
    }

    public void transferStudent(University currentUni,int studentID, String newGroupName) {
        Optional<Student> optionalStudent = currentUni.getStudentRepository().findById(studentID);
        if (optionalStudent.isPresent()) {
            throw new EntityNotFoundException("Student with id " + studentID + " not found!");
        }
        Student studentToTransfer = optionalStudent.get();
        Group currentGroup = studentToTransfer.getGroup();
        Group targetGroup = findGroupByName(currentUni, newGroupName);
        if (targetGroup == null) {
            throw new EntityNotFoundException("Group with name " + newGroupName + " not found!");
        }
        if (currentGroup.equals(targetGroup)) {
            throw new DuplicateEntityException("Student already studies in this group: " + currentGroup.getName());
        }
        Faculty currentFaculty = currentGroup.getFaculty();
        Faculty targetFaculty = targetGroup.getFaculty();
//        Department currentDept = currentGroup.getDepartment();
//        Department targetDept = targetGroup.getDepartment();
//        if (currentDept.getId() == targetDept.getId()) {
//            System.out.println("Transferring student with id " + studentID + " to " + targetDept.getName());}
         if (currentFaculty.getId() != targetFaculty.getId()) {
            System.out.println("Transferring student with id " + studentID + " to " + targetFaculty.getName());
        } else {
            //System.out.println("Transfer to another faculty!");
        }
        studentToTransfer.setGroup(targetGroup);
        System.out.println("Student with id " + studentID + " " +
                "has been transferred to " + targetGroup.getName() + " successfully!");
    }

    public Group findGroupByName(University currentUni,String newGroupName) {
        for (Group group : currentUni.getGroupRepository().findAll()) {
                    if (group.getName().equals(newGroupName)) {
                        return group;
                    }
                }
        return null;
    }

    @Override
    public void showAll() {
        System.out.println("All universities");
        repository.findAll().forEach(System.out::println);
    }

    private University university_validate_all() {
        int id = IdVerificator.ask_id();
        String fullName = UniversityVerificator.ask_full_name("fullName");
        String shortName = UniversityVerificator.ask_short_name("shortName");
        String city = UniversityVerificator.ask_city();
        String address = UniversityVerificator.ask_address();
        University u = new University(id, fullName, shortName, city, address);
        return u;
    }
}
