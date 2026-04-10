package ua.naukma.client.ui;

import ua.naukma.client.handler.*;
import ua.naukma.client.utils.*;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class MenuOptionsHandler{
        private MenuContext menuContext;
        Map<MenuLevel, BasicHandler> handlersMap;

    public MenuOptionsHandler(MenuContext menuContext, ObjectOutputStream oos, ObjectInputStream ois) {
        this.menuContext = menuContext;
        handlersMap = new HashMap<>();
        handlersMap.put(MenuLevel.ADMIN_PANEL, new AdminPanelHandler(menuContext, oos, ois));
        handlersMap.put(MenuLevel.MON, new MONHandler(ois, oos, menuContext));
        handlersMap.put(MenuLevel.UNI, new UniHandler(menuContext, oos, ois));
        handlersMap.put(MenuLevel.FAC, new FacultyHandler(menuContext, oos, ois));
        handlersMap.put(MenuLevel.DEPS, new DepsHandler(menuContext, ois, oos));
        handlersMap.put(MenuLevel.DEPARTAMENT,  new DepartmentHandler(menuContext, oos, ois));
        handlersMap.put(MenuLevel.GRPS, new GrpsHandler(menuContext, ois, oos));
        handlersMap.put(MenuLevel.GROUP, new GroupHandler(menuContext, ois, oos));
        handlersMap.put(MenuLevel.STUDENT, new StudentHandler(ois, oos, menuContext));
    }

    public String handleUiDrawing() {
        StringBuilder path = new StringBuilder("MON");
        MenuLevel level = menuContext.getCurrent_level();
        switch (level) {
            case MON -> {
               break;
            }
            case UNI -> {
                path.append(" > ").append(menuContext.getCurrent_university().getShortName());
            }
            case FAC ->  {
                path.append(" > ").append(menuContext.getCurrent_university().getShortName())
                        .append(" > ").append(menuContext.getCurrent_faculty().getShortName());
            }
            case DEPS -> {
                path.append(" > ").append(menuContext.getCurrent_university().getShortName())
                        .append(" > ").append(menuContext.getCurrent_faculty().getShortName())
                        .append(" > [DEPS]");
            }

            case DEPARTAMENT -> {
                path.append(" > ").append(menuContext.getCurrent_university().getShortName())
                        .append(" > ").append(menuContext.getCurrent_faculty().getShortName())
                        .append(" > ").append(menuContext.getCurrent_department().getName());
            }
            case GRPS -> {
                path.append(" > ").append(menuContext.getCurrent_university().getShortName())
                        .append(" > ").append(menuContext.getCurrent_faculty().getShortName())
                        .append(" > [GRPS]");
            }
            case GROUP -> {
                path.append(" > ").append(menuContext.getCurrent_university().getShortName())
                        .append(" > ").append(menuContext.getCurrent_faculty().getShortName())
                        .append(" > ").append(menuContext.getCurrent_group().getName());
            }
            case STUDENT -> {
                path.append(" > ").append(menuContext.getCurrent_university().getShortName())
                        .append(" > ").append(menuContext.getCurrent_faculty().getShortName())
                        .append(" > ").append(menuContext.getCurrent_group().getName()).append(" > ").append("Student ").append(menuContext.getCurrent_student().getLastName());
            }
            case ADMIN_PANEL -> {
                path.append(" > [Admin Panel]");
            }
        }
        return path.toString();
    }

    public String handleInfoAboutEntityDrawing() {
        if (menuContext.getCurrent_level() == MenuLevel.ADMIN_PANEL) {
            return "";
        }

        String content = "";

        switch (menuContext.getCurrent_level()) {
            case MON -> content = DashboardBuilder.buildMONPanel();
            case UNI -> content = DashboardBuilder.buildUniversityPanel(menuContext.getCurrent_university());
            case FAC -> content = DashboardBuilder.buildFacultyPanel(menuContext.getCurrent_faculty());
            case GRPS -> content = DashboardBuilder.buildFacultyPanel(menuContext.getCurrent_faculty());
            case DEPARTAMENT -> content = DashboardBuilder.buildDepartmentPanel(menuContext.getCurrent_department());
            case DEPS -> content = DashboardBuilder.buildFacultyPanel(menuContext.getCurrent_faculty());
            case GROUP -> {
                long studentsCount = fetchStudentsCountFromServer(menuContext.getCurrent_group().getId());
                content = DashboardBuilder.buildGroupPanel(menuContext.getCurrent_group(), studentsCount);
            }
            case STUDENT -> content = DashboardBuilder.buildStudentPanel(menuContext.getCurrent_student());
        }

        return content;
    }

    private long fetchStudentsCountFromServer(int groupId) {
        Response res =  handlersMap.get(MenuLevel.STUDENT).sendRequest(Request.RequestType.GET_STUDENTS_COUNT, groupId, false);
        if (res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            return (long) res.getPayload();
        }
        return -1;
    }
    public MenuLevel handle(MenuLevel lvl){
        handlersMap.get(lvl).handle();
        return menuContext.getCurrent_level();
    }
}