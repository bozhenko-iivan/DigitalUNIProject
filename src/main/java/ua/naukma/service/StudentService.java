package ua.naukma.service;

import ua.naukma.domain.Student;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.Repository;
import ua.naukma.domain.StudyForm;
import ua.naukma.domain.StudentStatus;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;


public class StudentService {
    private final Repository<Student, Integer> repository;
    public StudentService() {
        this.repository = inMemoryS;
    }
    private InMemoryStudentRepository inMemoryS = new InMemoryStudentRepository();
    public void addStudent() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Add student");
            int id = ask_id(scanner);
            boolean is_name_english = ask_alphabet();
            String firstName = ask_name("first name", is_name_english);
            String lastName = ask_name("last name",is_name_english);
            String middleName = ask_name("middle name", is_name_english);
            LocalDate dob = ask_dob();
            String email = validate_email(scanner);
            String phoneNumber = validate_phonenum(scanner);
            int admissionYear = admission_year();
            int course = course();
            StudyForm studyForm = ask_study_form();
            StudentStatus studentStatus = ask_student_status();
            String groupName = ask_groupname();
            String recordbookNum = generate_recordbook_num(lastName, id, admissionYear);
            Student new_s = new Student(id, firstName, lastName, middleName, dob, email,
                    phoneNumber, recordbookNum, course,groupName, admissionYear, studyForm, studentStatus);
            Optional <Student> student = repository.findById(id);
            if (student.isPresent()) {
                System.out.println("Student with such id already exists.");
                return;
            }
            inMemoryS.save(new_s);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteStudent() {
        try {
            Scanner scanner = new Scanner(System.in);
            int id = ask_id(scanner);
            Optional<Student> student = repository.findById(id);
            if (student.isPresent()) {
                repository.deleteById(id);
            }
            else {
                System.out.println("Student with such id doesn't exist.");
            }
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    public void findStudent() {
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

                break;
            case 2:
                service_findById();
                break;
        }
    }
    private void service_findById(){
        try {
            Scanner scanner = new Scanner(System.in);
            int id = ask_id(scanner);
            Optional<Student> student = repository.findById(id);
            if (student.isPresent()){
                student.ifPresent(s -> System.out.println(s.toString()));
            }else{
                System.out.println("Student with such id doesn't exist.");
            }
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private void service_findByPIB(){
        boolean is_name_english = ask_alphabet();
        String firstName = ask_name("first name", is_name_english);
        String lastName = ask_name("last name",is_name_english);
        String middleName = ask_name("middle name", is_name_english);
        inMemoryS.findByPIB(firstName, lastName, middleName);
    }
    public void studentsShowList(){
        inMemoryS.showAll();
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
    private String ask_name(String smth, boolean is_english) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's " + smth + ": ");
        String s = scanner.nextLine();
        while(s.isEmpty()) {
            s = scanner.nextLine();
        }
        char c = s.charAt(0);
        if(!is_english && (c < 'А' || c > 'Я')&& c != 'Ї'&& c != 'І') {
            System.out.println("Invalid input or no capital letter.");
            return ask_name(smth, is_english);
        }
        if(is_english && (c < 'A' || c > 'Z')){
            System.out.println("Invalid input or no capital letter.");
            return ask_name(smth, is_english);
        }
        boolean isalpha = isAlpha(s, is_english);
        if(s.length() < 3 || !isalpha ){
            System.out.println("Invalid input or no capital letter.");
            return ask_name(smth, is_english);
        }
        return s;
    }
    private int ask_id(Scanner scanner) {
        int id = 0;
        try {
            System.out.println("Enter student's ID (7 digits long): ");
            id = Integer.parseInt(scanner.nextLine());
            while(id/1000000 <= 0 || id/1000000 >= 10){
                System.out.println("ID must be 7 digits long: ");
                id = Integer.parseInt(scanner.nextLine());
            }
        }catch (NumberFormatException e) {
            System.out.println("Invalid input");
            return ask_id(scanner);
        }
        return id;
    }
    private String validate_email(Scanner scanner) {
        System.out.println("Enter student's email: ");
        String email = scanner.nextLine();
        for(char c : email.toCharArray()){
            if(c >= 'А' && c <= 'Я' || c >= 'а' && c <= 'я'|| 
                    c == 'ї'|| c == 'Ї'|| c == 'І'|| c == 'і' ){
                System.out.println("Invalid email.\n" +
                        "Examples of a valid one:\n" +
                        "ivanLastivka@gmail.com\n" +
                        "or   ivanLastivka@ukr.net\n");
                return validate_email(scanner);
            }
        }
        String target1 = "@gmail.com", target2 = "@ukr.net";
        int dom_occ = count_occurances(target1, target2, email);
        int sob_occ =  count_occurances("@", "", email);
        int dots_occ = count_occurances("..", "", email);
        boolean spaces = spacesPresent(email, scanner);
        if(dom_occ == 1 && sob_occ == 1 && !spaces && dots_occ == 0){
            if(email.indexOf(target1) < email.length() - target1.length() && email.indexOf(target1) != -1 ){
                System.out.println("Invalid email.\n" +
                        "Examples of a valid one:\n" +
                        "ivanLastivka@gmail.com\n" +
                        "or   ivanLastivka@ukr.net\n");
                return validate_email(scanner);
            }else if(email.indexOf(target2) < email.length() - target2.length() && email.indexOf(target2) != -1  ){
                System.out.println("Invalid email.\n" +
                        "Examples of a valid one:\n" +
                        "ivanLastivka@gmail.com\n" +
                        "or   ivanLastivka@ukr.net\n");
                return validate_email(scanner);
            }
            else return email;
        }
        else {
            System.out.println("Invalid email.\n" +
                    "Examples of a valid one:\n" +
                    "ivanLastivka@gmail.com\n" +
                    "or   ivanLastivka@ukr.net\n");
            return validate_email(scanner);
        }
    }
    private String validate_phonenum(Scanner scanner) {
        System.out.println("Enter student's phone number: ");
        boolean cntr_code = false, valid_length = false, digits_only = false;
        String phone_number = scanner.nextLine();
        if(phone_number.indexOf("+380") == 0) cntr_code = true;
        String num = phone_number.substring(3);
        if(num.length() == 10) valid_length = true;
        if(digits_only(num,scanner)) digits_only = true;
        if(cntr_code && valid_length && digits_only) return phone_number;
        else {
            System.out.println("Phone number must contain Ukraine's country code and" +
                    " have 10 digits afterwards\n" +
                    "Example:" +
                    " +380234567890\n" +
                    "Should not contain any spaces.");
            return validate_phonenum(scanner);
        }
    }
    private int count_occurances(String target1, String target2, String source) {
        int occurances = 0;
        for (int index = 0; index < source.length(); index++) {
            int index1 = source.indexOf(target1, index);
            if(index1 == -1) break;
            else occurances++;
            index = index1;
        }
        if(!target2.equals("")){
            for (int index = 0; index < source.length(); index++) {
                int index1 = source.indexOf(target2, index);
                if(index1 == -1) break;
                else occurances++;
                index = index1 + 1;
            }
        }
        return occurances;
    }
    private boolean spacesPresent(String source, Scanner scanner) {
        char[] s = source.toCharArray();
        for(int i = 0; i < s.length; i++){
            if(s[i] == ' '){
                return true;
            }
        }
        return false;
    }
    private boolean digits_only(String source, Scanner scanner) {
        char[] s = source.toCharArray();
        for(int i = 0; i < s.length; i++){
            if(s[i] < '0' || s[i] > '9'){
                return false;
            }
        }
        return true;
    }
    private LocalDate ask_dob(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        int years = 0;
        LocalDate dob = LocalDate.now();
        do{
        System.out.println("Enter date of birth (dd.MM.yyyy): ");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        try {
            dob = LocalDate.parse(s, dtf);
        }catch(DateTimeParseException e){
            System.out.println("Invalid date format.");
            continue;
        }
        years = Period.between(dob, LocalDate.now()).getYears();
        if(years < 17) System.out.println("Student is too young.");
        }while(years < 17);
        return dob;
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
