package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.ReadInt;
import ua.naukma.domain.SystemUser;
import ua.naukma.network.Request;
import ua.naukma.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class BasicHandler {
    protected MenuContext menuContext;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    public BasicHandler(MenuContext context, ObjectOutputStream oos, ObjectInputStream ois) {
        this.menuContext = context;
        this.oos = oos;
        this.ois = ois;
    }
    protected void requirePermission(int requiredPermission, Runnable action) {
        if (menuContext.getCurrent_user().hasPermission(requiredPermission)) {
            action.run();
        } else {
            System.out.println("Access denied");
        }
    }
    protected int readInt(){
        return ReadInt.readInt();
    }
    protected boolean isIdAlreadyTaken(int id, Request.RequestType requestType) {
        Response response = sendRequest(requestType, id, true);
        return response != null && response.getResponseStatus() == Response.ResponseStatus.SUCCESS;
    }
    public Response sendRequest(Request.RequestType requestType, Object payload, boolean silent, MenuLevel targetLevel) {
        try {
            Request request = new Request(requestType, payload, targetLevel);
            oos.writeObject(request);
            oos.flush();

            Response response = (Response) ois.readObject();

            if (response != null && !silent) {
                System.out.println(response.getMsg());
            }
            return response;
        } catch (IOException | ClassNotFoundException e) {
            return new Response(Response.ResponseStatus.FAILURE, "Error while trying to read response from server");
        }
    }
    public Response sendRequest(Request.RequestType requestType, Object payload, boolean silent) {
        return sendRequest(requestType, payload, silent, menuContext.getCurrent_level());
    }
    public abstract void handle();
}
