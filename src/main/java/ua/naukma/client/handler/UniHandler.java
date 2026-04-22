package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.AcademicInfoVerificator;
import ua.naukma.client.utils.EmailVerificator;
import ua.naukma.client.utils.FacilityNameVerificator;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.Group;
import ua.naukma.domain.Teacher;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;
import ua.naukma.domain.Student;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class UniHandler extends BasicHandler {
    public UniHandler(MenuContext context, ObjectOutputStream oos, ObjectInputStream ois) {
        super(context, oos, ois);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> go_higher();
            case 2 -> add_faculty();
            case 3 -> remove_faculty();
            case 4 -> find_faculty();
            case 5 -> show_all_faculties();
            case 6 -> sort_by_ids();
            case 7 -> sort_by_name();
            case 8 -> find_group_by_name();
            case 9 -> show_all_uni_students();
            case 10 -> show_uni_students_by_course();
            case 11 -> find_student_by_course();
            case 12 -> find_teacher_by_pib();
            case 13 -> find_student_by_pib();
            default -> System.out.println("Invalid choice");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.MON);
        menuContext.setCurrent_university(null);
    }
    private void add_faculty() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
//            int facultyId = IdVerificator.ask_id();
//
//            if (isIdAlreadyTaken(facultyId, Request.RequestType.FIND)) {
//                System.out.println("This ID is already taken by another faculty in the system. Please choose a UNIQUE ID.");
//                return;
//            }

            String facultyFullName = FacilityNameVerificator.ask_facility_name();
            String facultyShortName = FacilityNameVerificator.ask_short_name();
            String facultyEmail = EmailVerificator.ask_email();

            Faculty facultyToAdd = new Faculty(0, facultyFullName, facultyShortName, null, facultyEmail, menuContext.getCurrent_university());

            sendRequest(Request.RequestType.ADD, facultyToAdd, false);
        });
    }
    private void remove_faculty() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int facultyId = IdVerificator.ask_id();
            Response findFacultyResponse = sendRequest(Request.RequestType.FIND, facultyId, false);
            Faculty f = (Faculty) findFacultyResponse.getPayload();
            if(f.getUniversity() != null && f.getUniversity().getId() == menuContext.getCurrent_university().getId())
                sendRequest(Request.RequestType.REMOVE, facultyId, false);
        });
    }
    private void find_faculty() {
        int facultyId = IdVerificator.ask_id();

        Response findFacultyResponse = sendRequest(Request.RequestType.FIND, facultyId, false);

        if (findFacultyResponse != null && findFacultyResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Faculty f = (Faculty) findFacultyResponse.getPayload();
            if(f.getUniversity() != null && f.getUniversity().getId() == menuContext.getCurrent_university().getId()) {
                menuContext.setCurrent_faculty((Faculty) findFacultyResponse.getPayload());
                menuContext.setCurrent_level(MenuLevel.FAC);
            }
        }

    }
    private void show_all_faculties() {
        Response getAllRes = sendRequest(Request.RequestType.GET_ALL, null, false);
        if (getAllRes != null && getAllRes.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Faculty> faculties = (List<Faculty>) getAllRes.getPayload();
            faculties = faculties.stream().filter(isChild).toList();
            faculties.forEach(System.out::println);
        }
    }

    private void sort_by_ids() {
        Response sortByIdResponse = sendRequest(Request.RequestType.SORT_BY_ID, null, false);
        if (sortByIdResponse != null && sortByIdResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Faculty> faculties = (List<Faculty>) sortByIdResponse.getPayload();
            faculties = faculties.stream().filter(isChild).toList();
            faculties.forEach(fac -> System.out.printf("%-25s | ID: %d%n", fac.getName(), fac.getId()));
        }
    }

    private void sort_by_name() {
        Response sortByNameResponse = sendRequest(Request.RequestType.SORT_BY_ALPHABETIC_NAME, null, false);
        if (sortByNameResponse != null && sortByNameResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Faculty> faculties = (List<Faculty>) sortByNameResponse.getPayload();
            faculties = faculties.stream().filter(isChild).toList();
            faculties.forEach(fac -> System.out.printf("%-25s%n", fac.getName()));
        }
    }

    private void find_group_by_name() {
        System.out.print("Enter group name (e.g., IPZ-1): ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine().trim();

        Response res = sendRequest(Request.RequestType.FIND_GROUP_BY_NAME, name, false, MenuLevel.MON);
        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Group group = (Group) res.getPayload();
            System.out.println("✅ Group found!");
            System.out.println(group);

//             menuContext.setCurrent_faculty(group.getFaculty());
//             menuContext.setCurrent_department(group.getDepartment());
//             menuContext.setCurrent_group(group);
//             menuContext.setCurrent_level(MenuLevel.GROUP);
        }
    }

    private void show_all_uni_students() {
        int uniId = menuContext.getCurrent_university().getId();
        Response res = sendRequest(Request.RequestType.GET_UNI_STUDENTS, uniId, false, MenuLevel.STUDENT);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) res.getPayload();
            if (students.isEmpty()) {
                System.out.println("No students found in this university yet.");
                return;
            }
            System.out.println("All Students of " + menuContext.getCurrent_university().getShortName() + ":");
            students.forEach(s -> {
                String deptName = s.getGroup().getDepartment() != null ? s.getGroup().getDepartment().getName() : "No Dept";
                System.out.printf("Course %d | %-30s | Group: %-8s | Dept: %s%n",
                        s.getCourse(), s.getName(), s.getGroup().getName(), deptName);
            });
        }
    }

    private void show_uni_students_by_course() {
        int uniId = menuContext.getCurrent_university().getId();

        Response res = sendRequest(Request.RequestType.GET_UNI_STUDENTS_BY_COURSE, uniId, false, MenuLevel.STUDENT);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) res.getPayload();
            if (students.isEmpty()) {
                System.out.println("No students found in this university.");
                return;
            }
            System.out.println("Students of " + menuContext.getCurrent_university().getShortName() + " (Sorted by Course):");
            students.forEach(s -> {
                String deptName = s.getGroup().getDepartment() != null ? s.getGroup().getDepartment().getName() : "No Dept";
                System.out.printf("Course %d | %-40s | Group: %-8s | Dept: %s%n",
                        s.getCourse(), s.getName(), s.getGroup().getName(), deptName);
            });
        }
    }

    private void find_student_by_course() {
        int course = AcademicInfoVerificator.ask_course();
        int uniId = menuContext.getCurrent_university().getId();
        int[] payload = new int[]{uniId, course};
        Response res = sendRequest(Request.RequestType.FIND_UNI_STUDENTS_BY_COURSE, payload, false, MenuLevel.MON);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) res.getPayload();

            if (students.isEmpty()) {
                System.out.println("No students found on course " + course + " in this university.");
                return;
            }

            System.out.println("Students of " + menuContext.getCurrent_university().getShortName() + " (Course " + course + "):");
            students.forEach(s -> {
                String deptName = s.getGroup().getDepartment() != null ? s.getGroup().getDepartment().getName() : "No Dept";
                System.out.printf("Course %d | %-40s | Group: %-8s | Dept: %s%n",
                        s.getCourse(), s.getName(), s.getGroup().getName(), deptName);
            });
        }
    }

    private void find_teacher_by_pib() {
        boolean alphabet = ua.naukma.client.utils.PersonInfoVerificator.ask_alphabet();
        String[] pib = {
                ua.naukma.client.utils.PersonInfoVerificator.ask_name("last name", alphabet),
                ua.naukma.client.utils.PersonInfoVerificator.ask_name("first name", alphabet),
                ua.naukma.client.utils.PersonInfoVerificator.ask_name("middle name", alphabet)
        };

        Response res = sendRequest(Request.RequestType.FIND_TEACHER_BY_PIB, pib, false, MenuLevel.DEPARTAMENT);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Teacher> teachers = (List<Teacher>) res.getPayload();
            int currentUniId = menuContext.getCurrent_university().getId();

            List<Teacher> uniTeachers = teachers.stream()
                    .filter(t -> t.getDepartment() != null &&
                            t.getDepartment().getFaculty() != null &&
                            t.getDepartment().getFaculty().getUniversity() != null &&
                            t.getDepartment().getFaculty().getUniversity().getId() == currentUniId)
                    .toList();

            if (uniTeachers.isEmpty()) {
                System.out.println("No teachers found with this PIB in the current university.");
            } else {
                System.out.println("Found teachers in " + menuContext.getCurrent_university().getShortName() + ":");
                uniTeachers.forEach(t -> System.out.printf("%-35s | Dept: %-25s | Position: %s%n",
                        t.getName(), t.getDepartment().getName(), t.getPosition()));

            }
        } else {
            System.out.println("Teachers not found.");
        }
    }

    private void find_student_by_pib() {
        boolean alphabet = ua.naukma.client.utils.PersonInfoVerificator.ask_alphabet();
        String[] pib = {
                ua.naukma.client.utils.PersonInfoVerificator.ask_name("last name", alphabet),
                ua.naukma.client.utils.PersonInfoVerificator.ask_name("first name", alphabet),
                ua.naukma.client.utils.PersonInfoVerificator.ask_name("middle name", alphabet)
        };

        Response res = sendRequest(Request.RequestType.FIND_STUDENT_BY_PIB, pib, false, MenuLevel.STUDENT);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) res.getPayload();

            int currentUniId = menuContext.getCurrent_university().getId();

            List<Student> uniStudents = students.stream()
                    .filter(s -> s.getGroup() != null &&
                            s.getGroup().getFaculty() != null &&
                            s.getGroup().getFaculty().getUniversity() != null &&
                            s.getGroup().getFaculty().getUniversity().getId() == currentUniId)
                    .toList();

            if (uniStudents.isEmpty()) {
                System.out.println("No students found with this PIB in the current university.");
            } else {
                System.out.println("Found students in " + menuContext.getCurrent_university().getShortName() + ":");
                uniStudents.forEach(s -> {
                    String deptName = s.getGroup().getDepartment() != null ? s.getGroup().getDepartment().getName() : "No Dept";
                    System.out.printf("Course %d | %-35s | Group: %-8s | Dept: %s%n",
                            s.getCourse(), s.getName(), s.getGroup().getName(), deptName);
                });
            }
        } else {
            System.out.println("Students not found.");
        }
    }

    private final Predicate<Faculty> isChild = f -> f.getUniversity() != null && f.getUniversity().getId() == menuContext.getCurrent_university().getId();
}
