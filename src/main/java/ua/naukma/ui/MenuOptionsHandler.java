package ua.naukma.ui;

import ua.naukma.domain.Department;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.University;
import ua.naukma.service.*;
import ua.naukma.utils.InitScanner;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuOptionsHandler{
    //University u = new University(1,"NAUKMA", "KMA", "Kyiv", "Contractova district.");
    private MenuLevel current_level;
    private Faculty current_faculty;
    private Department current_department;
    private University current_university;
    private UniversityService universityService;

    public MenuOptionsHandler(MenuLevel current_level, UniversityService universityService) {
        this.current_level = current_level;
        this.universityService = universityService;
    }

    private void handle_MON() {
        int choice = readInt();
        switch (choice) {
            case 1: System.out.println("Exiting.."); System.exit(0); break;
            case 2: universityService.add(); break;
            case 3: universityService.delete(); break;
            case 4:
            University found = universityService.findById();
            if (found != null) {
                current_university = found;
                current_level = MenuLevel.UNI;
            }
            break;
            case 5: universityService.showAll(); break;
        }
    }

    private void handle_UNI(University u) {
            int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.MON; break;
                case 2: u.getFacultyService().add(); break;
                case 3: u.getFacultyService().delete(); break;
                case 4: {
                    current_faculty = u.getFacultyService().workWithFaculty(current_level);
                    current_level = MenuLevel.FAC;
                    if(current_faculty == null){
                        current_level = MenuLevel.UNI;
                    }
                    break;
                }
                case 5: u.getFacultyService().showAll(); break;
            }
    }

    private   void handle_FAC(Faculty f) {
        /*System.out.println("Current operations:\n" +
                "1. Go higher\n" +
                "2. Go to departaments\n" +
                "3. Go to groups\n" +
                "4. Show faculty info\n");*/
        int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.UNI; break;
                case 2: current_level = MenuLevel.DEPS; break;
                case 3: System.out.println("This method is currently deprecated."); break;
                case 4: System.out.println("This method is currently deprecated."); break;
            }

    }
    private   void handle_DEPS(Faculty f) {
        int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.FAC; break;
                case 2: f.getDepartmentService().add(); break;
                case 3: f.getDepartmentService().delete(); break;
                case 4: {
                    current_department = f.getDepartmentService().findById();
                    current_level = MenuLevel.DEPARTAMENT;
                    if(current_department == null){
                        current_level = MenuLevel.FAC;
                    }
                    break;
                }
                case 5: f.getDepartmentService().showAll(); break;
            }
    }

    private   void handle_DEPARTAMENT(Department d) {
        int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.DEPS; break;
                case 2: d.getTeacherService().add(); break;
                case 3: d.getTeacherService().delete(); break;
                case 4: d.getTeacherService().findById(); break;
                case 5: d.getTeacherService().showAll(); break;
            }
    }
    private static int readInt(){
        Scanner scanner = InitScanner.try_init_scanner();
        for(;;) {
            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer.");
                scanner.next();
                continue;
            }
            return choice;
        }
    }
    public MenuLevel handle(MenuLevel lvl){
        switch (lvl){
            case MON:
                handle_MON();
                break;
            case UNI:
                handle_UNI(current_university);
                break;
            case FAC:
                handle_FAC(current_faculty);
                break;
            case DEPS:
                handle_DEPS(current_faculty);
                break;
            case DEPARTAMENT:
                handle_DEPARTAMENT(current_department);
                break;
                default: break;
        }
        return current_level;
    }
}
