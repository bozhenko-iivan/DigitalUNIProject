package ua.naukma.server.controller;

import ua.naukma.domain.Faculty;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.service.FacultyService;

import java.util.List;

public class FacultyController implements RequestHandler {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_FACULTY -> {
                Faculty facultyToAdd = (Faculty) request.getData();
                yield execute(
                        () -> {
                            facultyService.add(facultyToAdd);
                            return null;
                        },
                        request.getType()
                );
            }
            case REMOVE_FACULTY -> {
                int idFacultyToRemove = (int) request.getData();
                yield execute(
                        () -> {
                            facultyService.deleteById(idFacultyToRemove);
                            return null;
                        },
                        request.getType()
                );
            }
            case FIND_FACULTY_BY_ID ->  {
                int idFaculty = (int) request.getData();
                yield execute(
                        () -> facultyService.findById(idFaculty),
                        request.getType()
                );
            }
            case GET_ALL_FACULTIES -> {
                yield execute(
                        facultyService::findAll,
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
