package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.repository.InMemoryTeacherRepository;
import ua.naukma.repository.PersonRepository;
import ua.naukma.repository.Repository;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.PersonInfoVerificator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class TeacherService {
    private final PersonRepository<Teacher, Integer> repository;
    public TeacherService() {
        this.repository = inMemoryT;
    }
    private final InMemoryTeacherRepository inMemoryT = new InMemoryTeacherRepository();

    public void addTeacher() {
        Teacher tec = teacher_validate_all();
        try_addTeacher(tec);
    }
    public void deleteTeacher() {
        int id = IdVerificator.ask_id();
        Optional<Teacher> teacher = repository.findById(id);
        if (teacher.isPresent()) {
            repository.deleteById(id);
        }
        else {
            System.out.println("Teacher with such id doesn't exist.");
        }
    }
    public void findTeacher() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Find by PIB or ID? (1/2): ");
        int choice = 0;
        while (choice != 1 && choice != 2) {
            try {
                choice = sc.nextInt();
            }catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }
        switch (choice) {
            case 1:
                teacher_findByPIB();
                break;
            case 2:
                teacher_findById();
                break;
        }
    }
    private void teacher_findById(){
        int id = IdVerificator.ask_id();
        Optional<Teacher> teacher = repository.findById(id);
        if (teacher.isPresent()){
            System.out.println(teacher.get());
        }else{
            System.out.println("Teacher with such id doesn't exist.");
        }
    }
    private void teacher_findByPIB(){
        boolean is_name_english = PersonInfoVerificator.ask_alphabet();
        String firstName = PersonInfoVerificator.ask_name("first name", is_name_english);
        String lastName = PersonInfoVerificator.ask_name("last name",is_name_english);
        String middleName = PersonInfoVerificator.ask_name("middle name", is_name_english);
        Optional<Teacher> t = repository.findByPIB(firstName, lastName, middleName);
        if (t.isPresent()){
            System.out.println(t.get());
        }
        else{
            System.out.println("Teacher with such PIB doesn't exist.");
        }
    }
    public void teachersShowList(){
        repository.showAll();
    }
    private void try_addTeacher(Teacher t) {
        Optional<Teacher> teacher = repository.findById(t.getId());
        if (teacher.isPresent()) {
            System.out.println("Teacher with such id already exists.");
            return;
        }
        inMemoryT.save(t);
    }
    private Teacher teacher_validate_all() {
        System.out.println("Add teacher");
        PersonInfoVerificator.PersonData pd = PersonInfoVerificator.ask_common_info();
        TeacherPosition teacherPosition = ask_position();
        TeacherDegree teacherDegree = ask_degree();
        TeacherRank teacherRank = ask_rank();
        double load = ask_load();
        LocalDate hiring_date = ask_hiring_date();
        Teacher t = new Teacher(pd.id(), pd.firstName(), pd.lastName(), pd.middleName(),
                pd.birthDate(), pd.email(), pd.phoneNumber(), teacherPosition,
                teacherDegree, teacherRank, hiring_date, load);
        return t;
    }

    private double ask_load(){
        Scanner scanner = try_init_scanner();
        double load = 0;
        String error_message = "Invalid load: ";
        do{
            try {
                load = scanner.nextDouble();
            }catch (InputMismatchException e){
                System.out.println("Invalid input");
            }
            if(load < 1.8 || load > 4.5){
                System.out.println(error_message + load);
            }
        }while(load < 1.8 || load > 4.5);
        return load;
    }
    private LocalDate ask_hiring_date() {
        Scanner scanner = try_init_scanner();
        LocalDate doh;
        String error_message = "Impossible hiring date: ";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        do{
            System.out.println("Enter hiring date (dd.MM.yyyy): ");
            String s = scanner.nextLine();
            try{
                doh = LocalDate.parse(s, dtf);
                if(doh.isAfter(LocalDate.now())) {
                    System.out.println(error_message + doh);
                    doh = null;
                }
            }catch(DateTimeParseException ex){
                System.out.println("Illegal date format. " + s);
                doh = null;
            }
        }while(doh == null);
        return doh;
    }
    private TeacherPosition ask_position(){
        System.out.println("Enter teacher's position: ");
        Scanner scanner = try_init_scanner();
        TeacherPosition position;
        do {
            try {
                String s = scanner.nextLine();
                position = TeacherPosition.fromString(s);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                position = null;
            }
        }while(position == null);
        return position;
    }
    private TeacherDegree ask_degree(){
        System.out.println("Enter teacher's degree: ");
        Scanner scanner = try_init_scanner();
        TeacherDegree degree;
        do {
            try {
                String s = scanner.nextLine();
                degree = TeacherDegree.fromString(s);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                degree = null;
            }
        }while(degree == null);
        return degree;
    }
    private TeacherRank ask_rank(){
        System.out.println("Enter teacher's rank: ");
        Scanner scanner = try_init_scanner();
        TeacherRank rank;
        do {
            try {
                String s = scanner.nextLine();
                rank = TeacherRank.fromString(s);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                rank = null;
            }
        }while(rank == null);
        return rank;
    }
    private static Scanner try_init_scanner() {
        Scanner scanner = null;
        while (scanner == null) {
            try {
                scanner = new Scanner(System.in);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        return scanner;
    }
}
