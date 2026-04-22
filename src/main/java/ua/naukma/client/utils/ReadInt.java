package ua.naukma.client.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ReadInt {
    public static int readInt(){
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}
