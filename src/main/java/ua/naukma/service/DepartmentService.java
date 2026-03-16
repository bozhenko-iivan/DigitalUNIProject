package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.repository.InMemoryDepartmentRepository;
import ua.naukma.repository.PersonRepository;
import ua.naukma.repository.Repository;
import ua.naukma.ui.MenuLevel;
import ua.naukma.utils.EmailVerificator;
import ua.naukma.utils.FacilityNameVerificator;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.InitScanner;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DepartmentService implements Service<Department, Integer> {
    private final Repository<Department, Integer> repository;
    private Faculty faculty;
    public DepartmentService(University currUni,Faculty f) {
        this.repository = currUni.getDepartmentRepository();
        this.faculty = f;
    }
    public Faculty getFaculty() { return faculty; }

    @Override
    public void add() {
        Department newD = department_validate_all();
        try_AddDepartment(newD);
    }
    private void try_AddDepartment(Department d) {
        repository.save(d);
        System.out.println("Department with such id successfully added.");
    }
    @Override
    public void delete() {
        int id = IdVerificator.ask_id();
        Optional<Department> d = repository.findById(id);
        if (d.isPresent()) {
            repository.deleteById(id);
        }
        else {
            System.out.println("Department with such id doesn't exist.");
        }
    }
   @Override
    public Department findById() {
        System.out.println("Finding department by id.");
        int id = IdVerificator.ask_id();
        Optional<Department> department = repository.findById(id);
        if (department.isPresent()){
            System.out.println(department.get());
            return department.get();

        }else{
            System.out.println("Department with such id doesn't exist.");
            return null;
        }
    }
    public Department workWithDepartment(MenuLevel menu_level) {
        Department d = findById();
        System.out.println("Update/delete/or go to this department (1/2/3)?");
        int choice = readInt();
        while(choice < 1 || choice > 3){
            System.out.println("Invalid choice.");
            choice = readInt();
        }
        switch (choice) {
            case 1:
                System.out.println("Smth will be here.");
                return d;
            case 2:
                repository.deleteById(d.getId());
                break;
            case 3:
                menu_level = MenuLevel.DEPARTAMENT;
                return d;
        }
        return null;
    }
    @Override
    public void showAll(){
        System.out.println("Finding all departments of " + faculty.getName());
        repository.findAll().stream().filter(d -> d.getFaculty().getId() == faculty.getId()).forEach(System.out::println);
    }

    private Department department_validate_all(){
        int id;
        while (true) {
            id = IdVerificator.ask_id();
            Optional<Department> existingDep = repository.findById(id);
            if (existingDep.isPresent()) {
                System.out.println("Department with such id already exists. Please choose another id.");
            } else {
                break;
            }
        }
        String name = FacilityNameVerificator.ask_facility_name();
        String email = EmailVerificator.ask_email();
        Teacher tc = null;
        Faculty fac = getFaculty();
        String location = ask_location();
        Department d = new Department(id, name, fac, tc, location,  email);
        return d;
    }
    private String ask_location(){
        System.out.println("Please enter the location of the faculty.");
        Scanner sc = InitScanner.try_init_scanner();
        String location = sc.nextLine();
        return location;
    }
    private static int readInt(){
        Scanner scanner = InitScanner.try_init_scanner();
        for(;;) {
            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer.");
                scanner.next();
                continue;
            }
            return choice;
        }
    }
}
