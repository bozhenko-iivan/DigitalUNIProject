package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.*;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
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
}
