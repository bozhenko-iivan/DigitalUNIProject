package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.EmailVerificator;
import ua.naukma.client.utils.FacilityNameVerificator;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.client.utils.UniversityVerificator;
import ua.naukma.domain.Department;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.Group;
import ua.naukma.domain.Teacher;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.function.Predicate;

public class DepsHandler extends BasicHandler {
    public DepsHandler(MenuContext menuContext, ObjectInputStream in, ObjectOutputStream out) {
        super(menuContext, out, in);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> go_higher();
            case 2 -> add_dep();
            case 3 -> remove_dep();
            case 4 -> find_dep();
            case 5 -> show_all();
            case 6 -> sort_by_id();
            case 7 -> sort_by_name();
            default -> System.out.println("Invalid choice");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.FAC);
    }
    private void add_dep() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
//            int departmentId = IdVerificator.ask_id();
//
//            if (isIdAlreadyTaken(departmentId, Request.RequestType.FIND)) {
//                System.out.println("This ID is already taken by another department in the system. Please choose a UNIQUE ID.");
//                return;
//            }

            String departmentName = FacilityNameVerificator.ask_facility_name();
            String email = EmailVerificator.ask_email();
            String location = UniversityVerificator.ask_address();
            //Teacher head =

            Department dep = new Department(0
                    , departmentName, menuContext.getCurrent_faculty(),
                    null, location, email);

            sendRequest(Request.RequestType.ADD, dep, false);
        });
    }
    private void remove_dep() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_STRUCTURE)) {
                int departmentId = IdVerificator.ask_id();
                Response response = sendRequest(Request.RequestType.FIND, departmentId, false);
                Department f = (Department) response.getPayload();
                if(isChild.test(f)) {
                    response = sendRequest(Request.RequestType.REMOVE, departmentId, false);
                    if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        System.out.println("Department removed");
                    } else {
                        System.out.println(response.getMsg());
                    }
                }

        } else {
            System.out.println("Access Denied: You cannot remove department.");
        }
    }
    private void find_dep() {

        int departmentId = IdVerificator.ask_id();

        Response response = sendRequest(Request.RequestType.FIND, departmentId, false);
        Department f = (Department) response.getPayload();
        if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            if( f.getFaculty() != null && f.getFaculty().getId() == menuContext.getCurrent_faculty().getId()) {
                System.out.println("Department found");
                Department department = (Department) response.getPayload();
                System.out.println(department);
                menuContext.setCurrent_department(department);
                menuContext.setCurrent_level(MenuLevel.DEPARTAMENT);
            }else {
                System.out.println("No department found");
            }
        } else {
            System.out.println(response.getMsg());
        }
    }
    private void show_all() {
        try {
            Request showAllDepartmentsRequest = new Request(Request.RequestType.GET_ALL,
                    menuContext.getCurrent_faculty().getId(), menuContext.getCurrent_level());
            oos.writeObject(showAllDepartmentsRequest);
            oos.flush();

            Response response = (Response) ois.readObject();

            if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("All departments found");
                @SuppressWarnings("unchecked")
                List<Department> list = (List<Department>) response.getPayload();
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
            List<Department> deps = (List<Department>) sortByIdResponse.getPayload();
            deps = deps.stream().filter(isChild).toList();
            deps.forEach(dep -> System.out.printf("%-15s | ID: %d%n", dep.getName(), dep.getId()));
        }
    }

    private void sort_by_name(){
        Response sortByNameResponse = sendRequest(Request.RequestType.SORT_BY_ALPHABETIC_NAME, null, false);
        if (sortByNameResponse != null && sortByNameResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Department> deps = (List<Department>) sortByNameResponse.getPayload();
            deps = deps.stream().filter(isChild).toList();
            deps.forEach(dep -> System.out.printf("%-15s%n", dep.getName()));
        }
    }
    private final Predicate<Department> isChild = f -> f.getFaculty() != null && f.getFaculty().getId() == menuContext.getCurrent_faculty().getId();
}
