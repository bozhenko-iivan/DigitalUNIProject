package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.IdVerificator;
import ua.naukma.client.utils.ReadInt;
import ua.naukma.client.utils.SystemUserVerificator;
import ua.naukma.domain.SystemUser;
import ua.naukma.domain.SystemUserRoles;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class AdminPanelHandler extends BasicHandler {
    public AdminPanelHandler(MenuContext menuContext, ObjectOutputStream out,  ObjectInputStream in) {
        super(menuContext, out, in);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = ReadInt.readInt();
        switch (choice) {
            case 1 -> enter();
            case 2 -> add_user();
            case 3 -> remove_user();
            case 4 -> find_user();
            case 5 -> show_all_users();
            default -> System.out.println("Invalid choice");
        }
    }
    private void enter() {
        menuContext.setCurrent_level(MenuLevel.MON);
    }
    private void add_user() {
        if (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_USERS)) {
            try {
                int userId = IdVerificator.ask_id();

                if (isIdAlreadyTaken(userId, Request.RequestType.FIND_USER_BY_ID)) {
                    System.out.println("This id is already taken. Please try choose another id");
                    return;
                }

                String login = SystemUserVerificator.askLogin();
                String password = SystemUserVerificator.askPassword();
                SystemUserRoles role = SystemUserVerificator.askRole();

                SystemUser user = new SystemUser(userId, login, password, role);

                Request addRequest = new Request(Request.RequestType.ADD_USER, user, menuContext.getCurrent_level());
                oos.writeObject(addRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                System.out.println(response.getMsg());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else  {
            System.out.println("Access Denied: You cannot add users.");
        }
    }
    private void remove_user() {
        if  (menuContext.getCurrent_user().hasPermission(Permissions.MANAGE_USERS)) {
            try {
                int userId = IdVerificator.ask_id();

                if (menuContext.getCurrent_user().getId() == userId) {
                    System.out.println("You cannot delete yourself!");
                    return;
                }

                Request removeRequest = new Request(Request.RequestType.REMOVE_USER, userId, menuContext.getCurrent_level());
                oos.writeObject(removeRequest);
                oos.flush();

                Response response = (Response) ois.readObject();
                System.out.println(response.getMsg());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else   {
            System.out.println("Access Denied: You cannot remove users.");
        }
    }
    private void find_user() {
        try {
            int userId = IdVerificator.ask_id();

            Request findRequest = new Request(Request.RequestType.FIND_USER_BY_ID, userId, menuContext.getCurrent_level());
            oos.writeObject(findRequest);
            oos.flush();

            Response response = (Response) ois.readObject();

            if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("User found");
                SystemUser user = (SystemUser) response.getPayload();
                System.out.println(user);
            }  else {
                System.out.println(response.getMsg());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void show_all_users() {
        try {
            Request showAllUsersRequest = new Request(Request.RequestType.GET_ALL_USERS);
            oos.writeObject(showAllUsersRequest);
            oos.flush();

            Response response = (Response) ois.readObject();

            if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                System.out.println("All users found");
                @SuppressWarnings("unchecked")
                List<SystemUser> list = (List<SystemUser>) response.getPayload();
                list.forEach(System.out::println);
            } else  {
                System.out.println(response.getMsg());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
