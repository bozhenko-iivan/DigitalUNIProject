package ua.naukma.domain;

import ua.naukma.security.Permissions;
import ua.naukma.server.repository.GetId;

import java.io.Serializable;

public class SystemUser implements Serializable, GetId {
    private int id;
    private String login;
    private String password;
    private SystemUserRoles role;

    public SystemUser(int id, String login, String password, SystemUserRoles role) {
        setId(id);
        setLogin(login);
        setPassword(password);
        setRoleMask(role);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    public SystemUserRoles getRole() {
        return role;
    }

    @Override
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRoleMask(SystemUserRoles role) {
        this.role = role;
    }

    public boolean hasPermission(int requiredPermission) {
        return (this.role.getPermissionsMask() & requiredPermission) == requiredPermission;
    }

    private String getRightsString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        if (hasPermission(Permissions.READ_INFO)) {
            builder.append("|READ|");
        }
        if (hasPermission(Permissions.MANAGE_USERS)) {
            builder.append("MANAGE_USERS|");
        }
        if (hasPermission(Permissions.MANAGE_STRUCTURE)) {
            builder.append("MANAGE_STRUCTURE|");
        }
        if (hasPermission(Permissions.MANAGE_STUDENTS)) {
            builder.append("MANAGE_STUDENTS|");
        }
        if (hasPermission(Permissions.ADD_UNIVERSITY)) {
            builder.append("ADDITION_UNIVERSITY|");
        }
        if (hasPermission(Permissions.DELETE_UNIVERSITY)) {
            builder.append("DELETION_UNIVERSITY|");
        }
        builder.append(']');
        return builder.toString();
    }

    @Override
    public String toString() {
        if (role == null) {
            return "User: " + login + "\nID: " + id + "\nRole: NOT ASSIGNED";
        }
        return "User: " + login + "\nID: " + id + "\nRole: " + role + "\nCurrent rights: " + getRightsString();
    }
}
