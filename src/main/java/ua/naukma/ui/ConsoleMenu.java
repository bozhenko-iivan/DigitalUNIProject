package ua.naukma.ui;

import ua.naukma.domain.Student;
import ua.naukma.domain.StudentStatus;
import ua.naukma.domain.StudyForm;
import ua.naukma.service.StudentService;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleMenu {
    private final StudentService studentService;
    private final Scanner scanner;

    public ConsoleMenu(StudentService studentService) {
        this.studentService = studentService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n University of Kyiv-Mohila Academy DigitUNIProject menu");
            System.out.println("1. Add student");
            System.out.println("2. View student list");
            System.out.println("3. Find student by ID");
            System.out.println("0. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addStudentUI();
                case "2" -> showAllStudentsUI();
                case "3" -> findStudentByIdUI();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addStudentUI() {
        try {
            System.out.println("Add student");

            System.out.println("Student ID (number): ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.println("Last name: ");
            String lastName = scanner.nextLine();

            System.out.println("First name: ");
            String firstName = scanner.nextLine();

            System.out.println("Group (IPZ-2025): ");
            String group = scanner.nextLine();

            System.out.println("Course (1-6): ");
            int course = Integer.parseInt(scanner.nextLine());

            Student student = new Student(
                    id, firstName, lastName, "Unknown",
                    LocalDate.of(1613, 1, 1),
                    "email@test.com", "000",
                    "BOOK-" + id,
                    course, group, 2024,
                    StudyForm.BUDGET, StudentStatus.STUDYING
            );

            studentService.addStudent(student);
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error while adding: " + e.getMessage());
        }
    }

    private void showAllStudentsUI() {
        System.out.println("Student list");
        var students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(System.out::println);
        }
    }

    private void findStudentByIdUI() {
        System.out.println("Enter student ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            var student = studentService.findById(id);
            if (student.isPresent()) {
                System.out.println("Student found: " + student.get());
            } else  {
                System.out.println("Student with such ID not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a number.");
        }
    }
}