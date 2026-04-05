package ua.naukma.security;

public class Permissions {
    public static final int READ_INFO          = 1;
    public static final int ADD_UNIVERSITY     = 1 << 1;
    public static final int DELETE_UNIVERSITY  = 1 << 2;
    public static final int MANAGE_STRUCTURE   = 1 << 3;
    public static final int MANAGE_STUDENTS    = 1 << 4;
    public static final int MANAGE_USERS       = 1 << 5;
}
