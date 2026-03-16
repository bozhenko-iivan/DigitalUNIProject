package ua.naukma.utils;

import ua.naukma.domain.Student;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.exception.IncorrectNameException;

import java.util.Optional;
import java.util.Scanner;

import ua.naukma.repository.Repository;
import ua.naukma.service.StudentService;
import ua.naukma.utils.InitScanner;
import ua.naukma.domain.University.*;

public class UniversityVerificator {
    private static final String FULL_NAME_REGEX = "^[a-zA-Zа-яА-ЯіІїЇєЄґҐ\".,:()'\\-–/!?№@& ]+$";
    private static final String SHORT_NAME_REGEX = "^[a-zA-Zа-яА-ЯіІїЇєЄґҐ]+$";

    public static String ask_full_name() {
        return getString(FULL_NAME_REGEX);
    }

    public static String ask_short_name() {
        return getString(SHORT_NAME_REGEX);
    }

    private static String getString(String Regex) {
        Scanner scanner = InitScanner.try_init_scanner();
        String name;
        while (true) {
            try {
                System.out.println("Enter full name: ");
                name = scanner.next();
                if (name != null) {
                    name = name.trim().replaceAll("\\s+", " ");
                }
                if (isValidName(name, Regex) && areParenthesesValid(name)) {
                    return name;
                }
            } catch (IncorrectNameException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String ask_city() {
        Scanner scanner = InitScanner.try_init_scanner();
        String city_name;
        while (true) {
            try {
                System.out.println("Enter city name: ");
                city_name = scanner.nextLine();
                if (city_name != null) {
                    city_name = city_name.trim().replaceAll("\\s+", " ");
                }
                if (isValidName(city_name, FULL_NAME_REGEX) && areParenthesesValid(city_name)) {
                    return city_name;
                }
            } catch (IncorrectNameException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String ask_address() {
        Scanner scanner = InitScanner.try_init_scanner();
        String address;
        while (true) {
            try {
                System.out.println("Enter address: ");
                address = scanner.next();
                if (address != null) {
                    if (isValidName(address, SHORT_NAME_REGEX)) {
                        return address;
                    }
                }
            } catch (IncorrectNameException e) {
                System.out.println("Invalid address!");
            }
        }
    }

    private static boolean isValidName(String name, String regex) {
        return name != null && !name.isEmpty() && name.matches(regex);
    }

    private static boolean areParenthesesValid(String s) {
        if (s == null) return false;
        int quotes = 0;
        int parentheses = 0;

        for (char c : s.toCharArray()) {
            if (c == '"') quotes++;
            else if (c == '(') parentheses++;
            else if (c == ')') parentheses--;
            if (parentheses < 0) return false;
        }

        return quotes % 2 == 0 && parentheses == 0;
    }
}