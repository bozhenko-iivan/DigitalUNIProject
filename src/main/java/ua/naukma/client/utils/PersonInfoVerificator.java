package ua.naukma.client.utils;

import ua.naukma.exception.IncorrectNameException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PersonInfoVerificator {
    public static PersonData ask_common_info(Integer id) {
        //int id = IdVerificator.ask_id();
        boolean is_name_english = false;
        String firstName = ask_name("first name", is_name_english);
        String lastName = ask_name("last name",is_name_english);
        String middleName = ask_name("middle name", is_name_english);
        LocalDate dob = ask_dob();
        String email = EmailVerificator.ask_email();
        String phone_number = PhoneNumberVerificator.ask_phonenum();
        return new PersonData(id, firstName, lastName, middleName, dob, email, phone_number);
    }
    public record PersonData(int id, String firstName, String lastName, String middleName,
                             LocalDate birthDate, String email, String phoneNumber) {}
    public static String ask_name(String smth, boolean is_english){
        Scanner scanner = InitScanner.try_init_scanner();
        String error_message = "Invalid input or no capital letter.";
        String name;
        do{
            System.out.print("Enter person's " + smth + ": ");
            name = scanner.nextLine();
            name = validate_name(name, is_english);
            if(name == null) System.out.println(error_message);
        }while(name == null);
        return name;
    }
    private static String validate_name(String name, boolean is_english){
        if (name == null || name.isBlank()) {
            return null;
        }
        char c = name.charAt(0);
        if (!is_english && (c < 'А' || c > 'Я') && c != 'Ї' && c != 'І' && c != 'Є' && c != 'Ґ') {
            return null;
        }
        if (is_english && (c < 'A' || c > 'Z')) {
            return null;
        }
        boolean isalpha = isAlpha(name, is_english);
        if (name.length() < 3 || !isalpha) {
            return null;
        }
        return name;
    }

    public static boolean ask_alphabet() {
//        Scanner scanner = InitScanner.try_init_scanner();
//        String alphabet;
//        String error_message = "Invalid alphabet.";
//        do {
//            System.out.print("Enter alphabet in which you want to type person's name.\n" +
//                    "Latin/Cyrillic: ");
//            alphabet = scanner.nextLine();
//            alphabet = validate_alphabet(alphabet);
//            if (alphabet == null) System.out.println(error_message);
//        }while(alphabet == null);
//        return switch (alphabet) {
//            case "Latin" -> true;
//            case "latin" -> true;
//            case "Cyrillic" -> false;
//            case "cyrillic" -> false;
//            default -> false;
//        };
        return false;
    }
    private static String validate_alphabet(String alphabet) {
        return switch (alphabet.toLowerCase()) {
            case "latin", "cyrillic" -> alphabet;
            default -> null;
        };
    }
    private static boolean isAlpha(String s, boolean is_english){
        boolean isalpha = true;
        if(is_english) {
            for (char c : s.toCharArray()) {
                if ((c < 'А' || c > 'Я') && (c < 'а' || c > 'я') &&
                        (c != 'ї' && c != 'Ї' && c != 'І' && c != 'і' &&
                                c != 'Є' && c != 'є' && c != 'Ґ' && c != 'ґ' &&
                                c != '\'' && c != '`')) {
                    isalpha = false;
                    break;
                }
            }
        }
        else{
            for (char c : s.toCharArray()) {
                if ((c < 'А' || c > 'Я') && (c < 'а' || c > 'я') &&
                        (c != 'ї' && c != 'Ї' && c != 'І' && c != 'і' &&
                                c != 'Є' && c != 'є' && c != 'Ґ' && c != 'ґ' &&
                                c != '\'' && c != '`')) {
                    isalpha = false;
                    break;
                }
            }
        }
        return isalpha;
    }
    public static LocalDate ask_dob(){
        Scanner scanner = InitScanner.try_init_scanner();
        LocalDate dob = null;
        do{
            try {
                System.out.print("Enter date of birth (dd.MM.yyyy): ");
                String s = scanner.nextLine();
                dob = validate_dob(s);
            } catch (IncorrectNameException e) {
                System.out.println(e.getMessage());
            }
        }while(dob == null);
        return dob;
    }
    private static LocalDate validate_dob(String dob){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dob_date;
        int years;
        try {
            dob_date = LocalDate.parse(dob, dtf);
        }catch(DateTimeParseException e){
            System.out.println("Invalid date format.");
            return null;
        }
        years = Period.between(dob_date, LocalDate.now()).getYears();
        if (years > 101) {
            System.out.println("Person is too old");
            return null;
        } else if(years < 17){
            System.out.println("Person is too young");
            return null;
        }
        return dob_date;
    }
}
