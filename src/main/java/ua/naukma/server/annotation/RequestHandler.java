package ua.naukma.server.annotation;

import ua.naukma.network.Request;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestHandler {
    Request.RequestType value();
}
