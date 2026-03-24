package ua.naukma.server.controller;

import ua.naukma.network.Request;
import ua.naukma.network.Response;

import javax.swing.*;
import java.util.concurrent.Callable;

public interface RequestHandler {
    Response process(Request request);

    @FunctionalInterface
    interface VoidAction {
        void run() throws Exception;
    }

    default Response execute(Callable<Object> action, Request.RequestType requestType) {
        try {
            Object resultData = action.call();
            String successMsg = requestType.name() + " successful";
            if (resultData != null) {
                return new Response(Response.ResponseStatus.SUCCESS, resultData, successMsg);
            } else {
                return new Response(Response.ResponseStatus.SUCCESS, successMsg);
            }
        } catch (Exception e) {
            String errorPrefix = requestType.name() + " failed: "  + e.getMessage();
            return new Response(Response.ResponseStatus.FAILURE, errorPrefix);
        }
    }

    default Response execute(VoidAction action, Request.RequestType requestType) {
        try {
            action.run();
            String successMsg = requestType.name() + " successful";
            return new Response(Response.ResponseStatus.SUCCESS, successMsg);
        } catch (Exception e) {
            String errorPrefix = requestType.name() + " failed: "  + e.getMessage();
            return new Response(Response.ResponseStatus.FAILURE, errorPrefix);
        }
    }
}