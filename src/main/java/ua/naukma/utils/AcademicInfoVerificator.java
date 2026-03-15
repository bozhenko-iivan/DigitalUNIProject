package ua.naukma.utils;

import ua.naukma.domain.StudyForm;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AcademicInfoVerificator {
    public static int ask_admission_year() {
        Scanner scanner = InitScanner.try_init_scanner();
        int year = 0;
        while (true) {
            System.out.println("Enter the year of admission: ");
            try {
                year = scanner.nextInt();
                if (year > LocalDate.now().getYear() || year < 1994) {
                    System.out.println("Impossible year.");
                } else {
                    return year;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    public static int ask_course() {
        Scanner scanner = InitScanner.try_init_scanner();
        int course = 0;
        while (course > 6 || course < 1) {
            System.out.println("Enter course (1-6): ");
            try {
                course = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return course;
    }

    public static StudyForm ask_study_form() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Enter study form (Бюджет/Контракт) або (1/2): ");
            String s = scanner.nextLine().trim();
            switch (s) {
                case "Бюджет":
                case "1":
                    return StudyForm.BUDGET;
                case "Контракт":
                case "2":
                    return StudyForm.CONTRACT;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }
}