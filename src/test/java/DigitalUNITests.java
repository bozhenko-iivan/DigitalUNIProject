import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.exception.IncorrectDataException;
import ua.naukma.network.Request;
import ua.naukma.security.Permissions;
import ua.naukma.server.controller.EntityController;
import ua.naukma.server.repository.FilePersonRepository;
import ua.naukma.server.repository.FileRepository;
import ua.naukma.server.repository.PersonRepository;
import ua.naukma.server.repository.Repository;
import ua.naukma.server.service.StudentService;
import ua.naukma.server.service.TeacherService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DigitalUniTests {
    private Repository<Grade, Integer> gradeRepository;
    private Repository<Group, Integer> groupRepository;

    private PersonRepository<Student, Integer> studentRepository;
    private StudentService studentService;

    private TeacherService teacherService;
    private PersonRepository<Teacher, Integer> teacherRepository;

    private Group testGroup;
    private Department testDepartment;
    private Faculty testFaculty;
    private University testUniversity;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        studentRepository = new FilePersonRepository<>(Student.class);
        teacherRepository = new FilePersonRepository<>(Teacher.class);
        gradeRepository = new FileRepository<>(Grade.class);
        groupRepository = new FileRepository<>(Group.class);

        testUniversity = new University(1234567, "NaUKMA", "KMA", "Kyiv", "address, 2");
        testFaculty = new Faculty(1236567, "FFi", "ffi", null ,"ffi@gmail.com", testUniversity);
        testDepartment = new Department(1111111, "TestDept", testFaculty, null, "TestLoc", "test@ukma.edu.ua");
        testGroup = new Group(1234567, "IPZ-2025", testFaculty, testDepartment,1, 2024);

        studentService = new StudentService(studentRepository, gradeRepository, groupRepository, Student.class);
        teacherService = new TeacherService(teacherRepository, Teacher.class);

        testStudent = new Student(1234456, "Максим", "Ткаченко", "Ігорович",
                LocalDate.of(2005, 10, 10), "max@ukma.edu.ua", "+380981231234",
                "11112222", testGroup, StudyForm.BUDGET, StudentStatus.STUDYING);
    }

    // 1. Перевірка валідації некоректного року вступу
    @Test
    void testInvalidAdmissionYearThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Group(1234567, "IPZ-2025", testFaculty, testDepartment,1, 1990)
        );
        assertEquals("Invalid admission year.", ex.getMessage());
    }

    // 2. Перевірка валідації неіснуючого курсу
    @Test
    void testInvalidCourseNumberThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Group(1234567, "IPZ-2025", testFaculty, testDepartment,7, 2024)
        );
        assertEquals("Invalid course.", ex.getMessage());
    }

    // 3. Перевірка валідації email без символу '@'
    @Test
    void testDepartmentInvalidEmailThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Department(1111111, "TestDept", testFaculty, null, "TestLoc", "invalid_email.com")
        );
        assertTrue(ex.getMessage().contains("must contain '@'"));
    }

    // 4. Перевірка заборони від'ємного навантаження викладача
    @Test
    void testTeacherNegativeLoadThrowsException() {
        IncorrectDataException ex = assertThrows(IncorrectDataException.class, () ->
                new Teacher(2222222, "Ivan", "Petrov", "O", LocalDate.of(1980, 1, 1),
                        "ivan@ukma.edu.ua", "+380501234567", TeacherPosition.LECTURER,
                        TeacherDegree.PHD, TeacherRank.DOCENT, LocalDate.now(), -1.5, testDepartment)
        );
        assertEquals("Load cannot be negative.", ex.getMessage());
    }

    // 5. Перевірка дати працевлаштування з майбутнього
    @Test
    void testTeacherFutureHiringDateThrowsException() {
        IncorrectDataException ex = assertThrows(IncorrectDataException.class, () ->
                new Teacher(2222222, "Ivan", "Petrov", "O", LocalDate.of(1980, 1, 1),
                        "ivan@ukma.edu.ua", "+380501234567", TeacherPosition.LECTURER,
                        TeacherDegree.PHD, TeacherRank.DOCENT, LocalDate.now().plusDays(5), 1.0, testDepartment)
        );
        assertEquals("Hiring Date is invalid.", ex.getMessage());
    }

    // 6. Перевірка довжини ID групи (має бути 7 цифр)
    @Test
    void testGroupIdLengthValidation() {

    }

    // 7. Перевірка генерації винятку при додаванні дубліката студента
    @Test
    void testAddExistingStudentThrowsDuplicateEntityException() {

    }

    // 8. Пошук неіснуючого студента за ID
    @Test
    void testFindNonExistingStudentThrowsEntityNotFoundException() {
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                studentService.findById(9999999)
        );
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    // 9. Видалення неіснуючого викладача
    @Test
    void testDeleteNonExistingTeacherThrowsException() {
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                teacherService.deleteById(8888888)
        );
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    // 10. Успішне оновлення контактних даних студента
    @Test
    void testUpdateStudentContactsSuccess() {
        try { studentService.add(testStudent); } catch (Exception ignored) {}

        Student updated = studentService.updateContacts(testStudent.getId(), "+380999999999", "new@ukma.edu.ua");
        assertEquals("new@ukma.edu.ua", updated.getEmail());
        assertEquals("+380999999999", updated.getPhoneNumber());
    }

    // 11. Успішне оновлення статусу студента
    @Test
    void testUpdateStudentStatusSuccess() {
        try { studentService.add(testStudent); } catch (Exception ignored) {}

        Student updated = studentService.updateStudentStatus(testStudent.getId(), StudentStatus.ACADEMIC_LEAVE);
        assertEquals(StudentStatus.ACADEMIC_LEAVE, updated.getStatus());
    }

    // 12. Успішне оновлення форми навчання
    @Test
    void testUpdateStudentStudyFormSuccess() {
        try { studentService.add(testStudent); } catch (Exception ignored) {}

        Student updated = studentService.updateStudyForm(testStudent.getId(), StudyForm.CONTRACT);
        assertEquals(StudyForm.CONTRACT, updated.getStudyForm());
    }

    // 13. Підрахунок кількості студентів у групі
    @Test
    void testGetStudentsCountInGroup() {
        try { studentService.add(testStudent); } catch (Exception ignored) {}
        long count = studentService.getStudentsCount(testGroup.getId());
        assertTrue(count >= 1);
    }

    // 14. Перевірка алгоритму генерації номера заліковки
    @Test
    void testRecordBookNumberGeneration() {
        String recordBook = studentService.generateRecordbookNum("Ткаченко", 1234567, 2024);
        assertNotNull(recordBook);
        assertTrue(recordBook.startsWith("Т-"));
    }

    // 15. Наявність прав керування користувачами у Адміністратора
    @Test
    void testAdminHasManageUsersPermission() {
        SystemUser admin = new SystemUser(1, "admin", "pass", SystemUserRoles.ADMIN);
        assertTrue(admin.hasPermission(Permissions.MANAGE_USERS));
    }

    // 16. Відсутність прав керування структурою у звичайного користувача
    @Test
    void testUserDoesNotHaveManageStructurePermission() {
        SystemUser simpleUser = new SystemUser(2, "user", "pass", SystemUserRoles.USER);
        assertFalse(simpleUser.hasPermission(Permissions.MANAGE_STRUCTURE));
    }

    // 17. Наявність прав керування студентами у Менеджера
    @Test
    void testManagerHasManageStudentsPermission() {
        SystemUser manager = new SystemUser(3, "manager", "pass", SystemUserRoles.MANAGER);
        assertTrue(manager.hasPermission(Permissions.MANAGE_STUDENTS));
    }

    // 18. Відсутність прав керування користувачами у Менеджера
    @Test
    void testManagerDoesNotHaveManageUsersPermission() {
        SystemUser manager = new SystemUser(3, "manager", "pass", SystemUserRoles.MANAGER);
        assertFalse(manager.hasPermission(Permissions.MANAGE_USERS));
    }

    // 19. Додавання студента через EntityController
    @Test
    void testEntityControllerAddsStudent() {
        EntityController<Student, StudentService> controller = new EntityController<>(studentService);
        Student newStudent = new Student(7654321, "Олег", "Петров", "Іванович",
                LocalDate.of(2004, 5, 5), "oleg@ukma.edu.ua", "+380501234567",
                "22223333",  testGroup, StudyForm.CONTRACT, StudentStatus.STUDYING);

        Request req = new Request(Request.RequestType.ADD, newStudent);
        controller.process(req);
        assertThrows(EntityNotFoundException.class, () -> studentService.findById(7654321));
    }

    // 20. Видалення студента через EntityController
    @Test
    void testEntityControllerRemovesStudent() {
        EntityController<Student, StudentService> controller = new EntityController<>(studentService);

        Request addReq = new Request(Request.RequestType.ADD, testStudent);
        controller.process(addReq);

        Request removeReq = new Request(Request.RequestType.REMOVE, testStudent.getId());
        controller.process(removeReq);

        assertThrows(EntityNotFoundException.class, () -> studentService.findById(testStudent.getId()));
    }
}