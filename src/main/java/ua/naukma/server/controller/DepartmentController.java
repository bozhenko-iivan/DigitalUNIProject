package ua.naukma.server.controller;

import ua.naukma.domain.Department;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.DepartmentService;

@CommandRoute({
        Request.RequestType.ADD_DEPARTMENT,
        Request.RequestType.REMOVE_DEPARTMENT,
        Request.RequestType.FIND_DEPARTMENT_BY_ID,
        Request.RequestType.GET_ALL_DEPARTMENTS
})

public class DepartmentController implements RequestHandler {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_DEPARTMENT -> {
                Department departmentToAdd = (Department) request.getData();
                yield execute(
                        () -> departmentService.add(departmentToAdd),
                        request.getType()
                );
            }
            case REMOVE_DEPARTMENT -> {
                int idDepartmentToRemove = (int) request.getData();
                yield execute(
                        () -> departmentService.deleteById(idDepartmentToRemove),
                        request.getType()
                );
            }
            case FIND_DEPARTMENT_BY_ID -> {
                int idDepartmentToFind = (int) request.getData();
                yield execute(
                        () -> departmentService.findById(idDepartmentToFind),
                        request.getType()
                );
            }
            case GET_ALL_DEPARTMENTS -> execute(
                    departmentService::findAll,
                    request.getType()
            );
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
