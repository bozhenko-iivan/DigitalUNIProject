import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.IncorrectDataException;
import ua.naukma.server.repository.FileStudentRepository;
import ua.naukma.server.repository.FileTeacherRepository;
import ua.naukma.server.repository.PersonRepository;
import ua.naukma.server.service.StudentService;
import ua.naukma.server.service.TeacherService;
import ua.naukma.server.service.UniversityService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUniTests {

    private PersonRepository<Student, Integer> studentRepository;
    private StudentService studentService;

    private TeacherService teacherService;
    private PersonRepository<Teacher, Integer> teacherRepository;

    private Group testGroup;
    private Department testDepartment;
    private Faculty testFaculty;
    private University testUniversity;
    private UniversityService universityService;

    @BeforeEach
    void setUp() {
        studentRepository = new FileStudentRepository();
        teacherRepository = new FileTeacherRepository();
        testDepartment = new Department(1111111, "TestDept", null, null, "TestLoc", "test@ukma.edu.ua");
        testGroup = new Group(1234567, "IPZ-2025", testFaculty, 1, 2025);
        studentService = new StudentService(studentRepository);
        teacherService = new TeacherService(teacherRepository);
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
            studentService.add(student);
        } catch (DuplicateEntityException ignored) {}

        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> {
            studentService.add(student);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testDOB() {
        Teacher t = new Teacher(1111111, "Ivan", "Igorovych", "F",
                LocalDate.of(1490, 06, 12), "ivan1@gmail.com",
                "+380976607505", TeacherPosition.DEAN, TeacherDegree.DOCTOR_OF_SCIENCES, TeacherRank.DOCENT,
                LocalDate.of(1580, 11, 23), 4, testDepartment);
        try {
            teacherService.add(t);
        } catch (IncorrectDataException ignored) {}

        IncorrectDataException exception = assertThrows(IncorrectDataException.class, () -> {
            teacherService.add(t);
        });
        assertTrue(exception.getMessage().contains("too old"));
    }
}