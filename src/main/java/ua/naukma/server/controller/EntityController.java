package ua.naukma.server.controller;

import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.repository.GetId;
import ua.naukma.server.service.Service;
import java.io.Serializable;
import java.util.List;

@CommandRoute({
    Request.RequestType.ADD,
            Request.RequestType.REMOVE,
            Request.RequestType.FIND,
            Request.RequestType.GET_ALL,
        Request.RequestType.SORT_BY_ID,
        Request.RequestType.SORT_BY_ALPHABETIC_NAME
})

public class EntityController<A extends Serializable & GetId, T extends Service<A, Integer>> implements RequestHandler {
    private final T service;

    public EntityController(T service) {
        this.service = service;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD -> {
                A toAdd = (A) request.getData();
                yield execute(
                        () -> service.add(toAdd),
                        request.getType()
                );
            }
            case REMOVE -> {
                int idToRemove = (int) request.getData();
                yield execute(
                        () -> service.deleteById(idToRemove),
                        request.getType()
                );
            }
            case FIND -> {
                int idToFind = (int) request.getData();
                yield execute(
                        () -> service.findById(idToFind),
                        request.getType()
                );
            }
            case GET_ALL -> execute(
                    service::findAll,
                    request.getType()
            );
            case LOGOUT -> execute(
                    () -> null, request.getType()
            );
            case SORT_BY_ID ->  {
                yield execute(
                       service::sortByIds,
                        request.getType()
                );
            }
            case  SORT_BY_ALPHABETIC_NAME ->  {
                yield execute(
                       service::sortByName,
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }

}
