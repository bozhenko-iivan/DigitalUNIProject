package ua.naukma.utils;

import java.util.Scanner;

public class IdVerificator {
    public static int ask_id() {
        Scanner scanner = try_init_scanner();
        String error_message = "ID must be 7 digits long.";
        String id = null;
        while(id == null) {
            System.out.println("Enter ID (7 digits long): ");
            id = scanner.nextLine();
            id = validate_id(id);
            if(id == null) System.out.println(error_message);
        }
        return Integer.parseInt(id);
    }
    private static String validate_id(String id){
        if(id == null) return null;
        for(char c : id.toCharArray()){
            if(!Character.isDigit(c)) return null;
        }
        if(id.length() != 7) return null;
        return id;
    }
    private static Scanner try_init_scanner() {
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
