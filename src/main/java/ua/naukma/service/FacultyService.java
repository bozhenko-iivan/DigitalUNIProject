package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.repository.InMemoryFacultyRepository;
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


public class FacultyService implements Service<Faculty, Integer> {
    private final Repository<Faculty, Integer> repository;
    private University university;
    public FacultyService(University u) {
        this.repository = new InMemoryFacultyRepository();
        this.university = u;
    }
    public University getUniversity(){
        return this.university;
    }
    @Override
    public void add() {
        Faculty newF = faculty_validate_all();
        try_AddFaculty(newF);
    }
    private void try_AddFaculty(Faculty f) {
            Optional<Faculty> faculty = repository.findById(f.getId());
            if (faculty.isPresent()) {
                System.out.println("Faculty with such id already exists.");
                return;
            }
            repository.save(f);
    }
    private Faculty faculty_validate_all(){
        int id = IdVerificator.ask_id();
        String name = FacilityNameVerificator.ask_facility_name();
        String shortname = FacilityNameVerificator.ask_short_name();
        String email = EmailVerificator.ask_email();
        PersonRepository<Student, Integer> globalRepo = university.getGlobalStudentRepository();
        Faculty f = new Faculty(id, name, shortname,null, email, globalRepo);
        return f;
    }
    @Override
    public void delete() {
        int id = IdVerificator.ask_id();
        Optional<Faculty> f = repository.findById(id);
        if (f.isPresent()) {
            repository.deleteById(id);
        }
        else {
            System.out.println("Faculty with such id doesn't exist.");
        }
    }
    @Override
    public Faculty findById() {
        System.out.println("Finding faculty with id.");
        int id = IdVerificator.ask_id();
        Optional<Faculty> faculty = repository.findById(id);
        if (faculty.isPresent()){
            System.out.println(faculty.get());
            return faculty.get();
        }else{
            System.out.println("Faculty with such id doesn't exist.");
            return null;
        }
    }
    public Faculty workWithFaculty(MenuLevel menu_level) {
        Faculty f = findById();
        System.out.println("Update/delete/or go to this faculty (1/2/3)?");
        int choice = readInt();
        while(choice < 1 || choice > 3){
            System.out.println("Invalid choice.");
            choice = readInt();
        }
        switch (choice) {
            case 1:
                System.out.println("Smth will be here.");
                return f;
            case 2:
                repository.deleteById(f.getId());
                break;
            case 3:
                menu_level = MenuLevel.FAC;
                return f;
        }
        return null;
    }
    public Faculty faculty_findById() {
        System.out.println("Finding faculty with id.");
        int id = IdVerificator.ask_id();
        Optional<Faculty> faculty = repository.findById(id);
        if (faculty.isPresent()){
            return faculty.get();
        }
        else{
            System.out.println("Faculty with such id doesn't exist.");
            return null;
        }
    }
    @Override
    public void showAll(){
        repository.findAll().forEach(System.out::println);
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