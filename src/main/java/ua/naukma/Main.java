package ua.naukma;

import ua.naukma.domain.Student;
import ua.naukma.repository.InMemoryStudentRepository;
import ua.naukma.repository.Repository;
import ua.naukma.service.StudentService;
import ua.naukma.ui.NewMenu; // Імпортуємо нове меню

public class Main {
    public static void main(String[] args) {
        Repository<Student, Integer> repository = new InMemoryStudentRepository();
        StudentService service = new StudentService(repository);

        NewMenu menu = new NewMenu(service);
        menu.start();
    }
}
