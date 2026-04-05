package ua.naukma.client.utils;

import ua.naukma.domain.Grade;
import ua.naukma.domain.Subject;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GradeVerificator {
    public static int askGradeID(List<Grade> grades) {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.print("Enter grade ID from transcript: ");
            try {
                int inputId = scanner.nextInt();
                boolean exists = grades.stream()
                        .anyMatch(g -> g.getId() == inputId);
                if (exists) {
                    return inputId;
                } else {
                    System.out.println("Error: Grade ID " + inputId + " not found in this transcript.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric ID.");
                scanner.nextLine();
            }
        }
    }
    public static int askScore() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.print("Please enter your score: (1 - 100): ");
            try {
                int score = scanner.nextInt();
                if (score < 1 || score > 100) {
                    System.out.println("Invalid score. Score must be between 1 and 100.");
                    continue;
                }
                return score;
            } catch (InputMismatchException e) {
                System.out.println("Invalid score. Score must be between 1 and 100.");
                scanner.nextLine();
            }
        }
    }

    public static Subject askSubject() {
        Scanner scanner = InitScanner.try_init_scanner();
        Subject[] subjects = Subject.values();
        int i = 1;
        for (Subject subject : subjects) {
            System.out.println(i + " " + subject);
            i++;
        }
        while (true) {
            System.out.print("Please enter your subject: (number or string name): ");
            String input = scanner.nextLine();
            if (input.matches("\\d+")) {
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > subjects.length) {
                    System.out.println("Invalid subjects. Please enter a number between 1 and " + subjects.length + " or subject name");
                    continue;
                }
                return subjects[choice - 1];
            }
            else {
                String normalizedInput = input.trim().toUpperCase();
                if (normalizedInput.isEmpty()) {
                    continue;
                }
                try {
                    return Subject.valueOf(normalizedInput);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid subjects. Please enter a number between 1 and " + subjects.length + " or subject name");
                }
            }
        }
    }
}
