package ua.naukma;

import ua.naukma.domain.*;
import ua.naukma.repository.*;
import ua.naukma.service.*;
import ua.naukma.ui.*;


public class Main {

    static void main() {
        Repository<University, Integer> uniRepo = new InMemoryUniversityRepository();
        UniversityService uniService = new UniversityService(uniRepo);


        NewMenu menu = new NewMenu(uniService);
        menu.main_menu();
    }
}