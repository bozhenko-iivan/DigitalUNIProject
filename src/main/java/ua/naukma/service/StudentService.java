package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.repository.*;
import ua.naukma.utils.IdVerificator;
import ua.naukma.utils.PersonInfoVerificator;


import java.time.LocalDate;
import java.util.*;

import static ua.naukma.utils.PersonInfoVerificator.ask_name;

public class StudentService implements Service<Student, Integer> {
    private final PersonRepository<Student, Integer> repository;
    //private final Repository<Group, Integer> groupRepository;
    //private Faculty faculty;
    private Group group;

    public StudentService(University currUni, Group group) {
        this.repository = currUni.getStudentRepository();
        this.group = group;
    }

    @Override
    public void add() {
        try {
            Student s = student_validate_all();
            if (s != null){
                try_addStudent(s);
            }
        } catch (DuplicateEntityException e) {
            System.out.println("Registration failed: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    private Student student_validate_all() {
        System.out.println("Add student");
        int id = IdVerificator.ask_id();
        Optional<Student> optional = repository.findById(id);
        if (optional.isPresent()){
            throw new DuplicateEntityException("Student with id " + id + " already exists.");
        }
        PersonInfoVerificator.PersonData pd = PersonInfoVerificator.ask_common_info(id);
        int admissionYear = admission_year();
        int course = course();
        StudyForm studyForm = ask_study_form();
        StudentStatus studentStatus = ask_student_status();
        Group group = this.group;
        String recordbookNum = generate_recordbook_num(pd.lastName(), pd.id(), admissionYear);
        Student new_s = new Student(pd.id(), pd.firstName(), pd.lastName(), pd.middleName(), pd.birthDate(), pd.email(),
                pd.phoneNumber(), recordbookNum, course, group, admissionYear, studyForm, studentStatus);
        return new_s;
    }

    public void try_addStudent(Student new_s) throws DuplicateEntityException {
        Optional<Student> student = repository.findById(new_s.getId());
        if (student.isPresent()) {
            throw new DuplicateEntityException("Student with id " + new_s.getId() + " already exists");
        }
        repository.save(new_s);
        System.out.println("Student with id " + new_s.getId() + " has been added");
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

    public void changeGrade(Student new_s) {
        int id = IdVerificator.ask_id();
        Optional<Student> student = repository.findById(id);
        if (student.isPresent()) {

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


    private void service_findByPIB() {
        boolean is_name_english = ask_alphabet();
        String firstName = ask_name("first name", is_name_english);
        String lastName = ask_name("last name", is_name_english);
        String middleName = ask_name("middle name", is_name_english);
        Optional<Student> s = repository.findByPIB(firstName, lastName, middleName);
        s.ifPresentOrElse(
                System.out::println, () -> System.out.println("Teacher with such PIB doesn't exist.")
        );
    }

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

    private void student_findByPIB(){
        boolean is_name_english = PersonInfoVerificator.ask_alphabet();
        String firstName = ask_name("first name", is_name_english);
        String lastName = ask_name("last name",is_name_english);
        String middleName = ask_name("middle name", is_name_english);
        Optional<Student> s = repository.findByPIB(firstName, lastName, middleName);
        s.ifPresentOrElse(
                student -> System.out.println(student), () -> System.out.println("Student with such id doesn't exist.")
        );
    }

    @Override
    public void showAll(){
        System.out.println("Students in group " + this.group.getName() + ":");
        repository.findAll().stream()
                .filter(s -> s.getGroup().equals(this.group))
                .forEach(System.out::println);
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
    private int course(){
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
//    private Group ask_group(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Available groups");
//        groupRepository.showAll();
//        while (true) {
//            System.out.println("Enter group name: (e.g IPZ-1): ");
//            String s = scanner.nextLine().trim();
//            if (s.isEmpty()) {
//                System.out.println("Invalid input.");
//                continue;
//            }
//            Optional<Group> group = ((InMemoryGroupRepository) groupRepository).findByName(s);
//            if (group.isPresent()) {
//                return group.get();
//            } else  {
//                System.out.println("Group not found.");
//            }
//        }
//    }
    private String generate_recordbook_num(String lastName, int id, int year){
        Random rand = new Random();
        String s = lastName.charAt(0) + "-" + (id % year) + lastName.charAt(1) + "-" + rand.nextInt(999);
        System.out.println("Your recordbook number is: " + s);
        return s;
    }
}