package ua.naukma.server.controller;

import ua.naukma.domain.SystemUser;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.service.UserService;

public class UserController implements RequestHandler {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case LOGIN -> {
                SystemUser credentials = (SystemUser) request.getData();
                yield execute(
                        () -> userService.authenticate(credentials), request.getType()
                );
            }
            case LOGOUT -> {
                yield execute(
                        () -> null, request.getType()
                );
            }
            case ADD_USER -> {
                SystemUser userToAdd = (SystemUser) request.getData();
                yield execute(
                        () -> {
                            userService.add(userToAdd);
                        }, request.getType()
                );
            }
            case REMOVE_USER -> {
                int userIdToRemove = (int) request.getData();
                yield execute(
                        () -> {
                            userService.deleteById(userIdToRemove);
                        }, request.getType()
                );
            }
            case FIND_USER_BY_ID -> {
                int userIdToFind = (int) request.getData();
                yield execute(
                        () -> userService.findById(userIdToFind),
                        request.getType()
                );
            }
            case GET_ALL_USERS -> {
                yield execute(
                        userService::findAll,
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
