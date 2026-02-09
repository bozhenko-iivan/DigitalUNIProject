package ua.naukma;

import ua.naukma.domain.Student;
import ua.naukma.domain.StudentStatus;
import ua.naukma.domain.StudyForm;
import ua.naukma.repository.*;
import ua.naukma.service.*;
import ua.naukma.ui.*;


import java.time.LocalDate;

public class Main {
    static void main() {
        StudentRepository repository = new InMemoryStudentRepository();

        StudentService service = new StudentService(repository);

        var menu = new ConsoleMenu(service);
        menu.showMenu();

    }
}
