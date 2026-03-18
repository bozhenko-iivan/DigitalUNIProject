package ua.naukma.utils;

import ua.naukma.exception.IncorrectDataException;

import java.util.Scanner;

public class PhoneNumberVerificator {
    public static String ask_phonenum() {
        String error_message = """
                Phone number must contain Ukraine's country code and
                have exactly 9 digits afterwards.
                Example: +380234567890
                Should not contain any spaces.""";
        Scanner scanner = InitScanner.try_init_scanner();
        while (true) {
            System.out.println("Enter person's phone number: ");
            String phone_number = scanner.nextLine().trim();
            try {
                if (!phone_number.matches("\\+380\\d{9}")) {
                    throw new IncorrectDataException(error_message);
                }
                return phone_number;
            } catch (IncorrectDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }
//    private static String validate_phonenum(String phone_number) {
//        boolean cntr_code = false, valid_length = false, digits_only = false;
//        if(phone_number.indexOf("+380") == 0) cntr_code = true;
//        String num = phone_number.substring(3);
//        if(num.length() == 10) valid_length = true;
//        if(digits_only(num)) digits_only = true;
//        if(cntr_code && valid_length && digits_only) return phone_number;
//        else return null;
//    }
//    private static boolean digits_only(String source) {
//        char[] s = source.toCharArray();
//        for(int i = 0; i < s.length; i++){
//            if(s[i] < '0' || s[i] > '9'){
//                return false;
//            }
//        }
//        return true;
//    }

}
