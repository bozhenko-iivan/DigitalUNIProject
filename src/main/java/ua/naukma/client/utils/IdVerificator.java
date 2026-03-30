package ua.naukma.client.utils;

import java.util.Scanner;

public class IdVerificator {
    public static int ask_id() {
        Scanner scanner = InitScanner.try_init_scanner();
        String id;

        while (true) {
            System.out.print("Please enter your ID (e.g 1234567): ");
            id = scanner.nextLine();
            if (id.length() != 7) {
                System.out.println("ID must be 7 digits long.");
                continue;
            }
            if (!id.matches("\\d+")) {
                System.out.println("ID must contain digits only");
                continue;
            }
            return Integer.parseInt(id);
        }
    }

    private static String validate_id(String id){
        if(id == null) return null;
        for(char c : id.toCharArray()){
            if(!Character.isDigit(c)) return null;
        }
        if(id.length() != 7) return null;
        return id;
    }
}
