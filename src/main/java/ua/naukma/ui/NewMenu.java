package ua.naukma.ui;
import ua.naukma.domain.SystemUser;
import ua.naukma.service.*;

import java.util.InputMismatchException;
import java.util.Scanner;
import ua.naukma.service.*;

public class NewMenu{
     public static MenuLevel current_level = MenuLevel.MON;
    private MenuOptionsHandler menu_options_handler;

    public NewMenu(UniversityService universityService, UserService userService, SystemUser loggedUser) {
        this.menu_options_handler = new MenuOptionsHandler(current_level, universityService, userService, loggedUser);
    }

    public void main_menu() {
        draw_greetings();
        while (true) {
            draw_current(current_level);
            current_level = menu_options_handler.handle(current_level);
        }
    }
    private  void draw_greetings() {
        System.out.println("\n\t\t\t\tWelcome to the DigiUni!\n\n\n" +
                "You can add, delete or view parts of the uni structure.\n" +
                "Available operations depend on the directory.\n" +
                "(University, Faculty, Departament etc.)\n\n" +
                "Our system has following hierarchy:\n\n" +
                "\t\t\t\t\t\t\t\tUniversity\n\t\t\t\t\t\t\t\t    ||\n" +
                "\t\t\t\t\t\t\t\t   /  \\\n" +
                "\t\t\t\t\t\t\t\t  /    \\\n" +
                "\t\t\t\t\t ...   Faculty\t\tFaculty  ...\n" +
                "\t\t\t _____________/  / \\\n" +
                "\t\t   /  \t\t\t\t/   \\\n" +
                "\t\t  /   ...  Departament  Departament ...\n" +
                "\t   Groups   \t\t|\n" +
                "\t\t |\t\t\t Teachers\n" +
                "\t  Students\n");
    }
   private  void draw_FAC(){
       System.out.println(
               "2. Go to departaments\n" +
               "3. Go to groups\n" +
               "4. Show faculty info");
   }
   public  void draw_current(MenuLevel level){
       System.out.println("Current level " + current_level);
       System.out.println("Operations: ");
       if (level == MenuLevel.MON) {
           System.out.println("1. Exit system");
       }
       else {
           System.out.println("1. Go higher");
       }
       String s = "";
       switch(level){
           case MON: s = "university"; break;
           case UNI: s = "faculty"; break;
           case DEPARTAMENT: s = "teacher"; break;
           case DEPS: s = "department"; break;
           case GROUP:  s = "student"; break;
           case GRPS: s = "group"; break;
           case FAC: draw_FAC(); return;
           case ADMIN_PANEL: s = "user"; break;
       }
       String action4 = (level == MenuLevel.MON) ? "Select " : "Find ";

       System.out.println("2. Add " + s +
               "\n3. Remove " + s +
               "\n4. " + action4 + s +
               "\n5. Show all " + s + "s");
//       if (level == MenuLevel.DEPARTAMENT) {
//           System.out.println("6. Go to groups");
//       }

       if (level == MenuLevel.MON) {
           System.out.println("6. Manage users (ADMIN only)");
           System.out.println("7. Log out");
       }
   }
}
