package ua.naukma.client.handler;

import com.google.gson.internal.bind.util.ISO8601Utils;
import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.client.utils.UniversityVerificator;
import ua.naukma.domain.University;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MONHandler extends BasicHandler{
    public MONHandler(ObjectInputStream in, ObjectOutputStream out, MenuContext menuContext) {
        super(menuContext, out, in);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> exit();
            case 2 -> add_uni();
            case 3 -> remove_uni();
            case 4 -> find_uni();
            case 5 -> show_all_uni();
            case 6 -> sort_by_id();
            case 7 -> sort_by_name();
            case 8 -> admin_panel();
            case 9 -> log_out();
            default -> System.out.println("Wrong choice");
        }
    }
    private void exit(){
        System.out.println("Exiting..");
        System.exit(0);
    }
    private void add_uni(){
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int university_id = IdVerificator.ask_id();

            if (isIdAlreadyTaken(university_id, Request.RequestType.FIND)) {
                System.out.println("Id is already taken");
                return;
            }

            String fullName = UniversityVerificator.ask_full_name("fullName");
            String shortName = UniversityVerificator.ask_short_name("shortName");
            String city = UniversityVerificator.ask_city();
            String address = UniversityVerificator.ask_address();

            University newUni = new University(university_id, fullName, shortName, city, address);

            sendRequest(Request.RequestType.ADD, newUni, false);
        });
    }
    private void remove_uni(){
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int university_id = IdVerificator.ask_id();

            sendRequest(Request.RequestType.REMOVE, university_id, false);
        });
    }
    private void find_uni(){
        int university_id = IdVerificator.ask_id();
        Response res = sendRequest(Request.RequestType.FIND, university_id, false);
        if (res != null && res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            menuContext.setCurrent_university((University) res.getPayload());
            menuContext.setCurrent_level(MenuLevel.UNI);
        }
    }
    private void show_all_uni(){
        Response getAllRes = sendRequest(Request.RequestType.GET_ALL, null, false);
        if (getAllRes != null && getAllRes.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<University> universities = (List<University>) getAllRes.getPayload();
            universities.forEach(System.out::println);
        }
    }
    private void admin_panel(){
        requirePermission(Permissions.MANAGE_USERS,
                () -> { menuContext.setCurrent_level(MenuLevel.ADMIN_PANEL);
                });
    }
    private void log_out(){
        Response logoutResponse = sendRequest(Request.RequestType.LOGOUT, null, false);
        if (logoutResponse != null && logoutResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            menuContext.setCurrent_level(MenuLevel.LOGIN);
        }
    }

    private void sort_by_id() {
        Response sortByIdResponse = sendRequest(Request.RequestType.SORT_BY_ID, null, false);
        if (sortByIdResponse != null && sortByIdResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<University> universities = (List<University>) sortByIdResponse.getPayload();
            universities.forEach(uni -> System.out.printf("%-15s | ID: %d%n", uni.getName(), uni.getId()));
        }
    }

    private void sort_by_name(){
        Response sortByNameResponse = sendRequest(Request.RequestType.SORT_BY_ALPHABETIC_NAME, null, false);
        if (sortByNameResponse != null && sortByNameResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<University> universities = (List<University>) sortByNameResponse.getPayload();
            universities.forEach(university -> System.out.printf("%-15s%n", university.getName()));
        }
    }
}
