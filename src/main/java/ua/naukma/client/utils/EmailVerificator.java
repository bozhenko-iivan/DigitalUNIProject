package ua.naukma.client.utils;

import java.util.Scanner;

public class EmailVerificator {
    public static String ask_email() {
        String error_message = """
                Invalid email.
                Examples of a valid one:
                ivanLastivka@gmail.com
                or   ivanLastivka@ukr.net
                """;
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            String email;
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (email.matches("^[a-zA-Z0-9.-]+@(gmail\\.com|ukr\\.net)$")) {
                return email;
            } else {
                System.out.println(error_message);
            }
        }
    }
}
