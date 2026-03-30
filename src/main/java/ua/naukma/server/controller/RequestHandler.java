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
        String lowerCase = requestType.name().replace('_', ' ').toLowerCase();

        try {
            Object resultData = action.call();
            String successMsg = lowerCase + ": ";
            if (resultData != null) {
                return new Response(Response.ResponseStatus.SUCCESS, resultData, successMsg);
            } else {
                return new Response(Response.ResponseStatus.SUCCESS, successMsg);
            }
        } catch (Exception e) {
            String errorPrefix = lowerCase + ": \n" + e.getMessage();
            return new Response(Response.ResponseStatus.FAILURE, errorPrefix);
        }
    }

    default Response execute(VoidAction action, Request.RequestType requestType) {
        String lowerCase = requestType.name().replace('_', ' ').toLowerCase();

        try {
            action.run();
            String successMsg = lowerCase + ": ";
            return new Response(Response.ResponseStatus.SUCCESS, successMsg);
        } catch (Exception e) {
            String errorPrefix = lowerCase+ ": \n" + e.getMessage();
            return new Response(Response.ResponseStatus.FAILURE, errorPrefix);
        }
    }
}