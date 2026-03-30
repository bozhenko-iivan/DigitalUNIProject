package ua.naukma.client.utils;

import java.util.Scanner;

public class InitScanner {
    public static Scanner try_init_scanner() {
        Scanner scanner = null;
        while (scanner == null) {
            try {
                scanner = new Scanner(System.in);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        return scanner;
    }
}
