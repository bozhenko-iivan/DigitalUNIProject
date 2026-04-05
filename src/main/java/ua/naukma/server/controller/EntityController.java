package ua.naukma.server.controller;

import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.repository.GetId;
import ua.naukma.server.service.Service;

import java.io.Serializable;

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
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }

}
