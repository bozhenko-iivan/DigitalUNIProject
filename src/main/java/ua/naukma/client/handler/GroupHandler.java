package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.*;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;
import ua.naukma.server.service.StudentService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

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
            case 6 -> sort_by_id();
            case 7 -> sort_by_name();
            case 8 -> find_student_by_pib();
            case 9 -> upgrade_course();
            default -> System.out.println("Invalid choice");
        }
    }

    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.GRPS);
        menuContext.setCurrent_group(null);
    }

    private void add_student() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
//            int studentId =  IdVerificator.ask_id();
//
//            if (isIdAlreadyTaken(studentId, Request.RequestType.FIND)) {
//                System.out.println("This ID is already taken by another student in the system. Please choose a UNIQUE ID.");
//                return;
//            }

            boolean alphabet = PersonInfoVerificator.ask_alphabet();
            String firstName = PersonInfoVerificator.ask_name("first name", alphabet);
            String lastName = PersonInfoVerificator.ask_name("last name", alphabet);
            String middleName = PersonInfoVerificator.ask_name("middle name", alphabet);
            LocalDate dob = PersonInfoVerificator.ask_dob();
            String email = EmailVerificator.ask_email();
            String phoneNumber = PhoneNumberVerificator.ask_phonenum();
            StudyForm studyForm = AcademicInfoVerificator.ask_study_form();
            StudentStatus status = AcademicInfoVerificator.ask_student_status();

            String recordBookNumber = "Б-" + LocalDate.now().getYear() + "-" + (int)(Math.random() * 9999);

//            Student student = new Student(0, firstName, lastName, middleName,dob,
//                    email, phoneNumber, recordBookNumber
//                    ,menuContext.getCurrent_group().getCourse(), menuContext.getCurrent_group(),menuContext.getCurrent_group().getAdmissionYear(), studyForm, status);

            Student student = new Student(0, firstName, lastName, middleName, dob,
                    email, phoneNumber, recordBookNumber,
                    menuContext.getCurrent_group(),
                    studyForm, status);

            sendRequest(Request.RequestType.ADD_STUDENT, student, false);
        });
    }

    private void remove_student() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int studentId = IdVerificator.ask_id();
            Response findStudentResponse = sendRequest(Request.RequestType.FIND_STUDENT_BY_ID, studentId, false);
            Student s = (Student) findStudentResponse.getPayload();
            if(isChild.test(s))
                sendRequest(Request.RequestType.REMOVE, studentId, false);
        });
    }

    private void find_student() {
        int studentId = IdVerificator.ask_id();
        Response findStudentRes = sendRequest(Request.RequestType.FIND_STUDENT_BY_ID, studentId, false);

        if (findStudentRes != null && findStudentRes.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Student student = (Student) findStudentRes.getPayload();

            if (student.getGroup().getId() != menuContext.getCurrent_group().getId()) {
                System.out.println("This student does not belong to group " + menuContext.getCurrent_group().getName() +
                        ". They are currently in: " + student.getGroup().getName());
                return;
            }

            System.out.println(student);
            menuContext.setCurrent_student(student);
            menuContext.setCurrent_level(MenuLevel.STUDENT);
        }
    }

    private void find_student_by_pib() {
        boolean alphabet = PersonInfoVerificator.ask_alphabet();
        String[] pib = {
                PersonInfoVerificator.ask_name("last name", alphabet),
                PersonInfoVerificator.ask_name("first name", alphabet),
                PersonInfoVerificator.ask_name("middle name", alphabet)
        };

        Response res = sendRequest(Request.RequestType.FIND_STUDENT_BY_PIB, pib, false);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) res.getPayload();

            int currentGroupId = menuContext.getCurrent_group().getId();
            List<Student> studentsInThisGroup = students.stream()
                    .filter(s -> s.getGroup().getId() == currentGroupId)
                    .toList();

            if (studentsInThisGroup.isEmpty()) {
                System.out.println("Student with this name not found in the current group.");
            } else if (studentsInThisGroup.size() == 1) {
                menuContext.setCurrent_student(studentsInThisGroup.get(0));
                menuContext.setCurrent_level(MenuLevel.STUDENT);
                System.out.println("Student found and selected.");
            }
        }
    }

    private void show_all_students() {
        int groupId = menuContext.getCurrent_group().getId();
        Response getAll = sendRequest(Request.RequestType.GET_ALL_STUDENTS, groupId, false);
        if (getAll != null && getAll.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> list = (List<Student>) getAll.getPayload();
            list.stream().filter(isChild).forEach((student) -> {
                System.out.println(student.toStringShort());
            });
        }
    }

    private void sort_by_id() {
        int groupId = menuContext.getCurrent_group().getId();
        Response sortByIdResponse = sendRequest(Request.RequestType.SORT_BY_ID, groupId, false);
        if (sortByIdResponse != null && sortByIdResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) sortByIdResponse.getPayload();
            students.forEach(student -> System.out.printf("%-35s | ID: %d%n", student.getName(), student.getId()));
        }
    }

    private void sort_by_name(){
        int groupId = menuContext.getCurrent_group().getId();
        Response sortByNameResponse = sendRequest(Request.RequestType.SORT_BY_ALPHABETIC_NAME, groupId, false);
        if (sortByNameResponse != null && sortByNameResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) sortByNameResponse.getPayload();
            students.forEach(student -> System.out.printf("%-35s%n", student.getName()));
        }
    }
    private void upgrade_course() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int groupId = menuContext.getCurrent_group().getId();

            System.out.print("Are you sure you want to move this group and ALL its students to the next course? (y/n): ");
            String confirmation = new java.util.Scanner(System.in).nextLine().trim().toLowerCase();

            if (confirmation.equals("y")) {
                Response response = sendRequest(Request.RequestType.UPGRADE_GROUP_COURSE, groupId, false);

                if (response != null && response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                    System.out.println("Group and all its students successfully moved to the next course!");
                    menuContext.setCurrent_group((Group) response.getPayload());
                }
            } else {
                System.out.println("Operation cancelled.");
            }
        });
    }
    private final Predicate<Student> isChild = s -> s.getGroup() != null && s.getGroup().getId() == menuContext.getCurrent_group().getId();
}