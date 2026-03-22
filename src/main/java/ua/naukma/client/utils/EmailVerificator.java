package ua.naukma.client.utils;

import java.util.Scanner;

public class EmailVerificator {
    public static String ask_email() {
        String error_message = """
                Invalid email.
                Examples of a valid one:
                ivanLastivka@gmail.com
                or   ivanLastivka@ukr.net
                """;
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            String email;
            System.out.println("Enter email: ");
            email = scanner.nextLine();
            if (email.matches("^[a-zA-Z0-9.-]+@(gmail\\.com|ukr\\.net)$")) {
                return email;
            } else {
                System.out.println(error_message);
            }
        }
    }
//    private static String validate_email(String email) {
//        if(email == null) return null;
//        for(char c : email.toCharArray()){
//            if(c >= 'А' && c <= 'Я' || c >= 'а' && c <= 'я'||
//                    c == 'ї'|| c == 'Ї'|| c == 'І'|| c == 'і' ){
//                return null;
//            }
//        }
//        String target1 = "@gmail.com", target2 = "@ukr.net";
//        int dom_occ = count_occurances(target1, target2, email);
//        int sob_occ =  count_occurances("@", "", email);
//        int dots_occ = count_occurances("..", "", email);
//        boolean spaces = spacesPresent(email);
//        if(dom_occ == 1 && sob_occ == 1 && !spaces && dots_occ == 0){
//            if(email.indexOf(target1) < email.length() - target1.length() && email.contains(target1)){
//                return null;
//            }else if(email.indexOf(target2) < email.length() - target2.length() && email.contains(target2)){
//                return null;
//            }
//            else return email;
//        }
//        else {
//            return null;
//        }
//    }
//    private static int count_occurances(String target1, String target2, String source) {
//        int occurances = 0;
//        for (int index = 0; index < source.length(); index++) {
//            int index1 = source.indexOf(target1, index);
//            if(index1 == -1) break;
//            else occurances++;
//            index = index1;
//        }
//        if(!target2.isEmpty()){
//            for (int index = 0; index < source.length(); index++) {
//                int index1 = source.indexOf(target2, index);
//                if(index1 == -1) break;
//                else occurances++;
//                index = index1 + 1;
//            }
//        }
//        return occurances;
//    }
//    private static boolean spacesPresent(String source) {
//        char[] s = source.toCharArray();
//
//        for(int i = 0; i < s.length; i++){
//            if(s[i] == ' '){
//                return true;
//            }
//        }
//
//        return false;
//    }
}
