package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.PersonRepository;
import ua.naukma.repository.Repository;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.PersonInfoVerificator;


import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class StudentService implements Service<Student, Integer> {
    private final PersonRepository<Student, Integer> repository;
    private Faculty faculty;

    public StudentService(Faculty faculty) {
        this.repository = new InMemoryStudentRepository();
        this.faculty = faculty;
    }

    @Override
    public void add() {
        Student s = student_validate_all();
        try_addStudent(s);
    }

    private Student student_validate_all() {
        System.out.println("Add student");
        PersonInfoVerificator.PersonData pd = PersonInfoVerificator.ask_common_info();
        int admissionYear = admission_year();
        int course = course();
        StudyForm studyForm = ask_study_form();
        StudentStatus studentStatus = ask_student_status();
        String groupName = ask_groupname();
        String recordbookNum = generate_recordbook_num(pd.lastName(), pd.id(), admissionYear);
        Student new_s = new Student(pd.id(), pd.firstName(), pd.lastName(), pd.middleName(), pd.birthDate(), pd.email(),
                pd.phoneNumber(), recordbookNum, course, groupName, admissionYear, studyForm, studentStatus);
        return new_s;
    }

    private void try_addStudent(Student new_s) {
        Optional<Student> student = repository.findById(new_s.getId());
        if (student.isPresent()) {
            System.out.println("Student with such id already exists.");
            return;
        }
        repository.save(new_s);
    }

    @Override
    public void delete() {
        int id = IdVerificator.ask_id();
        Optional<Student> student = repository.findById(id);
        if (student.isPresent()) {
            repository.deleteById(id);
        } else {
            System.out.println("Student with such id doesn't exist.");
        }
    }

    //    public void findStudent() {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Find by PIB or ID? (1/2): ");
//        int choice = 0;
//        while (choice != 1 && choice != 2) {
//            try {
//                choice = sc.nextInt();
//            }catch (InputMismatchException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        switch (choice) {
//            case 1:
//                student_findByPIB();
//                break;
//            case 2:
//                student_findById();
//                break;
//        }
//    }
    @Override
    public Student findById() {
        int id = IdVerificator.ask_id();
        Optional<Student> student = repository.findById(id);
        if (student.isPresent()) {
            System.out.println(student.get());
            return student.get();
        } else {
            System.out.println("Student with such id doesn't exist.");
            return null;
        }
    }


//    private void service_findByPIB() {
//        boolean is_name_english = ask_alphabet();
////        String firstName = ask_name("first name", is_name_english);
////        String lastName = ask_name("last name", is_name_english);
////        String middleName = ask_name("middle name", is_name_english);
////
////        //            ЛЯМБДИ
////        java.util.List<Student> foundStudents = repository.findAll().stream()
////                .filter(student -> student.getFirstName().equalsIgnoreCase(firstName))
////                .filter(student -> student.getLastName().equalsIgnoreCase(lastName))
////                .filter(student -> student.getMiddleName().equalsIgnoreCase(middleName))
////                .toList();
//
//        if (foundStudents.isEmpty()) {
//            System.out.println("Студентів з таким ПІБ не знайдено.");
//        } else {
//            System.out.println("Знайдено студентів:");
//            foundStudents.forEach(System.out::println);
//        }
//    }

    public void studentsShowList(){
        repository.showAll();
    }
    private boolean ask_alphabet() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter alphabet in which you want to type student's name.\n" +
                "Latin/Cyrillic: ");
        String language = scanner.next();
        switch (language) {
            case "Latin": return true;
            case "Cyrillic": return false;
            default: {
                System.out.println("Invalid language.");
                return ask_alphabet();
            }
        }
    }
    private boolean isAlpha(String s, boolean is_english){
        boolean isalpha = true;
        if(is_english) {
            for (char c : s.toCharArray()) {
                if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                    isalpha = false;
                    break;
                }
            }
        }
        else{
            for (char c : s.toCharArray()) {
                if ((c < 'А' || c > 'Я') && (c < 'а' || c > 'я')&&
                        ( c != 'ї'&& c != 'Ї'&& c != 'І'&& c != 'і')) {
                    isalpha = false;
                    break;
                }
            }
        }
        return isalpha;
    }

//    private void student_findByPIB(){
//        boolean is_name_english = PersonInfoVerificator.ask_alphabet();
//        String firstName = PersonInfoVerificator.ask_name("first name", is_name_english);
//        String lastName = PersonInfoVerificator.ask_name("last name",is_name_english);
//        String middleName = PersonInfoVerificator.ask_name("middle name", is_name_english);
//        Optional<Student> s = repository.findByPIB(firstName, lastName, middleName);
//        if (s.isPresent()){
//            System.out.println(s.get());
//        }
//        else{
//            System.out.println("Student with such PIB doesn't exist.");
//
//        }
//    }
    @Override
    public void showAll(){
        repository.showAll();
    }

    private int admission_year(){
        Scanner scanner = new Scanner(System.in);
        int year = 2027;
        do {
            System.out.println("Enter the year of admission: ");
            try {
                year = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
            }
            if (year > LocalDate.now().getYear()|| year < 1994) System.out.println("Impossible year.");
        }while(year > LocalDate.now().getYear()|| year < 1994);
        return year;
    }
    private  int course(){
        Scanner scanner = new Scanner(System.in);
        int course = 0;
        while(course > 6 || course < 1){
            System.out.println("Enter course: ");
            try {
                course = scanner.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Invalid input.");
            }
        }
        return course;
    }
    private StudyForm ask_study_form(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter study form." +
                "\n(Бюджет/Контракт) або (1/2): )");
        String s = scanner.nextLine();
        switch(s){
            case "Бюджет": case "1": return StudyForm.BUDGET;
            case "Контракт": case "2": return StudyForm.CONTRACT;
            default: {
                System.out.println("Invalid input.");
                return ask_study_form();
            }
        }
    }
    private StudentStatus ask_student_status(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student status." +
                "\n(Навчається/Академ відпустка/Виключений) або (1/2/3): )");
        String s = scanner.nextLine();
        switch(s){
            case "Навчається": case "1": return StudentStatus.STUDYING;
            case "Академ відпустка": case "2": return StudentStatus.ACADEMIC_LEAVE;
            case "Виключений": case "3": return StudentStatus.EXPELLED;
            default: {
                System.out.println("Invalid input.");
                return ask_student_status();
            }
        }
    }
    private String ask_groupname(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter group name: ");
        String s = scanner.nextLine();
        while(s == null || s.isBlank()){
            System.out.println("Invalid input.");
            s = scanner.nextLine();
        }
        return s;
    }
    private String generate_recordbook_num(String lastName, int id, int year){
        Random rand = new Random();
        String s = lastName.charAt(0) + "-" + (id % year) + lastName.charAt(1) + "-" + rand.nextInt(999);
        System.out.println("Your recordbook number is: " + s);
        return s;
    }
}