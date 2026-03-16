package ua.naukma.service;

import ua.naukma.domain.Department;
import ua.naukma.domain.Group;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.exception.IncorrectNameException;
import ua.naukma.repository.Repository;
import ua.naukma.utils.AcademicInfoVerificator;
import ua.naukma.utils.FacilityNameVerificator;
import ua.naukma.utils.IdVerificator;
import java.util.Optional;
import java.util.Scanner;

import static ua.naukma.utils.AcademicInfoVerificator.ask_admission_year;
import static ua.naukma.utils.AcademicInfoVerificator.ask_course;

public class GroupService implements Service<Group, Integer> {
    private final Repository<Group, Integer> groupRepository;
    private Department department;

    public GroupService(Repository<Group, Integer> groupRepository, Department department) {
        this.groupRepository = groupRepository;
        this.department = department;
    }

    @Override
    public void add() {
        Group gr = group_validate_all();
        try_add_group(gr);
    }

    private Group group_validate_all() throws IllegalArgumentException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Entering new group data");
                int id = IdVerificator.ask_id();
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
        try {
            Optional<Group> group = groupRepository.findById(gr.getId());
            if (group.isPresent()) {
                throw new DuplicateEntityException("Group with id " + gr.getId() + " already exists.");
            }
            groupRepository.save(gr);
            System.out.println("Group with id " + gr.getId() + " has been added.");
        } catch (DuplicateEntityException e) {
            System.out.println(e.getMessage());
        }
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
        groupRepository.showAll();
    }
}
