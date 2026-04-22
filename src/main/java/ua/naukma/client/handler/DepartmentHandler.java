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
import java.util.function.Predicate;

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
            case 13 -> show_department_info();
            case 14 -> show_dept_students();
            case 15 -> show_dept_students_by_name();
            case 16 -> find_dept_students_by_specific_course();
            default -> System.out.println("Invalid choice");
        }
    }

    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.DEPS);
        menuContext.setCurrent_department(null);
    }

    private void show_department_info() {
        Response showDepartmentInfo = sendRequest(Request.RequestType.FIND_DEP, menuContext.getCurrent_department().getId(), false);
        if (showDepartmentInfo.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Department dep = (Department) showDepartmentInfo.getPayload();
            System.out.println(dep.findDepartment());
        }
    }

    private void add_teacher() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_STRUCTURE)) {
            try {
//                int teacherId = IdVerificator.ask_id();
//
//                if (isIdAlreadyTaken(teacherId, Request.RequestType.FIND)) {
//                    System.out.println("This ID is already taken by another teacher in the system. Please choose a UNIQUE ID.");
//                    return;
//                }

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

                Teacher teacher = new Teacher(0, firstName, lastName, middleName, dob,
                        email, phoneNumber, position, degree, rank, hiringDate, load, menuContext.getCurrent_department());

                Request addRequest = new Request(Request.RequestType.ADD, teacher, menuContext.getCurrent_level());
                oos.writeObject(addRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                System.out.println(response.getMsg());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
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
            int teacherId = IdVerificator.ask_id();
            Response r = sendRequest(Request.RequestType.FIND, teacherId, false);
            Teacher t = (Teacher) r.getPayload();
            if (t.getDepartment() != null && t.getDepartment().getId() == menuContext.getCurrent_department().getId()) {
                r = sendRequest(Request.RequestType.REMOVE, teacherId, false);
                if (r.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                    System.out.println("Teacher removed.");
                } else {
                    System.out.println("No teacher found.");
                }
            }
        } else {
            System.out.println("Access Denied: You cannot remove teacher.");
        }
    }

    private void find_teacher() {

        int teacherId = IdVerificator.ask_id();

        Response response = sendRequest(Request.RequestType.FIND, teacherId, false);
        if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Teacher t = (Teacher) response.getPayload();
            if (isChild.test(t)) {
                System.out.println(t);
            }
        } else {
            System.out.println("No teacher found.");
        }
    }

    private void show_all_teachers() {
        try {
            Request showAllTeachersRequest = new Request(Request.RequestType.GET_ALL, menuContext.getCurrent_department().getId(),
                    menuContext.getCurrent_level());
            oos.writeObject(showAllTeachersRequest);
            oos.flush();

            Response response = (Response) ois.readObject();

            if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("All teachers found");
                List<Teacher> list = (List<Teacher>) response.getPayload();
                list = list.stream().filter(isChild).toList();
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
            teachers = teachers.stream().filter(isChild).toList();
            teachers.forEach(teacher -> System.out.printf("%-35s | ID: %d%n", teacher.getName(), teacher.getId()));
        }
    }

    private void sort_by_name() {
        int departmentId = menuContext.getCurrent_department().getId();
        Response res = sendRequest(Request.RequestType.SORT_DEPT_TEACHERS_BY_NAME, departmentId, false);
        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Teacher> teachers = (List<Teacher>) res.getPayload();
            teachers = teachers.stream().filter(isChild).toList();
            teachers.forEach(teacher -> System.out.printf("%-35s | ID: %d%n", teacher.getName(), teacher.getId()));
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
            teachers = teachers.stream().filter(isChild).toList();
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

            if (!isIdAlreadyTaken(teacherId, Request.RequestType.FIND)) {
                System.out.println("Teacher with such id does not exist.");
                return;
            }

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

            if (!isIdAlreadyTaken(teacherId, Request.RequestType.FIND)) {
                System.out.println("Teacher with such id does not exist.");
                return;
            }

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

    private void show_dept_students() {
        int deptId = menuContext.getCurrent_department().getId();
        Response res = sendRequest(Request.RequestType.GET_DEPT_STUDENTS, deptId, false, MenuLevel.DEPARTAMENT);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) res.getPayload();
            if (students.isEmpty()) {
                System.out.println("No students found in this department.");
                return;
            }
            System.out.println("Students of " + menuContext.getCurrent_department().getName() + ":");
            students.forEach(s -> System.out.printf("Course %d | %-30s | Group: %s%n",
                    s.getCourse(), s.getName(), s.getGroup().getName()));
        }
    }

    private void show_dept_students_by_name() {
        int deptId = menuContext.getCurrent_department().getId();
        Response res = sendRequest(Request.RequestType.GET_DEPT_STUDENTS_BY_NAME, deptId, false, MenuLevel.STUDENT);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) res.getPayload();

            if (students.isEmpty()) {
                System.out.println("No students found in this department.");
                return;
            }

            System.out.println("🎓 Students of " + menuContext.getCurrent_department().getName() + " (Sorted by Name):");
            students.forEach(s -> System.out.printf("Course %d | %-40s | Group: %s%n",
                    s.getCourse(), s.getName(), s.getGroup().getName()));
        }
    }

    private void find_dept_students_by_specific_course() {
        int course = AcademicInfoVerificator.ask_course();

        System.out.print("Sort by alphabet? (y/n): ");
        String sortChoice = new java.util.Scanner(System.in).nextLine().trim().toLowerCase();

        Response res = sendRequest(Request.RequestType.FIND_STUDENTS_BY_COURSE, course, false, MenuLevel.STUDENT);
        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> list = (List<Student>) res.getPayload();

            if (list.isEmpty()) {
                System.out.println("No students found on this course.");
                return;
            }

            list = new java.util.ArrayList<>(list.stream().filter(isStudentChild).toList());
            if (list.isEmpty()) {
                System.out.println("No students found on course " + course + " in this department.");
                return;
            }

            if (sortChoice.equals("y")) {
                java.text.Collator collator = java.text.Collator.getInstance(new java.util.Locale("uk", "UA"));
                list.sort((s1, s2) -> collator.compare(s1.getName(), s2.getName()));
                System.out.println("Students of Course " + course + " (Sorted by Name):");
            } else {
                System.out.println("Students of Course " + course + " (Unsorted):");
            }

            list.forEach(s -> System.out.printf("Course %d | %-35s | Group: %s%n",
                    s.getCourse(), s.getName(), s.getGroup().getName()));
        }
    }

    private final Predicate<Teacher> isChild = t ->
            t.getDepartment() != null &&
                    t.getDepartment().getId() == menuContext.getCurrent_department().getId();


    private final Predicate<Student> isStudentChild = s ->
            s.getGroup() != null &&
                    s.getGroup().getDepartment() != null &&
                    s.getGroup().getDepartment().getId() == menuContext.getCurrent_department().getId();
}
