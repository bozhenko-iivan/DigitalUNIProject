package ua.naukma.client.utils;

import ua.naukma.domain.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
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

    public static StudentStatus ask_student_status() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Enter student status: (Навчається/АкадемВідпустка/Вибус) або (1/2/3): ");
            String s = scanner.nextLine().trim();
            switch (s) {
                case "Навчається":
                case "1":
                    return StudentStatus.STUDYING;
                case "АкадемВідпустка":
                case "2":
                    return StudentStatus.ACADEMIC_LEAVE;
                case "Вибус":
                case "3":
                    return StudentStatus.EXPELLED;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static TeacherPosition ask_teacher_position() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Enter teacher position: (1/2/3/4/5): ");
            System.out.println(Arrays.toString(TeacherPosition.values()));
            int teacherPosition = scanner.nextInt();
            switch (teacherPosition) {
                case 1: return TeacherPosition.ASSISTANT;
                case 2: return TeacherPosition.LECTURER;
                case 3: return TeacherPosition.SENIOR_LECTURER;
                case 4: return TeacherPosition.HEAD_OF_DEPARTMENT;
                case 5: return TeacherPosition.DEAN;
                default: System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static TeacherDegree ask_teacher_degree() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Enter teacher degree: (1/2/3): ");
            System.out.println(Arrays.toString(TeacherDegree.values()));
            int teacherDegree = scanner.nextInt();
            switch (teacherDegree) {
                case 1: return TeacherDegree.NONE;
                case 2: return TeacherDegree.PHD;
                case 3: return TeacherDegree.DOCTOR_OF_SCIENCES;
                default: System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static TeacherRank ask_teacher_rank() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Enter teacher rank: (1/2/3): ");
            System.out.println(Arrays.toString(TeacherRank.values()));
            int teacherRank = scanner.nextInt();
            switch (teacherRank) {
                case 1: return TeacherRank.DOCENT;
                case 2: return TeacherRank.PROFESSOR;
                case 3: return TeacherRank.SENIOR_RESEARCHER;
                default: System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static LocalDate ask_hiring_date(LocalDate dob) {
        Scanner scanner = InitScanner.try_init_scanner();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        while (true) {
            System.out.println("Enter hire date (DD.MM.YYYY): ");
            String s = scanner.nextLine().trim();
            try {
                LocalDate hiringDate = LocalDate.parse(s, formatter);
                if (hiringDate.isAfter(LocalDate.now())) {
                    System.out.println("Invalid input. Can't use future date");
                    continue;
                }
                if (hiringDate.isBefore(dob.plusYears(21))) {
                    System.out.println("Teacher must be at least 21 years old");
                    continue;
                }
                return hiringDate;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid input. Please use format (DD.MM.YYYY)");
            }
        }
    }

    public static double ask_load() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Please enter a load value: (0.8 || 4.5): ");
            double load = scanner.nextDouble();
            if (load < 0.8 || load > 4.5) {
                System.out.println("Invalid input. Please enter a valid load value (0.8 || 4.5): ");
                continue;
            }
            return load;
        }
    }
}