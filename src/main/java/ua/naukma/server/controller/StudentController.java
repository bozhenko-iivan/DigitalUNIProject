package ua.naukma.server.controller;

import ua.naukma.domain.Student;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.StudentService;

@CommandRoute({
        Request.RequestType.ADD_STUDENT,
        Request.RequestType.REMOVE_STUDENT,
        Request.RequestType.FIND_STUDENT_BY_ID,
        Request.RequestType.GET_ALL_STUDENTS,
})

public class StudentController implements RequestHandler {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_STUDENT -> {
                Student studentToAdd = (Student) request.getData();
                yield execute(
                        () -> studentService.add(studentToAdd),
                        request.getType()
                );
            }
            case REMOVE_STUDENT -> {
                int idStudentToRemove = (int) request.getData();
                yield execute(
                        () -> studentService.deleteById(idStudentToRemove),
                        request.getType()
                );
            }
            case FIND_STUDENT_BY_ID -> {
                int idStudentToFind = (int) request.getData();
                yield execute(
                        () -> studentService.findById(idStudentToFind),
                        request.getType()
                );
            }
            case GET_ALL_STUDENTS -> {
                yield execute(
                        studentService::findAll,
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
