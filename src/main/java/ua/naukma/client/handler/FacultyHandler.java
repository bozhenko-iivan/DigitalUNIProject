package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.domain.Faculty;

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
            default -> System.out.println("Invalid choice.");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.UNI);
        menuContext.setCurrent_faculty(null);
    }
    private void go_deps() {
        menuContext.setCurrent_level(MenuLevel.DEPS);
    }
    private void go_groups() {
        menuContext.setCurrent_level(MenuLevel.GRPS);
    }
}
