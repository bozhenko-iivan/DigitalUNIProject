package ua.naukma.ui;

import ua.naukma.domain.*;
import ua.naukma.security.Permissions;
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
    private Group current_group;
    private SystemUser current_user;

    private UniversityService universityService;
    private StudentService studentService;
    private FacultyService facultyService;
    private DepartmentService departmentService;
    private GroupService groupService;
    private TeacherService teacherService;
    private UserService userService;

    public MenuOptionsHandler(MenuLevel current_level, UniversityService universityService, UserService userService, SystemUser user) {
        this.current_level = current_level;
        this.universityService = universityService;
        this.userService = userService;
        this.current_user = user;
    }

    private void handle_MON() {
        int choice = readInt();
        switch (choice) {
            case 1: System.out.println("Exiting.."); System.exit(0); break;
            case 2 :
                if (current_user.hasPermission(Permissions.ADD_UNIVERSITY)) {
                universityService.add();
            } else {
                System.out.println("Access Denied: You cannot add universities.");
            }
            break;
            case 3: {
                if (current_user.hasPermission(Permissions.DELETE_UNIVERSITY)) {
                    universityService.delete();
                } else {
                    System.out.println("Access Denied: You cannot delete universities.");
                }
                break;
            }
            case 4:
            University found = universityService.findById();
            if (found != null) {
                current_university = found;
                current_level = MenuLevel.UNI;
                facultyService = new FacultyService(current_university);
            }
            break;
            case 5: universityService.showAll(); break;
            case 6:
                if ((current_user.hasPermission(Permissions.MANAGE_USERS))) {
                    current_level = MenuLevel.ADMIN_PANEL;
                } else {
                    System.out.println("You do not have permissions to do this");
                }
                break;
            case 7:
                System.out.println("Logging out...");
                this.current_user = userService.login();
                break;
        }
    }

    private void handle_UNI(University u) {
            int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.MON; break;
                case 2: facultyService.add(); break;
                case 3: facultyService.delete(); break;
                case 4: {
                    current_faculty = facultyService.workWithFaculty(current_level);
                    if(current_faculty != null){
                        current_level = MenuLevel.FAC;
                        departmentService = new DepartmentService(current_university, current_faculty);
                    } else {
                        current_level = MenuLevel.UNI;
                    }
                    break;
                }
                case 5: facultyService.showAll(); break;
            }
    }

    private void handle_GRPS(Faculty f) {
        int choice = readInt();
        switch (choice){
            case 1: current_level = MenuLevel.FAC; break;
            case 2: groupService.add(); break;
            case 3: groupService.delete(); break;
            case 4: {
                current_group = groupService.findById();
                if (current_group != null){
                    current_level = MenuLevel.GROUP;
                    studentService = new StudentService(current_university, current_group);
                }
                break;
            }
            case 5: groupService.showAll(); break;
        }
    }

    private void handle_GROUP(Group g) {
        int choice = readInt();
        switch (choice){
            case 1: current_level = MenuLevel.GRPS; break;
            case 2:
                if (current_user.hasPermission(Permissions.MANAGE_USERS)) {
                    studentService.add();
                } else {
                    System.out.println("Access Denied: You cannot add students.");
                } break;
            case 3:
                if (current_user.hasPermission(Permissions.MANAGE_USERS)) {
                    studentService.delete();
                } else {
                    System.out.println("Access Denied: You cannot delete students.");
                } break;
            case 4: studentService.findById(); break;
            case 5: studentService.showAll(); break;
        }
    }
    private   void handle_FAC(Faculty f) {
//        System.out.println("Current operations:\n" +
//                "1. Go higher\n" +
//                "2. Go to departaments\n" +
//                //"3. Go to groups\n" +
//                "4. Show faculty info\n");
        int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.UNI; break;
                case 2: current_level = MenuLevel.DEPS; break;
                case 3: {
                    current_level = MenuLevel.GRPS;
                    groupService = new GroupService(current_university, current_faculty);
                }break;
                case 4: System.out.println("This method is currently deprecated."); break;
            }

    }
    private void handle_DEPS(Faculty f) {
        int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.FAC; break;
                case 2: departmentService.add(); break;
                case 3: departmentService.delete(); break;
                case 4: {
                    current_department = departmentService.findById();
                    if(current_department != null){
                        current_level = MenuLevel.DEPARTAMENT;
                        teacherService = new TeacherService(current_university, current_department);
                        //groupService = new GroupService(current_university, current_department);
                    } else {
                        current_level = MenuLevel.DEPS;
                    }
                    break;
                }
                case 5: departmentService.showAll(); break;
            }
    }

    private   void handle_DEPARTAMENT(Department d) {
        int choice = readInt();
            switch (choice){
                case 1: current_level = MenuLevel.DEPS; break;
                case 2: teacherService.add(); break;
                case 3: teacherService.delete(); break;
                case 4: teacherService.findById(); break;
                case 5: teacherService.showAll(); break;
                //case 6: current_level = MenuLevel.GRPS; break;
            }
    }

    private void handle_ADMIN_PANEL() {
        int choice = readInt();
        switch (choice){
            case 1: current_level = MenuLevel.MON; break;
            case 2: userService.add(); break;
            case 3: userService.delete(); break;
            case 4: userService.findById(); break;
            case 5: userService.showAll(); break;
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
                case GRPS:
                    handle_GRPS(current_faculty);
                    break;
                    case GROUP:
                        handle_GROUP(current_group);
                        break;
                default: break;
            case ADMIN_PANEL:
                handle_ADMIN_PANEL(); break;
        }
        return current_level;
    }
}
