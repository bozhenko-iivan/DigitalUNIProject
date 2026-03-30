package ua.naukma.server.controller;

import ua.naukma.domain.Group;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.GroupService;

@CommandRoute({
        Request.RequestType.ADD_GROUP,
        Request.RequestType.REMOVE_GROUP,
        Request.RequestType.FIND_GROUP_BY_ID,
        Request.RequestType.GET_ALL_GROUPS,
})

public class GroupController implements RequestHandler {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_GROUP -> {
                Group groupToAdd = (Group) request.getData();
                yield execute(
                        () -> groupService.add(groupToAdd),
                        request.getType()
                );
            }
            case REMOVE_GROUP -> {
                int idGroupToRemove = (int) request.getData();
                yield execute(
                        () -> groupService.deleteById(idGroupToRemove),
                        request.getType()
                );
            }
            case FIND_GROUP_BY_ID -> {
                int idGroupToFind = (int) request.getData();
                yield execute(
                        () -> groupService.findById(idGroupToFind),
                        request.getType()
                );
            }
            case GET_ALL_GROUPS -> {
                yield execute(
                        groupService::findAll,
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
