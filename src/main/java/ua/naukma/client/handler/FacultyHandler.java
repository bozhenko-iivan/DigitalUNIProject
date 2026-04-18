package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.domain.Department;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.Teacher;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FacultyHandler extends BasicHandler{
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
            case 4 -> System.out.println("This method is currently not implemented yet.");
            case 5 -> set_dean();
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
}
