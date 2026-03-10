import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.naukma.domain.*;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.Repository;
import ua.naukma.service.StudentService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUniTests {

    private Repository<Student, Integer> repository;
    private StudentService studentService;

//    @BeforeEach
//    void setUp() {
//        repository = new InMemoryStudentRepository();
//        studentService = new StudentService(repository);
//    }

    @Test
    void testBirthDateInFutureThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Student(1, "Іван", "Іваненко", "Іванович",
                    LocalDate.now().plusDays(5), // Дата з майбутнього
                    "test@ukma.edu.ua", "0981231234",
                    "12345678", 1, "IPZ-2025", 2024,
                    StudyForm.BUDGET, StudentStatus.STUDYING);
        });

        assertEquals("Future fiction? Future dates not allowed.", exception.getMessage());
    }

    @Test
    void testInvalidPhoneNumberLengthThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Student(2, "Ганна", "Петренко", "Олегівна",
                    LocalDate.of(2005, 5, 5),
                    "test@ukma.edu.ua",
                    "12345", // Занадто короткий номер
                    "12345678", 1, "IPZ-2025", 2024,
                    StudyForm.BUDGET, StudentStatus.STUDYING);
        });

        assertEquals("Phone number must contain 10 digits. Example: 0981231234", exception.getMessage());
    }

    @Test
    void testInvalidEmailThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Student(3, "Олег", "Сидоренко", "Іванович",
                    LocalDate.of(2004, 3, 3),
                    "oleg_ukma.edu.ua", // Відсутня собачка '@'
                    "0981231234",
                    "87654321", 1, "IPZ-2025", 2024,
                    StudyForm.CONTRACT, StudentStatus.STUDYING);
        });

        assertEquals("Email cannot be empty and must contain '@'.", exception.getMessage());
    }

    @Test
    void testInvalidAdmissionYearThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Student(4, "Олексій", "Іванов", "Петрович",
                    LocalDate.of(2005, 1, 1),
                    "oleksiy@ukma.edu.ua", "0981231234",
                    "12345678", 1, "IPZ-2025",
                    1990, // Некоректний рік (меньше 1991)
                    StudyForm.BUDGET, StudentStatus.STUDYING);
        });

        assertEquals("Invalid admission year.", exception.getMessage());
    }

    @Test
    void testInvalidCourseNumberThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Student(5, "Олена", "Коваленко", "Сергіївна",
                    LocalDate.of(2004, 2, 2),
                    "olena@ukma.edu.ua", "0981231234",
                    "12345678",
                    7, // Некоректний курс (більше 6)
                    "IPZ-2025", 2024,
                    StudyForm.CONTRACT, StudentStatus.STUDYING);
        });

        assertEquals("Invalid course number. Only 1-6 courses exist.", exception.getMessage());
    }

    @Test
    void testAddExistingStudentThrowsException() {

        repository = new InMemoryStudentRepository();
        studentService = new StudentService(repository);

        Student student = new Student(5, "Максим", "Ткаченко", "Ігорович",
                LocalDate.of(2005, 10, 10),
                "max@ukma.edu.ua", "0981231234",
                "11112222", 1, "IPZ-2025", 2024,
                StudyForm.BUDGET, StudentStatus.STUDYING);

        studentService.addStudent(student);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            studentService.addStudent(student);
        });

        assertEquals("Student already exists", exception.getMessage());
    }
}