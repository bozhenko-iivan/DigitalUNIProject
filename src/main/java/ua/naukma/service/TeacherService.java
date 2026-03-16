package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.repository.PersonRepository;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.InitScanner;
import ua.naukma.utils.PersonInfoVerificator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class TeacherService implements Service <Teacher, Integer> {
    private final PersonRepository<Teacher, Integer> repository;
    private Department department;

    public TeacherService(University currUni, Department department) {
        this.department = department;
        this.repository = currUni.getTeacherRepository();
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public void add() {
        Teacher tec = teacher_validate_all();
        try_addTeacher(tec);
    }

    private void try_addTeacher(Teacher t) {
        repository.save(t);
        System.out.println("Teacher with id " + t.getId() + " has been added.");
    }

    @Override
    public void delete() {
        int id = IdVerificator.ask_id();
        try {
            Optional<Teacher> teacher = repository.findById(id);
            if (teacher.isEmpty()) {
                throw new EntityNotFoundException("Teacher with id " + id + " does not exist.");
            }
            repository.deleteById(id);
            System.out.println("Teacher with id " + id + " has been deleted.");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Teacher findById(){
        int id = IdVerificator.ask_id();
        try {
            Optional<Teacher> teacher = repository.findById(id);
            if (teacher.isEmpty()) {
                throw new EntityNotFoundException("Teacher with id " + id + " does not exist.");
            }
            System.out.println("Teacher found:\n" + teacher.get());
            return teacher.get();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void teacher_findByPIB(){
        boolean is_name_english = PersonInfoVerificator.ask_alphabet();
        String firstName = PersonInfoVerificator.ask_name("first name", is_name_english);
        String lastName = PersonInfoVerificator.ask_name("last name",is_name_english);
        String middleName = PersonInfoVerificator.ask_name("middle name", is_name_english);
        Optional<Teacher> t = repository.findByPIB(firstName, lastName, middleName);
        t.ifPresentOrElse(
                System.out::println, () -> System.out.println("Teacher with such PIB doesn't exist.")
        );
    }

    @Override
    public void showAll() {
        System.out.println("Teachers in department " + department.getName() + ":");
        repository.findAll().stream()
                .filter(t -> t.getDepartment().getId() == department.getId())
                .forEach(System.out::println);
    }

    private Teacher teacher_validate_all() {
        System.out.println("Add teacher");
        int id;
        while (true) {
            id = IdVerificator.ask_id();
            Optional<Teacher> optional = repository.findById(id);
            if (optional.isPresent()) {
                System.out.println("Teacher with such id already exists. Please choose another id.");
            } else {
                break;
            }
        }
        PersonInfoVerificator.PersonData pd = PersonInfoVerificator.ask_common_info(id);
        TeacherPosition teacherPosition = ask_position();
        TeacherDegree teacherDegree = ask_degree();
        TeacherRank teacherRank = ask_rank();
        double load = ask_load();
        LocalDate hiring_date = ask_hiring_date();
        Teacher t = new Teacher(pd.id(), pd.firstName(), pd.lastName(), pd.middleName(),
                pd.birthDate(), pd.email(), pd.phoneNumber(), teacherPosition,
                teacherDegree, teacherRank, hiring_date, load, this.department);
        return t;
    }

    private double ask_load(){
        System.out.println("Enter teacher's load (between 1.8 and 4.5): ");
        Scanner scanner = InitScanner.try_init_scanner();
        double load = 0;
        while (true) {
            try {
                load = scanner.nextDouble();
                if (load > 1.8 && load < 4.5) {
                    scanner.nextLine();
                    return load;
                } else {
                    System.out.println("Invalid load. Must be between 1.8 and 4.5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private LocalDate ask_hiring_date() {
        System.out.println("Available teacher's positions:");
        Scanner scanner = InitScanner.try_init_scanner();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        while (true) {
            System.out.println("Enter hiring date (dd.MM.yyyy): ");
            String s = scanner.nextLine();
            try {
                LocalDate doh = LocalDate.parse(s, dtf);
                if (doh.isAfter(LocalDate.now())) {
                    System.out.println("Impossible hiring date: " + doh);
                } else {
                    return doh;
                }
            } catch (DateTimeParseException ex) {
                System.out.println("Illegal date format. " + s);
            }
        }
    }

    private TeacherPosition ask_position(){
        Scanner scanner = InitScanner.try_init_scanner();
        for (TeacherPosition tp : TeacherPosition.values()) {
            System.out.println(tp.name());
        }
        System.out.println("Enter teacher's position: ");
        while (true) {
            try {
                String s = scanner.nextLine();
                return TeacherPosition.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException | InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    private TeacherDegree ask_degree(){
        System.out.println("Available teacher's degrees:");
        Scanner scanner = InitScanner.try_init_scanner();
        for (TeacherDegree d : TeacherDegree.values()) {
            System.out.println(d.name());
        }
        System.out.println("Enter teacher's degree: ");
        while (true) {
            try {
                String s = scanner.nextLine();
                return TeacherDegree.fromString(s.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private TeacherRank ask_rank(){
        System.out.println("Available teacher's rank:");
        Scanner scanner = InitScanner.try_init_scanner();
        for (TeacherRank r : TeacherRank.values()) {
            System.out.println(r.name());
        }
        System.out.println("Enter teacher's rank: ");
        while (true) {
            try {
                String s = scanner.nextLine();
                return TeacherRank.fromString(s.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}