package ua.naukma;

import ua.naukma.domain.*;
import ua.naukma.repository.*;
import ua.naukma.service.*;
import ua.naukma.ui.*;


public class Main {

    static void main() {
        //Repository<University, Integer> uniRepo = new InMemoryUniversityRepository();
        Repository<University, Integer> uniRepo = new FileUniversityRepository();
        UniversityService uniService = new UniversityService(uniRepo);
        Repository<SystemUser, Integer> userRepository = new FileUserRepository();
        UserService userService = new UserService(userRepository);
        userService.initUser();
        SystemUser loggedUser = userService.login();

        NewMenu menu = new NewMenu(uniService, userService, loggedUser);
        menu.main_menu();
    }
}