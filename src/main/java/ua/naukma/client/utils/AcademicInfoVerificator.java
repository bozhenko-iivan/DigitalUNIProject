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
            System.out.print("Enter the year of admission: ");
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
            System.out.print("Enter course (1-6): ");
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
        System.out.print("Enter study form (Бюджет/Контракт) або (1/2): ");
        String s = scanner.nextLine().trim();
        while (true) {
            try {
                return switch (s) {
                    case "Бюджет", "1" -> StudyForm.BUDGET;
                    case "Контракт", "2" -> StudyForm.CONTRACT;
                    default -> null;
                };
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please a valid input.");
                scanner.next();
            }
        }
    }

    public static StudentStatus ask_student_status() {
        Scanner scanner = InitScanner.try_init_scanner();
        System.out.print("Enter student status: (Навчається/АкадемВідпустка/Вибув) або (1/2/3): ");
        String s = scanner.nextLine().trim();
        while (true) {
            try {
                return switch (s) {
                    case "Навчається", "1" -> StudentStatus.STUDYING;
                    case "АкадемВідпустка", "2" -> StudentStatus.ACADEMIC_LEAVE;
                    case "Вибув", "3" -> StudentStatus.EXPELLED;
                    default -> null;
                };
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please a valid input.");
                scanner.next();
            }
        }
    }

    public static TeacherPosition ask_teacher_position() {
        Scanner scanner = InitScanner.try_init_scanner();
        System.out.print("Enter teacher position: (1/2/3/4/5): ");
        System.out.println(Arrays.toString(TeacherPosition.values()));
        while (true) {
            try {
                int teacherPosition = scanner.nextInt();
                return switch (teacherPosition) {
                    case 1 -> TeacherPosition.ASSISTANT;
                    case 2 -> TeacherPosition.LECTURER;
                    case 3 -> TeacherPosition.SENIOR_LECTURER;
                    case 4 -> TeacherPosition.HEAD_OF_DEPARTMENT;
                    case 5 -> TeacherPosition.DEAN;
                    default -> null;
                };
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please a valid input.");
                scanner.next();
            }
        }
    }

    public static TeacherDegree ask_teacher_degree() {
        Scanner scanner = InitScanner.try_init_scanner();
        System.out.print("Enter teacher degree: (1/2/3): ");
        System.out.println(Arrays.toString(TeacherDegree.values()));
        while (true) {
            try {
                int teacherDegree = scanner.nextInt();
                return switch (teacherDegree) {
                    case 1 -> TeacherDegree.NONE;
                    case 2 -> TeacherDegree.PHD;
                    case 3 -> TeacherDegree.DOCTOR_OF_SCIENCES;
                    default -> null;
                };
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please a valid input.");
                scanner.next();
            }
        }
    }

    public static TeacherRank ask_teacher_rank() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.print("Enter teacher rank: (1/2/3): ");
            System.out.println(Arrays.toString(TeacherRank.values()));
            try {
                int teacherRank = scanner.nextInt();
                return switch (teacherRank) {
                    case 1 -> TeacherRank.DOCENT;
                    case 2 -> TeacherRank.PROFESSOR;
                    case 3 -> TeacherRank.SENIOR_RESEARCHER;
                    default -> null;
                };
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please a valid input.");
                scanner.next();
            }
        }
    }

    public static LocalDate ask_hiring_date(LocalDate dob) {
        Scanner scanner = InitScanner.try_init_scanner();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        while (true) {
            System.out.print("Enter hire date (DD.MM.YYYY): ");
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
                scanner.next();
            }
        }
    }

    public static double ask_load() {
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.print("Please enter a load value: (0.8 || 4.5): ");
            double load = scanner.nextDouble();
            if (load < 0.8 || load > 4.5) {
                System.out.println("Invalid input. Please enter a valid load value (0.8 || 4.5): ");
                continue;
            }
            return load;
        }
    }
}