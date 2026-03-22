package ua.naukma.network;

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

        ADD_USER,
        REMOVE_USER,

        ADD_UNIVERSITY,
        REMOVE_UNIVERSITY,

        ADD_FACULTY,
        REMOVE_FACULTY,

        ADD_GROUP,
        REMOVE_GROUP,

        ADD_STUDENT,
        REMOVE_STUDENT,

        ADD_DEPARTMENT,
        REMOVE_DEPARTMENT,

        ADD_TEACHER,
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
