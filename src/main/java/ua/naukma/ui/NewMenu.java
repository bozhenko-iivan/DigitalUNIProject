package ua.naukma.ui;
import ua.naukma.service.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class NewMenu{
    private enum menu_level{
        MON,
        UNI,
        FAC,
        DEPS,
        GRPS,
        DEPARTAMENT,
        GROUP,
        TEACHS,
        STUDS
    }
    final private StudentService studentService = new StudentService();
    public void draw_greetings() {
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
   public void draw_MON(){
        System.out.println("Current operations:\n" +
                "1. Add university\n" +
                "2. Remove university\n" +
                "3. Find university\n" +
                "4. Show all universities\n");
   }
   public void draw_UNI(){
        System.out.println("Current operations:\n" +
                "1. Go higher\n" +
                "2. Add faculty\n" +
                "3. Remove faculty\n" +
                "4. Find faculty\n" +
                "5. Show all faculties\n" +
                "6. Find person (student/teacher)\n");
   }
   public void draw_FAC(){
       System.out.println("Current operations:\n" +
               "1. Go higher\n" +
               "2. Go to departaments\n" +
               "3. Go to groups\n" +
               "4. Show faculty info\n");
   }
   public void draw_DEP(){
       System.out.println("Current operations:\n" +
               "1. Go higher\n" +
               "2. Add departament\n" +
               "3. Remove departament\n" +
               "4. Find departament\n" +
               "5. Show all departaments\n");
   }
   public void draw_DEPARTAMENT(){
        System.out.println("Current operations:\n" +
                "1. Go higher\n" +
                "2. Show all teachers and departament info\n" +
                "3. Add teacher\n" +
                "4. Remove teacher\n" +
                "5. Find teacher\n");
   }
   public void draw_GRPS(){
       System.out.println("Current operations:\n" +
               "1. Go higher \n" +
               "2. Add group\n" +
               "3. Remove group\n" +
               "4. Find group\n" +
               "5. Show all groups\n");
   }
   public void draw_GROUP(){
       System.out.println("1. Go higher\n" +
               "2. Show all students and group info\n" +
               "3. Add student\n" +
               "4. Remove student\n" +
               "5. Find student\n");
   }
    public void menu_test(){
        for(;;) {
        System.out.println("This is a test version just for the operations with students.\n" +
                "The options are:\n" + "1. Add student.\n2. Find student." +
                "\n3. Delete student.\n4. Show list of all students.\n5. Exit.");
        Scanner scanner = new Scanner(System.in);
            int choice = 0;
            while (choice < 1 || choice > 5) {
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                }
            }
            switch (choice) {
                case 1:
                    studentService.addStudent();
                    break;
                case 2:
                    studentService.findStudent();
                    break;
                case 3:
                    studentService.deleteStudent();
                    break;
                case 4:
                    studentService.studentsShowList();
                    break;
                case 5:
                    return;
            }
        }
    }
}
