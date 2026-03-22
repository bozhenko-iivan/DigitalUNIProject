package ua.naukma.client.ui;
import ua.naukma.domain.SystemUser;
import ua.naukma.security.Permissions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NewMenu {
    public static MenuLevel current_level = MenuLevel.MON;
    private MenuOptionsHandler menu_options_handler;
    private SystemUser loggedUser;

    public NewMenu(ObjectOutputStream universityService, ObjectInputStream userService, SystemUser loggedUser) {
        this.loggedUser = loggedUser;
        this.menu_options_handler = new MenuOptionsHandler(current_level, universityService, userService, loggedUser);
    }

    public void main_menu() {
        draw_greetings();
        while (current_level != null) {
            draw_current(current_level);
            current_level = menu_options_handler.handle(current_level);
        }
    }

    private void draw_greetings() {
        System.out.println("\n\t\t\t\tWelcome to the DigiUni!\n\n\n" +
                "You can add, delete or view parts of the uni structure.\n" +
                "Available operations depend on the directory.\n" +
                "(University, Faculty, Departament etc.)\n\n" +
                "Our system has following hierarchy:\n\n" +
                "\t\t\t\t\t\t\t\tUniversity\n\t\t\t\t\t\t\t\t    ||\n" +
                "\t\t\t\t\t\t\t\t   /  \\\n" +
                "\t\t\t\t\t\t\t\t  /    \\\n" +
                "\t\t\t\t\t ...   Faculty\t\tFaculty  ...\n" +
                "\t\t\t _____________/  / \\\n" +
                "\t\t   /  \t\t\t\t/   \\\n" +
                "\t\t  /   ...  Departament  Departament ...\n" +
                "\t   Groups   \t\t|\n" +
                "\t\t |\t\t\t Teachers\n" +
                "\t  Students\n");
    }

    private void draw_FAC() {
        System.out.println(
                "2. Go to departaments\n" +
                        "3. Go to groups\n" +
                        "4. Show faculty info");
    }

    public void draw_current(MenuLevel level) {
        System.out.println("Current level " + current_level);
        System.out.println("Operations: ");
        if (level == MenuLevel.MON) {
            System.out.println("1. Exit system");
        } else {
            System.out.println("1. Go higher");
        }

        String s = "";
        boolean canAdd = false;
        boolean canRemove = false;

        switch (level) {
            case MON:
                s = "university";
                canAdd = loggedUser.hasPermission(Permissions.ADD_UNIVERSITY);
                canRemove = loggedUser.hasPermission(Permissions.DELETE_UNIVERSITY);
                break;
            case UNI:
                s = "faculty";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                break;
            case DEPARTAMENT:
                s = "teacher";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                break;
            case DEPS:
                s = "department";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                break;
            case GROUP:
                s = "student";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STUDENTS);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STUDENTS);
                break;
            case GRPS:
                s = "group";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                break;
            case FAC:
                draw_FAC();
                return;
            case ADMIN_PANEL:
                s = "user";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                break;
        }

        String action4;
        if (level == MenuLevel.MON || level == MenuLevel.UNI || level == MenuLevel.DEPS || level == MenuLevel.GRPS) {
            action4 = "Select ";
        } else {
            action4 = "Find ";
        }

        if (canAdd) {
            System.out.println("2. Add " + s);
        }
        if (canRemove) {
            System.out.println("3. Remove " + s);
        }

        System.out.println("4. " + action4 + s);
        System.out.println("5. Show all " + s + "s");

        if (level == MenuLevel.MON) {
            if (loggedUser.hasPermission(Permissions.MANAGE_USERS)) {
                System.out.println("6. Manage users (ADMIN only): ");
            }
            System.out.println("7. Log out");
        }
    }
}
