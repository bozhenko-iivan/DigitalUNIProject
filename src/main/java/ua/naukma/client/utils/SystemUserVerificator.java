package ua.naukma.client.utils;

import ua.naukma.domain.SystemUserRoles;
import ua.naukma.exception.IncorrectDataException;

import java.util.Arrays;
import java.util.Scanner;

public class SystemUserVerificator {
    private static final String loginRegex = "^[a-zA-Z0-9.-]+$";
    private static final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8}$";

    public static String askLogin() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Please enter your login: ");
            String login = scanner.nextLine().trim();
            try {
                if (!login.matches(loginRegex)) {
                    throw new IncorrectDataException(("Invalid login. Allowed characters are: "  +
                            loginRegex + ". \n" +
                            "Please try again."));
                }
                return login;
            } catch (IncorrectDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String askPassword() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Please enter your password: ");
            String password = scanner.nextLine().trim();
            try {
                if (!password.matches(passwordRegex)) {
                    throw new IncorrectDataException("""
                        Invalid password. Your password should contain at least:\
                        
                        One special character: !@#$%^&* \
                        
                        One lowercase english letter: a-z \
                        
                        One uppercase english letter: A-Z \
                        
                        One digit: 0-9 \
                        
                        Password length should be 8 symbols""");
                }
                return password;
            } catch (IncorrectDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static SystemUserRoles askRole() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Please enter your role: ");
            System.out.println(Arrays.toString(SystemUserRoles.values()));
            String role = scanner.nextLine().trim();
            try {
                return SystemUserRoles.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
