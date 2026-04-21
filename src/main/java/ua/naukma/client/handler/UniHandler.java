package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.EmailVerificator;
import ua.naukma.client.utils.FacilityNameVerificator;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.domain.Faculty;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

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
            default -> System.out.println("Invalid choice");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.MON);
        menuContext.setCurrent_university(null);
    }
    private void add_faculty() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int facultyId = IdVerificator.ask_id();

            if (isIdAlreadyTaken(facultyId, Request.RequestType.FIND)) {
                System.out.println("This id is already taken. Please try choose another id");
                return;
            }

            String facultyFullName = FacilityNameVerificator.ask_facility_name();
            String facultyShortName = FacilityNameVerificator.ask_short_name();
            String facultyEmail = EmailVerificator.ask_email();

            Faculty facultyToAdd = new Faculty(facultyId, facultyFullName, facultyShortName, null, facultyEmail, menuContext.getCurrent_university());

            sendRequest(Request.RequestType.ADD, facultyToAdd, false);
        });
    }
    private void remove_faculty() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int facultyId = IdVerificator.ask_id();

            sendRequest(Request.RequestType.REMOVE, facultyId, false);
        });
    }
    private void find_faculty() {
        int facultyId = IdVerificator.ask_id();

        Response findFacultyResponse = sendRequest(Request.RequestType.FIND, facultyId, false);

        if (findFacultyResponse != null && findFacultyResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            menuContext.setCurrent_faculty((Faculty) findFacultyResponse.getPayload());
            menuContext.setCurrent_level(MenuLevel.FAC);
        }
    }
    private void show_all_faculties() {
        Response getAllRes = sendRequest(Request.RequestType.GET_ALL, null, false);
        if (getAllRes != null && getAllRes.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Faculty> faculties = (List<Faculty>) getAllRes.getPayload();
            faculties.forEach(System.out::println);
        }
    }

    private void sort_by_ids() {
        Response sortByIdResponse = sendRequest(Request.RequestType.SORT_BY_ID, null, false);
        if (sortByIdResponse != null && sortByIdResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Faculty> faculties = (List<Faculty>) sortByIdResponse.getPayload();
            faculties.forEach(fac -> System.out.printf("%-25s | ID: %d%n", fac.getName(), fac.getId()));
        }
    }

    private void sort_by_name() {
        Response sortByNameResponse = sendRequest(Request.RequestType.SORT_BY_ALPHABETIC_NAME, null, false);
        if (sortByNameResponse != null && sortByNameResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Faculty> faculties = (List<Faculty>) sortByNameResponse.getPayload();
            faculties.forEach(fac -> System.out.printf("%-25s%n", fac.getName()));
        }
    }
}
