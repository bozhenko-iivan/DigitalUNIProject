package ua.naukma.server.controller;

import ua.naukma.domain.Teacher;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.DepartmentService;
import ua.naukma.server.service.TeacherService;

import java.util.List;

@CommandRoute({
        Request.RequestType.SET_HEAD,
        Request.RequestType.ADD,
        Request.RequestType.REMOVE,
        Request.RequestType.FIND,
        Request.RequestType.GET_ALL,
        Request.RequestType.SORT_BY_ID,
        Request.RequestType.SORT_BY_ALPHABETIC_NAME
})
public class DepartmentController implements RequestHandler {
    private final DepartmentService departmentService;
    private final TeacherService teacherService;

    public DepartmentController(DepartmentService departmentService, TeacherService teacherService) {
        this.departmentService = departmentService;
        this.teacherService = teacherService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case SET_HEAD -> {
                int[] ids = (int[]) request.getData();
                yield execute(
                        () -> departmentService.setHead(ids[0], ids[1]),
                        request.getType()
                );
            }
            case ADD -> {
                Teacher teacherToAdd = (Teacher) request.getData();
                yield execute(
                        () -> teacherService.add(teacherToAdd),
                        request.getType()
                );
            }
            case REMOVE -> {
                int teacherId = (int) request.getData();
                yield execute(
                        () -> teacherService.deleteById(teacherId),
                        request.getType()
                );
            }
            case FIND -> {
                int teacherId = (int) request.getData();
                yield execute(
                        () -> teacherService.findById(teacherId),
                        request.getType()
                );
            }
            case GET_ALL -> {
                int departmentId = (int) request.getData();
                yield execute(
                        () -> teacherService.findAllByDepartmentId(departmentId),
                        request.getType()
                );
            }
            case SORT_BY_ID ->  {
                yield execute(
                        teacherService::sortByIds,
                        request.getType()
                );
            }
            case  SORT_BY_ALPHABETIC_NAME ->  {
                yield execute(
                        teacherService::sortByName,
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command in DepartmentController");
        };
    }
}