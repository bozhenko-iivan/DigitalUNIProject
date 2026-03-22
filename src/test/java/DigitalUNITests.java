import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.server.repository.InMemoryStudentRepository;
import ua.naukma.server.repository.PersonRepository;
import ua.naukma.server.service.StudentService;
import ua.naukma.server.service.UniversityService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUniTests {

    private PersonRepository<Student, Integer> studentRepository;
    private StudentService studentService;

    private Group testGroup;
    private Department testDepartment;
    private Faculty testFaculty;
    private University testUniversity;
    private UniversityService universityService;

    @BeforeEach
    void setUp() {
        studentRepository = new InMemoryStudentRepository();
        testDepartment = new Department(1111111, "TestDept", null, null, "TestLoc", "test@ukma.edu.ua");
        testGroup = new Group(1234567, "IPZ-2025", testFaculty, 1, 2025);
        studentService = new StudentService(studentRepository);
    }

    @Test
    void testBirthDateInFutureThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Student(1, "Іван", "Іваненко", "Іванович",
                    LocalDate.now().plusDays(5),
                    "test@ukma.edu.ua", "+380981231234",
                    "12345678", 1, testGroup, 2024,
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
                    "12345678", 1, testGroup, 2024,
                    StudyForm.BUDGET, StudentStatus.STUDYING);
        });

        assertEquals("Phone number must contain 13 digits. Example: +380981231234", exception.getMessage());
    }

    @Test
    void testInvalidEmailThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Student(3, "Олег", "Сидоренко", "Іванович",
                    LocalDate.of(2004, 3, 3),
                    "oleg_ukma.edu.ua", // Відсутня собачка '@'
                    "+380981231234",
                    "87654321", 1, testGroup, 2024,
                    StudyForm.CONTRACT, StudentStatus.STUDYING);
        });

        assertEquals("Email cannot be empty and must contain '@'.", exception.getMessage());
    }

    @Test
    void testInvalidAdmissionYearThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Group(1234567, "IPZ-2025", testFaculty, 1, 1990);
        });

        assertEquals("Invalid admission year.", exception.getMessage());
    }

    @Test
    void testInvalidCourseNumberThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Group(1234567, "IPZ-2025", testFaculty, 7, 2024);
        });

        assertEquals("Invalid course.", exception.getMessage());
    }

    @Test
    void testAddExistingStudentThrowsException() {
        Student student = new Student(5, "Максим", "Ткаченко", "Ігорович",
                LocalDate.of(2005, 10, 10),
                "max@ukma.edu.ua", "+380981231234",
                "11112222", 1, testGroup, 2024,
                StudyForm.BUDGET, StudentStatus.STUDYING);
        try {
            studentService.addStudent(student);
        } catch (DuplicateEntityException ignored) {}

        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> {
            studentService.addStudent(student);
        });
        assertTrue(exception.getMessage().contains("already exists"));

    }
}