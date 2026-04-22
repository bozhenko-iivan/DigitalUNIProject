package ua.naukma.server.controller;

import ua.naukma.domain.University;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.GroupService;
import ua.naukma.server.service.StudentService;
import ua.naukma.server.service.UniversityService;

@CommandRoute({
        Request.RequestType.ADD,
        Request.RequestType.REMOVE,
        Request.RequestType.FIND,
        Request.RequestType.GET_ALL,
        Request.RequestType.SORT_BY_ID,
        Request.RequestType.SORT_BY_ALPHABETIC_NAME,
        Request.RequestType.FIND_GROUP_BY_NAME,
        Request.RequestType.GET_UNI_STUDENTS
})
public class UniversityController implements RequestHandler {
    private final UniversityService universityService;
    private final StudentService studentService;
    private final GroupService groupService;

    public UniversityController(UniversityService universityService, StudentService studentService, GroupService groupService) {
        this.universityService = universityService;
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD -> {
                University universityToAdd = (University) request.getData();
                yield execute(
                        () -> universityService.add(universityToAdd),
                        request.getType()
                );
            }
            case REMOVE -> {
                int universityId = (int) request.getData();
                yield execute(
                        () -> universityService.deleteById(universityId),
                        request.getType()
                );
            }
            case FIND -> {
                int universityId = (int) request.getData();
                yield execute(
                        () -> universityService.findById(universityId),
                        request.getType()
                );
            }
            case GET_ALL -> {
                yield execute(
                        universityService::findAll,
                        request.getType()
                );
            }
            case SORT_BY_ID ->  {
                yield execute(
                        universityService::sortByIds,
                        request.getType()
                );
            }
            case SORT_BY_ALPHABETIC_NAME ->  {
                yield execute(
                        universityService::sortByName,
                        request.getType()
                );
            }
            case FIND_GROUP_BY_NAME -> {
                String groupName = (String) request.getData();
                yield execute(
                        () -> groupService.findByName(groupName),
                        request.getType()
                );
            }
            case GET_UNI_STUDENTS -> {
                int uniId = (int) request.getData();
                yield execute(
                        () -> studentService.findAllByUniversityId(uniId),
                        request.getType()
                );
            }
            case FIND_UNI_STUDENTS_BY_COURSE -> {
                int[] data = (int[]) request.getData();
                yield execute(
                        () -> studentService.findAllByUniversityIdAndCourse(data[0], data[1]),
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command in UniversityController");
        };
    }
}