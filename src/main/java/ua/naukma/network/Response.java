package ua.naukma.network;

import java.io.Serializable;

public class Response implements Serializable {
    enum responseStatus {
        SUCCESS,
        FAILURE
    }

    String successMessage;
    String failureMessage;
}