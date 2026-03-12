package ua.naukma.ui;
import ua.naukma.service.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class NewMenu{
     public static MenuLevel current_level = MenuLevel.UNI;
     MenuOptionsHandler menu_options_handler = new MenuOptionsHandler(current_level);
    public void main_menu(){
        draw_greetings();
        while(current_level != MenuLevel.MON){
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
       System.out.println("Current operations: ");
       if(level != MenuLevel.MON) System.out.println("1. Go higher");
       else System.out.println("1. Exit.");
       String s = "";
       switch(level){
           case MON: s = "university"; break;
           case UNI: s = "faculty"; break;
           case DEPARTAMENT: s = "teacher"; break;
           case DEPS: s = "department"; break;
           case GROUP:  s = "student"; break;
           case GRPS: s = "group"; break;
           case FAC: draw_FAC(); return;
       }
       System.out.println("2. Add " + s + "\n3. Remove " + s + "\n4. Find " + s + "\n5. Show all");
   }
}
