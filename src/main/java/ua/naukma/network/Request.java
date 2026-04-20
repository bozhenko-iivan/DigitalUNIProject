package ua.naukma.network;

import ua.naukma.client.ui.MenuLevel;
import ua.naukma.security.Permissions;
import ua.naukma.server.annotation.Secured;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    private Object data;
    private RequestType type;
    private MenuLevel level;
    @Serial
    private static final long serialVersionUID = 1L;

    public enum RequestType {
        LOGIN,
        LOGOUT,

        @Secured(Permissions.MANAGE_USERS)
        ADD_USER,
        @Secured(Permissions.MANAGE_USERS)
        REMOVE_USER,
        @Secured(Permissions.MANAGE_STUDENTS)
        ADD_STUDENT,
        @Secured(Permissions.MANAGE_STUDENTS)
        REMOVE_STUDENT,

        GET_ALL_USERS,
        GET_ALL_STUDENTS,

        FIND_USER_BY_ID,
        FIND_STUDENT_BY_ID,

        GET_STUDENTS_COUNT,

        UPDATE_STUDENT_CONTACTS,
        UPDATE_STUDENT_STUDY_FORM,
        UPDATE_STUDENT_STATUS,
        SET_STUDENT_GRADE,
        DELETE_STUDENT_GRADE,
        SHOW_TRANSCRIPT,
        CALCULATE_AVG,

        @Secured(Permissions.MANAGE_STRUCTURE)
        SET_DEAN,

        @Secured(Permissions.MANAGE_STRUCTURE)
        SET_HEAD,

        @Secured(Permissions.MANAGE_STRUCTURE)
        ADD,
        @Secured(Permissions.MANAGE_STRUCTURE)
        REMOVE,
        FIND,

        GET_ALL,
        SORT_BY_ID,
        SORT_BY_ALPHABETIC_NAME,
    }
    public Request(RequestType type, Object data) {
        setType(type);
        setData(data);
    }
    public Request(RequestType type, Object data, MenuLevel menuLevel) {
        setType(type);
        setData(data);
        this.level = menuLevel;
    }

    public MenuLevel getLevel() {
        return level;
    }
    public Request(RequestType type) {
        setType(type);
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public RequestType getType() {
        return type;
    }
}
