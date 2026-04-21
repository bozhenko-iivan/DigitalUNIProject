package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.*;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.network.dto.UpdateDepartmentDTO;
import ua.naukma.network.dto.UpdateTeacherAcademicDTO;
import ua.naukma.network.dto.UpdateTeacherContactsDTO;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.List;

public class DepartmentHandler extends BasicHandler {
    public DepartmentHandler(MenuContext context, ObjectOutputStream oos, ObjectInputStream ois) {
        super(context, oos, ois);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> go_higher();
            case 2 -> add_teacher();
            case 3 -> remove_teacher();
            case 4 -> find_teacher();
            case 5 -> show_all_teachers();
            case 6 -> sort_by_id();
            case 7 -> sort_by_name();
            case 8 -> set_head();
            case 9 -> find_teacher_by_pib();
            case 10 -> update_teacher_contacts();
            case 11 -> update_teacher_academic();
            case 12 -> edit_department();
            default -> System.out.println("Invalid choice");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.DEPS);
        menuContext.setCurrent_department(null);
    }
    private void add_teacher() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_STRUCTURE)) {
            try {
                int teacherId = IdVerificator.ask_id();

                if (isIdAlreadyTaken(teacherId, Request.RequestType.FIND)) {
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
                TeacherPosition position = AcademicInfoVerificator.ask_teacher_position();
                TeacherDegree degree = AcademicInfoVerificator.ask_teacher_degree();
                TeacherRank rank = AcademicInfoVerificator.ask_teacher_rank();
                LocalDate hiringDate = AcademicInfoVerificator.ask_hiring_date(dob);
                double load = AcademicInfoVerificator.ask_load();

                Teacher teacher = new Teacher(teacherId, firstName, lastName, middleName, dob,
                        email, phoneNumber, position, degree, rank, hiringDate, load, menuContext.getCurrent_department());

                Request addRequest = new Request(Request.RequestType.ADD, teacher, menuContext.getCurrent_level());
                oos.writeObject(addRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                System.out.println(response.getMsg());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else     {
            System.out.println("Access Denied: You cannot add teacher.");
        }
    }
    private void set_head() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_STRUCTURE)) {
            try {
                int teacherId = IdVerificator.ask_id();

                int departmentId = menuContext.getCurrent_department().getId();

                int[] payload = new int[]{departmentId, teacherId};

                Request setHeadRequest = new Request(Request.RequestType.SET_HEAD, payload, menuContext.getCurrent_level());
                oos.writeObject(setHeadRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                    Department d = menuContext.getCurrent_department();
                    Teacher teacher = (Teacher) response.getPayload();
                    d.setHead(teacher);
                    System.out.println("Head of department successfully set!");
                } else {
                    System.out.println("Failed to set head: " + response.getMsg());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Access Denied: You cannot set head of department.");
        }
    }

    private void remove_teacher() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_STRUCTURE)) {
            try {
                int teacherId = IdVerificator.ask_id();

                Request removeRequest = new Request(Request.RequestType.REMOVE, teacherId, menuContext.getCurrent_level());
                oos.writeObject(removeRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                System.out.println(response.getMsg());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Access Denied: You cannot remove teacher.");
        }
    }
    private void find_teacher() {
        try {
            int teacherId = IdVerificator.ask_id();

            Request findRequest = new Request(Request.RequestType.FIND, teacherId, menuContext.getCurrent_level());
            oos.writeObject(findRequest);
            oos.flush();

            Response response = (Response) ois.readObject();

            if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("Teacher found");
                Teacher teacher = (Teacher) response.getPayload();
                System.out.println(teacher);
            } else {
                System.out.println(response.getMsg());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void show_all_teachers() {
        try {
            Request showAllTeachersRequest = new Request(Request.RequestType.GET_ALL, menuContext.getCurrent_department().getId(),
                    menuContext.getCurrent_level());
            oos.writeObject(showAllTeachersRequest);
            oos.flush();

            Response response = (Response) ois.readObject();

            if  (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("All teachers found");
                List<Teacher> list = (List<Teacher>) response.getPayload();
                list.forEach(System.out::println);
            } else {
                System.out.println(response.getMsg());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sort_by_id() {
        Response sortByIdResponse = sendRequest(Request.RequestType.SORT_BY_ID, null, false);
        if (sortByIdResponse != null && sortByIdResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Teacher> teachers = (List<Teacher>) sortByIdResponse.getPayload();
            teachers.forEach(teacher -> System.out.printf("%-25s | ID: %d%n", teacher.getName(), teacher.getId()));
        }
    }

    private void sort_by_name() {
        int departmentId = menuContext.getCurrent_department().getId();
        Response res = sendRequest(Request.RequestType.SORT_DEPT_TEACHERS_BY_NAME, departmentId, false);
        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Teacher> teachers = (List<Teacher>) res.getPayload();
            teachers.forEach(teacher -> System.out.printf("%-20s | ID: %d%n", teacher.getName(), teacher.getId()));
        }
    }

    private void find_teacher_by_pib() {
        boolean alphabet = PersonInfoVerificator.ask_alphabet();
        String[] pib = {
                PersonInfoVerificator.ask_name("last name", alphabet),
                PersonInfoVerificator.ask_name("first name", alphabet),
                PersonInfoVerificator.ask_name("middle name", alphabet)
        };
        Response res = sendRequest(Request.RequestType.FIND_TEACHER_BY_PIB, pib, false);
        if (res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Teacher> teachers = (List<Teacher>) res.getPayload();
            if (teachers.isEmpty()) {
                System.out.println("Teachers not found.");
            } else {
                teachers.forEach(System.out::println);
            }
        }
    }

    private void update_teacher_contacts() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int teacherId = IdVerificator.ask_id();
            String phoneNumber = PhoneNumberVerificator.ask_phonenum();
            String email = EmailVerificator.ask_email();

            UpdateTeacherContactsDTO payload = new UpdateTeacherContactsDTO(teacherId, phoneNumber, email);
            Response response = sendRequest(Request.RequestType.UPDATE_TEACHER_CONTACTS, payload, false);

            if (response != null && response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("Teacher contacts updated successfully.");
            }
        });
    }

    private void update_teacher_academic() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int teacherId = IdVerificator.ask_id();
            TeacherPosition pos = AcademicInfoVerificator.ask_teacher_position();
            TeacherDegree deg = AcademicInfoVerificator.ask_teacher_degree();
            TeacherRank rank = AcademicInfoVerificator.ask_teacher_rank();
            double load = AcademicInfoVerificator.ask_load();

            UpdateTeacherAcademicDTO payload = new UpdateTeacherAcademicDTO(teacherId, pos, deg, rank, load);
            sendRequest(Request.RequestType.UPDATE_TEACHER_ACADEMIC, payload, false);
            System.out.println("Teacher academic info updated.");
        });
    }

    private void edit_department() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int deptId = menuContext.getCurrent_department().getId();

            System.out.print("Enter new location: ");
            String newLocation = UniversityVerificator.ask_address();
            String newEmail = EmailVerificator.ask_email();

            UpdateDepartmentDTO dto = new UpdateDepartmentDTO(deptId, newLocation, newEmail);
            Response res = sendRequest(Request.RequestType.UPDATE_DEPARTMENT, dto, false);

            if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("Department info updated!");
                menuContext.setCurrent_department((Department) res.getPayload());
            }
        });
    }
}
