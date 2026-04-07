package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.EmailVerificator;
import ua.naukma.client.utils.FacilityNameVerificator;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.client.utils.UniversityVerificator;
import ua.naukma.domain.Department;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

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
            default -> System.out.println("Invalid choice");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.FAC);
    }
    private void add_dep() {
        requirePermission(Permissions.MANAGE_STRUCTURE, () -> {
            int departmentId = IdVerificator.ask_id();

            if (isIdAlreadyTaken(departmentId, Request.RequestType.FIND)) {
                System.out.println("This id is already taken. Please try choose another id");
                return;
            }

            String departmentName = FacilityNameVerificator.ask_facility_name();
            String email = EmailVerificator.ask_email();
            String location = UniversityVerificator.ask_address();

            Department dep = new Department(departmentId, departmentName, menuContext.getCurrent_faculty(),
                    null, location, email);

            sendRequest(Request.RequestType.ADD, dep, false);
        });
    }
    private void remove_dep() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_STRUCTURE)) {
            try {
                int departmentId = IdVerificator.ask_id();

                Request removeRequest = new Request(Request.RequestType.REMOVE, departmentId, menuContext.getCurrent_level());
                oos.writeObject(removeRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                System.out.println(response.getMsg());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Access Denied: You cannot remove department.");
        }
    }
    private void find_dep() {
        try {
            int departmentId = IdVerificator.ask_id();

            Request findRequest = new Request(Request.RequestType.FIND, departmentId, menuContext.getCurrent_level());
            oos.writeObject(findRequest);
            oos.flush();

            Response response = (Response) ois.readObject();

            if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("Department found");
                Department department = (Department) response.getPayload();
                System.out.println(department);
                menuContext.setCurrent_department(department);
                menuContext.setCurrent_level(MenuLevel.DEPARTAMENT);
            } else {
                System.out.println(response.getMsg());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
                list.forEach(System.out::println);
            } else {
                System.out.println(response.getMsg());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
