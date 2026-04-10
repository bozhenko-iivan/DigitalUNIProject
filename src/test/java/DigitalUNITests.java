import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.exception.IncorrectDataException;
import ua.naukma.network.Request;
import ua.naukma.server.controller.EntityController;
import ua.naukma.server.repository.FilePersonRepository;
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
    private Faculty testFaculty = new Faculty( 1236567, "FFi", "ffi", null ,"ffi@gmail.com", new University(1234567, "NaUKMA", "KMA", "Kyiv", "address, 2"));

    private University testUniversity;
    private UniversityService universityService;

    @BeforeEach
    void setUp() {
        studentRepository = new FilePersonRepository<>(Student.class);
        teacherRepository = new FilePersonRepository<>(Teacher.class);
        testDepartment = new Department(1111111, "TestDept", testFaculty, null, "TestLoc", "test@ukma.edu.ua");
        testGroup = new Group(1234567, "IPZ-2025", testFaculty, 1, 2025);
        studentService = new StudentService(studentRepository, Student.class);
        teacherService = new TeacherService(teacherRepository, Teacher.class);
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
        Student student = new Student(1234456, "Максим", "Ткаченко", "Ігорович",
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
    void testEntityControllerAddsStudent(){
        EntityController<Student, StudentService> entityController = new EntityController<>(studentService);
        Request r = new Request(Request.RequestType.ADD, new Student(1234456, "Максим", "Ткаченко", "Ігорович",
                LocalDate.of(2005, 10, 10),
                "max@ukma.edu.ua", "+380981231234",
                "11112222", 1, testGroup, 2024,
                StudyForm.BUDGET, StudentStatus.STUDYING));
        entityController.process(r);
    }
    @Test
    void testEntityControllerRemovesStudent(){
        EntityController<Student, StudentService> entityController = new EntityController<>(studentService);
        Request r = new Request(Request.RequestType.REMOVE, 1234456);
        entityController.process(r);
    }
}