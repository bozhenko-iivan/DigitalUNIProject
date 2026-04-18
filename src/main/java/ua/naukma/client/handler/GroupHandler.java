package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.*;
import ua.naukma.domain.Student;
import ua.naukma.domain.StudentStatus;
import ua.naukma.domain.StudyForm;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;
import ua.naukma.server.service.StudentService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.List;

public class GroupHandler extends BasicHandler {
    private StudentService studentService;
    public GroupHandler(MenuContext menuContext, ObjectInputStream in, ObjectOutputStream out) {
        super(menuContext, out, in);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> go_higher();
            case 2 -> add_student();
            case 3 -> remove_student();
            case 4 -> find_student();
            case 5 -> show_all_students();
            default -> System.out.println("Invalid choice");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.GRPS);
        menuContext.setCurrent_group(null);
    }
    private void add_student() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int studentId =  IdVerificator.ask_id();

            if (isIdAlreadyTaken(studentId, Request.RequestType.FIND)) {
                System.out.println("This id is already taken. Please try choose another id");
                return;
            }

            boolean alphabet = PersonInfoVerificator.ask_alphabet();
            String firstName = PersonInfoVerificator.ask_name("first name", alphabet);
            String lastName = PersonInfoVerificator.ask_name("last name", alphabet);
            String middleName = PersonInfoVerificator.ask_name("middle name", alphabet);
            LocalDate dob = PersonInfoVerificator.ask_dob();
            String email = EmailVerificator.ask_email();
            String phoneNumber = PhoneNumberVerificator.ask_phonenum();
            StudyForm studyForm = AcademicInfoVerificator.ask_study_form();
            StudentStatus status = AcademicInfoVerificator.ask_student_status();
            String recordBookNumber = studentService.generateRecordbookNum(lastName, studentId, LocalDate.now().getYear());

            Student student = new Student(studentId, firstName, lastName, middleName,dob,
                    email, phoneNumber, recordBookNumber
                    ,menuContext.getCurrent_group().getCourse(), menuContext.getCurrent_group(),menuContext.getCurrent_group().getAdmissionYear(), studyForm, status);

            sendRequest(Request.RequestType.ADD_STUDENT, student, false);
        });
    }
    private void remove_student() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int studentId = IdVerificator.ask_id();
            sendRequest(Request.RequestType.REMOVE, studentId, false);
        });
    }
    private void find_student() {
        int studentId = IdVerificator.ask_id();
        Response findStudentRes = sendRequest(Request.RequestType.FIND_STUDENT_BY_ID, studentId, false);
        if (findStudentRes != null && findStudentRes.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            System.out.println(findStudentRes.getPayload());
            menuContext.setCurrent_student((Student) findStudentRes.getPayload());
            menuContext.setCurrent_level(MenuLevel.STUDENT);
        }
    }
    private void show_all_students() {
        Response getAll = sendRequest(Request.RequestType.GET_ALL_STUDENTS, null, false);
        if (getAll.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> list = (List<Student>) getAll.getPayload();
            list.forEach((student) -> {
                System.out.println(student.toStringShort());
            });
        }
    }
}
