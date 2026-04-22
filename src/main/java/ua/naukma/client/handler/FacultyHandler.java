package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.AcademicInfoVerificator;
import ua.naukma.client.utils.EmailVerificator;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.domain.Department;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.Student;
import ua.naukma.domain.Teacher;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.network.dto.UpdateFacultyDTO;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FacultyHandler extends BasicHandler {
    public FacultyHandler(MenuContext menuContext, ObjectOutputStream out, ObjectInputStream in) {
        super(menuContext, out, in);
    }

    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> go_higher();
            case 2 -> go_deps();
            case 3 -> go_groups();
            case 4 -> show_fac_info();
            case 5 -> set_dean();
            case 6 -> edit_faculty();
            case 7 -> fac_students_by_name();
            case 8 -> fac_students_by_course();
            case 9 -> fac_teachers_by_name();
            case 10 -> find_students_by_specific_course();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.UNI);
        menuContext.setCurrent_faculty(null);
    }

    private void set_dean() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_STRUCTURE)) {
            try {
                int teacherId = IdVerificator.ask_id();
                int facultyId = menuContext.getCurrent_faculty().getId();
                int[] payload = new int[]{facultyId, teacherId};

                Request setHeadRequest = new Request(Request.RequestType.SET_DEAN, payload, menuContext.getCurrent_level());
                oos.writeObject(setHeadRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                    Faculty f = menuContext.getCurrent_faculty();
                    Teacher teacher = (Teacher) response.getPayload();
                    f.setDean(teacher);
                    System.out.println("Dean of faculty has been successfully set!");
                } else {
                    System.out.println("Failed to set head: " + response.getMsg());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Access Denied: You cannot set dean of Faculty.");
        }
    }

    private void go_deps() {
        menuContext.setCurrent_level(MenuLevel.DEPS);
    }

    private void go_groups() {
        menuContext.setCurrent_level(MenuLevel.GRPS);
    }

    private void show_fac_info() {
        Response showFacultyInfo = sendRequest(Request.RequestType.FIND, menuContext.getCurrent_faculty().getId(), false, menuContext.getCurrent_level());
        if (showFacultyInfo.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Faculty faculty = (Faculty) showFacultyInfo.getPayload();
            System.out.println(faculty.toString());
        }
    }

    private synchronized void edit_faculty() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int facId = menuContext.getCurrent_faculty().getId();
            String newEmail = EmailVerificator.ask_email();
            UpdateFacultyDTO dto = new UpdateFacultyDTO(facId, newEmail);
            Response res = sendRequest(Request.RequestType.UPDATE_FACULTY, dto, false, menuContext.getCurrent_level());
            if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("Faculty info updated!");
                menuContext.setCurrent_faculty((Faculty) res.getPayload());
            }
        });
    }

    private void fac_students_by_name() {
        int facId = menuContext.getCurrent_faculty().getId();
        Response res = sendRequest(Request.RequestType.GET_FAC_STUDENTS_BY_NAME, facId, false, MenuLevel.STUDENT);
        printStudentsList(res);
    }

    private void fac_students_by_course() {
        int facId = menuContext.getCurrent_faculty().getId();
        Response res = sendRequest(Request.RequestType.GET_FAC_STUDENTS_BY_COURSE, facId, false, MenuLevel.STUDENT);
        printStudentsList(res);
    }

    private void find_students_by_specific_course() {
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
            list = new ArrayList<>(list.stream().filter(isChild).toList());
            if(list.isEmpty()) {
                System.out.println("No students found on this course.");
                return;
            }
            if (sortChoice.equals("y")) {
                java.text.Collator collator = java.text.Collator.getInstance(new java.util.Locale("uk", "UA"));
                list.sort((s1, s2) -> collator.compare(s1.getName(), s2.getName()));
                System.out.println("Students of Course " + course + " (Sorted by Name)");
            } else {
                System.out.println("Students of Course " + course + " (Unsorted)");
            }

            list.forEach(s -> System.out.printf("Course %d | %-35s | Group: %s%n",
                    s.getCourse(), s.getName(), s.getGroup().getName()));
        }
    }

    private void fac_teachers_by_name() {
        int facId = menuContext.getCurrent_faculty().getId();
        Response res = sendRequest(Request.RequestType.GET_FAC_TEACHERS_BY_NAME, facId, false, MenuLevel.DEPARTAMENT);

        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Teacher> list = (List<Teacher>) res.getPayload();
            list = list.stream().filter(isTeacherChild).toList();
            System.out.println("Teachers of Faculty");
            if (list.isEmpty()) System.out.println("No teachers found.");
            list.forEach(t -> System.out.printf("%-35s | Dept: %s%n", t.getName(), t.getDepartment().getName()));
        }
    }

    private void printStudentsList(Response res) {
        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Student> list = (List<Student>) res.getPayload();
            list = list.stream().filter(isChild).toList();
            if (list.isEmpty()) {
                System.out.println("No students found.");
                return;
            }
            list.forEach(s -> System.out.printf("Course %d | %-35s | Group: %s%n",
                    s.getCourse(), s.getName(), s.getGroup().getName()));
        } else {
            System.out.println("Failed to load students list.");
        }
    }
    private final Predicate<Student> isChild = s -> s.getGroup().getFaculty() != null && s.getGroup().getFaculty().getId() == menuContext.getCurrent_faculty().getId();
    private final Predicate<Teacher> isTeacherChild = t -> t.getDepartment().getFaculty() != null && t.getDepartment().getFaculty().getId() == menuContext.getCurrent_faculty().getId();
}