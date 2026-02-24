package ua.naukma.utils;

import java.util.Scanner;

public class PhoneNumberVerificator {
    public static String ask_phonenum() {
        String error_message = """
                Phone number must contain Ukraine's country code and
                have 10 digits afterwards
                Example:
                +380234567890
                Should not contain any spaces.""";
        Scanner scanner = try_init_scanner();
        String phone_number;
        do{
            System.out.println("Enter person's phone number: ");
            phone_number = scanner.nextLine();
            phone_number = validate_phonenum(phone_number);
            if(phone_number == null) System.out.println(error_message);
        }while(phone_number == null);
        return phone_number;
    }
    private static String validate_phonenum(String phone_number) {
        boolean cntr_code = false, valid_length = false, digits_only = false;
        if(phone_number.indexOf("+380") == 0) cntr_code = true;
        String num = phone_number.substring(3);
        if(num.length() == 10) valid_length = true;
        if(digits_only(num)) digits_only = true;
        if(cntr_code && valid_length && digits_only) return phone_number;
        else return null;
    }
    private static boolean digits_only(String source) {
        char[] s = source.toCharArray();
        for(int i = 0; i < s.length; i++){
            if(s[i] < '0' || s[i] > '9'){
                return false;
            }
        }
        return true;
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
