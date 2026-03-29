package ua.naukma.network;

import ua.naukma.security.Permissions;
import ua.naukma.server.annotation.Secured;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    private Object data;
    private RequestType type;

    @Serial
    private static final long serialVersionUID = 1L;

    public enum RequestType {
        LOGIN,
        LOGOUT,

        @Secured(Permissions.MANAGE_USERS)
        ADD_USER,

        @Secured(Permissions.MANAGE_USERS)
        REMOVE_USER,

        @Secured(Permissions.ADD_UNIVERSITY)
        ADD_UNIVERSITY,

        @Secured(Permissions.MANAGE_STRUCTURE)
        REMOVE_UNIVERSITY,

        @Secured(Permissions.MANAGE_STRUCTURE)
        ADD_FACULTY,

        @Secured(Permissions.MANAGE_STRUCTURE)
        REMOVE_FACULTY,

        @Secured(Permissions.MANAGE_STRUCTURE)
        ADD_GROUP,

        @Secured(Permissions.MANAGE_STRUCTURE)
        REMOVE_GROUP,

        @Secured(Permissions.MANAGE_STUDENTS)
        ADD_STUDENT,

        @Secured(Permissions.MANAGE_STUDENTS)
        REMOVE_STUDENT,

        @Secured(Permissions.MANAGE_STRUCTURE)
        ADD_DEPARTMENT,

        @Secured(Permissions.MANAGE_STRUCTURE)
        REMOVE_DEPARTMENT,

        @Secured(Permissions.MANAGE_STRUCTURE)
        ADD_TEACHER,

        @Secured(Permissions.MANAGE_STRUCTURE)
        REMOVE_TEACHER,

        GET_ALL_USERS,
        GET_ALL_TEACHERS,
        GET_ALL_DEPARTMENTS,
        GET_ALL_GROUPS,
        GET_ALL_STUDENTS,
        GET_ALL_FACULTIES,
        GET_ALL_UNIVERSITIES,

        FIND_USER_BY_ID,
        FIND_UNIVERSITY_BY_ID,
        FIND_FACULTY_BY_ID,
        FIND_STUDENT_BY_ID,
        FIND_DEPARTMENT_BY_ID,
        FIND_GROUP_BY_ID,
        FIND_TEACHER_BY_ID,

        GET_STUDENTS_COUNT,
        UPDATE_STUDENT_CONTACTS,
        UPDATE_STUDENT_STUDY_FORM,
        UPDATE_STUDENT_STATUS
    }

    public Request(RequestType type, Object data) {
        setType(type);
        setData(data);
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
