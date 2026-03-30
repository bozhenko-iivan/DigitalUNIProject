package ua.naukma.network;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    private Object payload;

    private ResponseStatus status;

    @Serial
    private static final long serialVersionUID = 1L;

    public enum ResponseStatus {
        SUCCESS,
        FAILURE
    }

    private String msg;

    public Response(ResponseStatus status, Object payload, String msg) {
        setResponseStatus(status);
        setPayload(payload);
        setMsg(msg);
    }

    public Response(ResponseStatus status, String msg) {
        this.status = status;
        this.msg = msg;
        this.payload = null;
    }

    public ResponseStatus getResponseStatus() {
        return status;
    }

    public Object getPayload() {
        return payload;
    }

    public String getMsg() {
        return msg;
    }

    public void setResponseStatus(ResponseStatus status) {
        this.status = status;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}