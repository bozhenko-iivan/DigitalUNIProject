package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.AcademicInfoVerificator;
import ua.naukma.client.utils.FacilityNameVerificator;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.Group;
import ua.naukma.domain.University;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.function.Predicate;

public class GrpsHandler extends BasicHandler{
    public GrpsHandler(MenuContext menuContext, ObjectInputStream in, ObjectOutputStream out) {
        super(menuContext, out, in);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> go_higher();
            case 2 -> add_group();
            case 3 -> remove_group();
            case 4 -> find_group();
            case 5 -> show_all_groups();
            case 6 -> sort_by_id();
            case 7 -> sort_by_name();
            default -> System.out.println("Invalid choice.");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.FAC);
    }
    private void add_group() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int groupToAddId = IdVerificator.ask_id();

            if (isIdAlreadyTaken(groupToAddId, Request.RequestType.FIND)) {
                System.out.println("This id is already taken. Please try choose another id");
                return;
            }

            String groupName = FacilityNameVerificator.ask_group_name();
            Faculty faculty = menuContext.getCurrent_faculty();
            int admissionYear = AcademicInfoVerificator.ask_admission_year();
            int course = AcademicInfoVerificator.ask_course();

            Group group = new Group(groupToAddId, groupName, faculty, course, admissionYear);

            sendRequest(Request.RequestType.ADD, group, false);
        });
    }
    private void remove_group() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int groupToRemoveId = IdVerificator.ask_id();
            Response findGroupRes = sendRequest(Request.RequestType.FIND, groupToRemoveId, false);
            Group g = (Group) findGroupRes.getPayload();
            if(g.getFaculty() != null && g.getFaculty().getId() == menuContext.getCurrent_faculty().getId())
                 sendRequest(Request.RequestType.REMOVE, groupToRemoveId, false);
        });
    }
    private void find_group() {
        int groupId = IdVerificator.ask_id();
        Response findGroupRes = sendRequest(Request.RequestType.FIND, groupId, false);
        Group g = (Group) findGroupRes.getPayload();
        if (findGroupRes.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            if (g.getFaculty() != null && g.getFaculty().getId() == menuContext.getCurrent_faculty().getId()) {
                menuContext.setCurrent_group((Group) findGroupRes.getPayload());
                menuContext.setCurrent_level(MenuLevel.GROUP);
            }
        }
    }
    private void show_all_groups() {
        try {
            Request showAllGroupsRequest = new Request(Request.RequestType.GET_ALL, menuContext.getCurrent_faculty().getId(), menuContext.getCurrent_level());
            oos.writeObject(showAllGroupsRequest);
            oos.flush();

            Response response = (Response) ois.readObject();
            if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("All groups found");
                @SuppressWarnings("unchecked")
                List<Group> list = (List<Group>) response.getPayload();
                list.stream().filter(isChild).forEach(System.out::println);
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
            List<Group> groups = (List<Group>) sortByIdResponse.getPayload();
            groups.stream().filter(isChild).forEach(gr -> System.out.printf("%-15s | ID: %d%n", gr.getName(), gr.getId()));
        }
    }

    private void sort_by_name(){
        Response sortByNameResponse = sendRequest(Request.RequestType.SORT_BY_ALPHABETIC_NAME, null, false);
        if (sortByNameResponse != null && sortByNameResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Group> groups = (List<Group>) sortByNameResponse.getPayload();
            groups.stream().filter(isChild).forEach(gr -> System.out.printf("%-15s%n", gr.getName()));
        }
    }
    private final Predicate<Group> isChild = g -> g.getFaculty() != null && g.getFaculty().getId() == menuContext.getCurrent_faculty().getId();
}
