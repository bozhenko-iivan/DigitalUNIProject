package ua.naukma.server.controller;

import ua.naukma.domain.University;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.UniversityService;

@CommandRoute({
        Request.RequestType.ADD_UNIVERSITY,
        Request.RequestType.REMOVE_UNIVERSITY,
        Request.RequestType.FIND_UNIVERSITY_BY_ID,
        Request.RequestType.GET_ALL_UNIVERSITIES
})

public class UniversityController implements RequestHandler {
    private final UniversityService uniService;

    public UniversityController(UniversityService uniService) {
        this.uniService = uniService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_UNIVERSITY -> {
                University uniToAdd = (University) request.getData();
                yield execute(
                        () -> {
                            uniService.add(uniToAdd);
                        },
                        request.getType()
                );
            }
            case REMOVE_UNIVERSITY -> {
                int idUniToRemove = (int) request.getData();
                yield execute(
                        () -> {
                            uniService.deleteById(idUniToRemove);
                        },
                        request.getType()
                );
            }
            case FIND_UNIVERSITY_BY_ID ->  {
                int uniId = (int) request.getData();
                yield execute(
                        () -> uniService.findById(uniId),
                        request.getType()
                );
            }
            case GET_ALL_UNIVERSITIES -> execute(
                    uniService::findAll,
                    request.getType()
            );
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
