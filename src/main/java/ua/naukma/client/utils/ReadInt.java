package ua.naukma.client.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ReadInt {
    public static int readInt(){
        Scanner scanner = InitScanner.try_init_scanner();
        for(;;) {
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer.");
                scanner.next();
                continue;
            }
            return choice;
        }
    }
}
