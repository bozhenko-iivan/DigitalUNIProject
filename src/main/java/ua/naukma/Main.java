package ua.naukma;

import ua.naukma.domain.*;
import ua.naukma.repository.*;
import ua.naukma.service.*;
import ua.naukma.ui.*;


public class Main {

    static void main() {
        Repository<University, Integer> uniRepo = new InMemoryUniversityRepository();
        UniversityService uniService = new UniversityService(uniRepo);
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        userService.initUser();
        SystemUser loggedUser = userService.login();

        NewMenu menu = new NewMenu(uniService, userService, loggedUser);
        menu.main_menu();
    }
}