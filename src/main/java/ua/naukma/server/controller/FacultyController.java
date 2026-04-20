package ua.naukma.server.controller;

import ua.naukma.domain.Faculty;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.FacultyService;
import ua.naukma.server.service.TeacherService;

import java.util.List;

@CommandRoute({
        Request.RequestType.SET_DEAN,
        Request.RequestType.ADD,
        Request.RequestType.REMOVE,
        Request.RequestType.FIND,
        Request.RequestType.GET_ALL,
        Request.RequestType.SORT_BY_ID,
        Request.RequestType.SORT_BY_ALPHABETIC_NAME
})
public class FacultyController implements RequestHandler {
    private final FacultyService facultyService;
    private final TeacherService teacherService;

    public FacultyController(FacultyService facultyService, TeacherService teacherService) {
        this.facultyService = facultyService;
        this.teacherService = teacherService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case SET_DEAN -> {
                int[] ids = (int[]) request.getData();
                yield execute(
                        () -> facultyService.setDean(ids[0], ids[1]),
                        request.getType()
                );
            }
            case ADD -> {
                Faculty facultyToAdd = (Faculty) request.getData();
                yield execute(
                        () -> facultyService.add(facultyToAdd),
                        request.getType()
                );
            }
            case REMOVE -> {
                int facultyId = (int) request.getData();
                yield execute(
                        () -> facultyService.deleteById(facultyId),
                        request.getType()
                );
            }
            case FIND -> {
                int facultyId = (int) request.getData();
                yield execute(
                        () -> facultyService.findById(facultyId),
                        request.getType()
                );
            }
            case GET_ALL -> {
                yield execute(
                        facultyService::findAll,
                        request.getType()
                );
            }
            case SORT_BY_ID ->  {
                yield execute(
                        facultyService::sortByIds,
                        request.getType()
                );
            }
            case  SORT_BY_ALPHABETIC_NAME ->  {
                yield execute(
                        facultyService::sortByName,
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command in FacultyController");
        };
    }
}