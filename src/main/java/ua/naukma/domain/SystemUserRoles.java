package ua.naukma.domain;

import ua.naukma.security.Permissions;

public enum SystemUserRoles {
    USER(Permissions.READ_INFO),
    MANAGER(Permissions.READ_INFO | Permissions.MANAGE_STRUCTURE | Permissions.MANAGE_STUDENTS),
    ADMIN(MANAGER.permissionsMask | Permissions.ADD_UNIVERSITY | Permissions.DELETE_UNIVERSITY | Permissions.MANAGE_USERS);

    private final int permissionsMask;

    SystemUserRoles(int permissionsMask) {
        this.permissionsMask = permissionsMask;
    }
    public int getPermissionsMask() {
        return permissionsMask;
    }
}
