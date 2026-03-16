package ua.naukma.service;

import ua.naukma.domain.Department;
import ua.naukma.domain.Group;
import ua.naukma.domain.Student;
import ua.naukma.domain.University;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.repository.Repository;
import ua.naukma.utils.IdVerificator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static ua.naukma.utils.AcademicInfoVerificator.ask_admission_year;
import static ua.naukma.utils.AcademicInfoVerificator.ask_course;

public class GroupService implements Service<Group, Integer> {
    private final Repository<Group, Integer> groupRepository;
    private Department department;

    public GroupService(University currUni, Department department) {
        this.groupRepository = currUni.getGroupRepository();
        this.department = department;
    }

    @Override
    public void add() {
        Group gr = group_validate_all();
        try_add_group(gr);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    private Group group_validate_all() throws IllegalArgumentException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Entering new group data");
                int id;
                while (true) {
                    id = IdVerificator.ask_id();
                    Optional<Group> existingGroup = groupRepository.findById(id);
                    if (existingGroup.isPresent()) {
                        System.out.println("Group with such id already exists. Please choose another id.");
                    } else {
                        break;
                    }
                }
                System.out.println("Enter group name (e.g. IPZ-1): ");
                String name = scanner.nextLine().trim();
                while (name.isEmpty()) {
                    System.out.println("Name cannot be empty. Try again: ");
                    name = scanner.nextLine().trim();
                }
                int course = ask_course();
                int admissionYear = ask_admission_year();
                return new Group(id, name, department, course, admissionYear);

            } catch (IllegalArgumentException e) {
                System.out.println("Error creating group: " + e.getMessage());
            }
        }
    }

    private void try_add_group(Group gr) throws DuplicateEntityException {
        groupRepository.save(gr);
        System.out.println("Group has been added");
    }

    @Override
    public void delete() throws EntityNotFoundException {
        int id = IdVerificator.ask_id();
        try {
            Optional<Group> groupToDelete = groupRepository.findById(id);
            if (groupToDelete.isEmpty()) {
                throw new EntityNotFoundException("Group with id " + id + " does not exist.");
            }
            groupRepository.deleteById(id);
            System.out.println("Group with id " + id + " has been deleted.");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Group findById() throws EntityNotFoundException {
        int  id = IdVerificator.ask_id();
        try {
            Optional<Group> group = groupRepository.findById(id);
            if (group.isEmpty()) {
                throw new EntityNotFoundException("Group with id " + id + " does not exist.");
            }
            System.out.println("Group with id " + id + " has been found: " + group.get());
            return group.get();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void showAll() {
        System.out.println("Groups of " + department.getName() + ": ");
        groupRepository.findAll().stream().filter(g -> g.getDepartment().getId() == department.getId()).forEach(System.out::println);
    }
}
