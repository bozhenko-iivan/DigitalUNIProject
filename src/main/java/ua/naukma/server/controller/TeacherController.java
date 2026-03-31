package ua.naukma.server.controller;

import ua.naukma.domain.Teacher;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.TeacherService;

@CommandRoute({
        Request.RequestType.ADD_TEACHER,
        Request.RequestType.REMOVE_TEACHER,
        Request.RequestType.FIND_TEACHER_BY_ID,
        Request.RequestType.GET_ALL_TEACHERS,
})

public class TeacherController implements RequestHandler {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_TEACHER -> {
                Teacher teacherToAdd = (Teacher) request.getData();
                yield execute(
                        () -> teacherService.add(teacherToAdd),
                        request.getType()
                );
            }
            case REMOVE_TEACHER -> {
                int idTeacherToRemove = (int) request.getData();
                yield execute(
                        () -> teacherService.deleteById(idTeacherToRemove),
                        request.getType()
                );
            }
            case FIND_TEACHER_BY_ID -> {
                int idTeacherToFind = (int) request.getData();
                yield execute(
                        () -> teacherService.findById(idTeacherToFind),
                        request.getType()
                );
            }
            case GET_ALL_TEACHERS -> execute(
                    teacherService::findAll,
                    request.getType()
            );
          default -> new Response(Response.ResponseStatus.FAILURE, "Unknow command");
        };
    }
}
