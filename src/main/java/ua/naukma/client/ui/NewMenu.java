package ua.naukma.client.ui;
import ua.naukma.domain.SystemUser;
import ua.naukma.security.Permissions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NewMenu {
    public static MenuLevel current_level = MenuLevel.MON;
    private MenuOptionsHandler menu_options_handler;
    private SystemUser loggedUser;


    public NewMenu(ObjectOutputStream universityService, ObjectInputStream userService, SystemUser loggedUser) {
        this.loggedUser = loggedUser;
        this.menu_options_handler = new MenuOptionsHandler(new MenuContext(loggedUser, current_level), universityService, userService);
    }

    public void main_menu(Socket socket){
        draw_greetings();
        while (current_level != null) {
            if (current_level == MenuLevel.LOGIN) {
                break;
            }
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
                        "4. Show faculty info\n" +
                        "5. Set Faculty's Dean");
    }

    public void draw_current(MenuLevel level) {
        System.out.println("===================================");
        System.out.println("\uD83D\uDCCD Navigation: " + menu_options_handler.handleUiDrawing());
        System.out.println("===================================");
        System.out.println(menu_options_handler.handleInfoAboutEntityDrawing());
        System.out.println("===================================");
        System.out.println("⚙\uFE0F Operations: ");
        if (level == MenuLevel.MON) {
            System.out.println("\uD83D\uDD11 1. Exit system");
        } else {
            System.out.println("⬆\uFE0F 1. Go higher");
        }

        String s = "";
        boolean canAdd = false;
        boolean canRemove = false;

        switch (level) {
            case MON:
                s = "University";
                canAdd = loggedUser.hasPermission(Permissions.ADD_UNIVERSITY);
                canRemove = loggedUser.hasPermission(Permissions.DELETE_UNIVERSITY);
                break;
            case UNI:
                s = "Faculty";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                break;
            case DEPARTAMENT:
                s = "Teacher";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                break;
            case DEPS:
                s = "Department";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                break;
            case GROUP:
                s = "Student";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STUDENTS);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STUDENTS);
                break;
            case GRPS:
                s = "Group";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE);
                break;
            case FAC:
                draw_FAC();
                return;
            case ADMIN_PANEL:
                s = "User";
                canAdd = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                canRemove = loggedUser.hasPermission(Permissions.MANAGE_USERS);
                break;
        }

        if (level != MenuLevel.STUDENT) {
            String action4;
            if (level == MenuLevel.UNI || level == MenuLevel.DEPS || level == MenuLevel.GRPS) {
                action4 = "Select ";
            } else {
                action4 = "Find ";
            }

            if (canAdd) {
                System.out.println("➕ 2. Add " + s);
            }
            if (canRemove) {
                System.out.println("❌ 3. Remove " + s);
            }

            System.out.println("\uD83D\uDD0D 4. " + action4 + s + " by ID");
            System.out.println("\uD83D\uDCCB 5. Show all " + s + "s");
        }

        if (level == MenuLevel.MON) {
            if (loggedUser.hasPermission(Permissions.MANAGE_USERS)) {
                System.out.println("\uD83D\uDD12 6. Manage users (ADMIN only): ");
            }
            System.out.println("\uD83D\uDEAA 7. Log out");
        }

        if (level == MenuLevel.DEPARTAMENT) {
            if (loggedUser.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                System.out.println("6. Set Head of Department");
            }
        }

        if (level == MenuLevel.STUDENT) {
            System.out.println("ℹ\uFE0F 2. Show student info");
            System.out.println("\uD83D\uDCC7 3. Update student's contacts");
            System.out.println("\uD83D\uDCDA 4. Update student's study form");
            System.out.println("✅ 5. Update student's status");
            System.out.println("\uD83D\uDCAF 6. Set student's grade");
            System.out.println("⛔ 7. Delete student's grade");
            System.out.println("\uD83D\uDCCB 8. Show student's transcript");
        }

        System.out.println("===================================");
    }
}
