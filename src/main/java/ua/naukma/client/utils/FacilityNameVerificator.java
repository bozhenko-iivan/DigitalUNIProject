package ua.naukma.client.utils;

import java.util.Scanner;

public class FacilityNameVerificator {
    public static final String GROUP_NAME_REGEX = "^[A-Z]+\\-[1-6]$";
    public static String ask_facility_name() {
        String error_message = "Name contains invalid characters or overall incorrect.";
        Scanner scanner = InitScanner.try_init_scanner();
        String name;
        do{
            System.out.print("Enter facility name: ");
            name = scanner.nextLine();
            name = validate_facility_name(name);
            if(name == null) System.out.println(error_message);
        }while(name == null);
        delete_extra_spaces(name);
        return name;
    }
    private static String validate_facility_name(String name) {
        if(name == null) return null;
        char c = Character.toLowerCase(name.charAt(0));
        boolean is_eng;
        if(c >= 'a' && c <= 'z') is_eng = true;
        else if (c >= 'а' && c <= 'я') is_eng = false;
        else return null;
        for(char ch : name.toCharArray()){
            if(!is_char_valid(ch, is_eng)) return null;
        }
        boolean is_parentheses_closed = is_parentheses_closed(name);
        if(!is_parentheses_closed) return null;
        return name;
    }

    public static String ask_group_name() {
        String error_message = "Name contains invalid characters or overall incorrect.";
        Scanner scanner = InitScanner.try_init_scanner();
        String name;
        while (true) {
            System.out.print("Enter group name: (e.g: IPZ-1): ");
            name = scanner.nextLine();
            if (name != null && name.matches(GROUP_NAME_REGEX)) {
                return name;
            }
            else {
                System.out.println(error_message);
            }
        }
    }

    private static boolean is_char_valid(char c, boolean is_english) {
        if (is_english) {
            return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')  ||
                    c == '"' || c == '.' || c == ','
                    || c == ':' || c == '(' || c == ')' || c == '\''
                    || c == '-' || c == '–' || c == '/' || c == '!' || c == '?'
                    || c == '№' || c == '@' || c == '&' || c == ' ';
        } else {
            return (c >= 'а' && c <= 'я') || (c >= 'А' && c <= 'Я') ||
                    c == 'Ї' || c == 'ї' || c == '"' || c == '.' || c == ','
                    || c == ':' || c == '(' || c == ')' || c == '\''
                    || c == '-' || c == '–' || c == '/' || c == '!' || c == '?'
                    || c == '№' || c == '@' || c == '&' || c == ' ';
        }
    }
    private static boolean is_parentheses_closed(String s) {
        int num_quotes = 0;
        int num_parentheses = 0;
        for(char c : s.toCharArray()){
            if(c == '"') num_quotes++;
            else if(c == '(') num_parentheses++;
            else if(c == ')') num_parentheses--;
        }
        return num_quotes % 2 == 0 && num_parentheses == 0;
    }
    private static void delete_extra_spaces(String name) {
        while(name.indexOf(' ') == 0 || name.indexOf('\t') == 0){
            name = name.replace("  ", "");
            name = name.replace("\t", "");
        }
        while(name.indexOf("  ") > 0 || name.indexOf("\t") > 0) {
            name = name.replace("  ", " ");
            name = name.replace("\t", " ");
        }
    }
    public static String ask_short_name() {
        String error_message = """
                Name contains invalid characters or
                is more than 10 characters / less than 2 characters long.""";
        Scanner scanner = InitScanner.try_init_scanner();
        String name;
        do{
            System.out.println("Enter facility's shortname: ");
            name = scanner.nextLine();
            name = validate_shortname(name);
            if(name == null) System.out.println(error_message);
        }while(name == null);
        return name;
    }
    private static String validate_shortname(String name) {
        if(name == null) return null;
        char c = Character.toLowerCase(name.charAt(0));
        boolean is_eng;
        if(c >= 'a' && c <= 'z') is_eng = true;
        else if (c >= 'а' && c <= 'я') is_eng = false;
        else return null;
        if (!isAlpha(name, is_eng) && (name.length() < 2 || name.length() > 9)) return null;
        return name;
    }
    private static boolean isAlpha(String s, boolean is_english){
        boolean isalpha = true;
        if(is_english) {
            for (char c : s.toCharArray()) {
                if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                    isalpha = false;
                    break;
                }
            }
        }
        else{
            for (char c : s.toCharArray()) {
                if ((c < 'А' || c > 'Я') && (c < 'а' || c > 'я')&&
                        ( c != 'ї'&& c != 'Ї'&& c != 'І'&& c != 'і')) {
                    isalpha = false;
                    break;
                }
            }
        }
        return isalpha;
    }
}
