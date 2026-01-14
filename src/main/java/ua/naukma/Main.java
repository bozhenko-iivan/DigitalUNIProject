package ua.naukma;

import ua.naukma.domain.Student;
import ua.naukma.domain.StudentStatus;
import ua.naukma.domain.StudyForm;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.StudentRepository;
import ua.naukma.service.StudentService;
import ua.naukma.ui.ConsoleMenu;

import java.time.LocalDate;

public class Main {
    static void main() {
        StudentRepository repository = new InMemoryStudentRepository();

        StudentService service = new StudentService(repository);

        ConsoleMenu menu = new ConsoleMenu(service);

        menu.showMenu();
    }
}
