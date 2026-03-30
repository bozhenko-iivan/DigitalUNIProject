package ua.naukma.server.annotation;

import ua.naukma.network.Request;
import ua.naukma.security.Permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
    int value();
}
