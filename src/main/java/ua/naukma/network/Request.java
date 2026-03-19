package ua.naukma.network;

import java.io.Serializable;

public class Request implements Serializable {
    enum requestType {
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
    }
}
