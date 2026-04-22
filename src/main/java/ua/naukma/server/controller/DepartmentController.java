package ua.naukma.server.controller;

import ua.naukma.domain.Teacher;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.network.dto.UpdateDepartmentDTO;
import ua.naukma.network.dto.UpdateTeacherAcademicDTO;
import ua.naukma.network.dto.UpdateTeacherContactsDTO;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.DepartmentService;
import ua.naukma.server.service.StudentService;
import ua.naukma.server.service.TeacherService;
import ua.naukma.server.service.UserService;

import java.util.List;

@CommandRoute({
        Request.RequestType.SET_HEAD,
        Request.RequestType.ADD,
        Request.RequestType.REMOVE,
        Request.RequestType.FIND,
        Request.RequestType.GET_ALL,
        Request.RequestType.SORT_BY_ID,
        Request.RequestType.SORT_BY_ALPHABETIC_NAME,
        Request.RequestType.UPDATE_TEACHER_CONTACTS,
        Request.RequestType.UPDATE_TEACHER_ACADEMIC,
        Request.RequestType.FIND_TEACHER_BY_PIB,
        Request.RequestType.SORT_DEPT_TEACHERS_BY_NAME
})
public class DepartmentController implements RequestHandler {
    private final DepartmentService departmentService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public DepartmentController(DepartmentService departmentService, StudentService studentService,TeacherService teacherService) {
        this.departmentService = departmentService;
        this.teacherService = teacherService;
        this.studentService = studentService;
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
            case FIND_DEP -> {
                int departmentId = (int) request.getData();
                yield execute(
                        () -> departmentService.findById(departmentId),
                        request.getType());
            }
            case GET_DEPT_STUDENTS -> {
                int departmentId = (int) request.getData();
                yield execute(
                        () -> studentService.findAllByDepartmentId(departmentId),
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
            case UPDATE_TEACHER_CONTACTS -> {
                UpdateTeacherContactsDTO dto = (UpdateTeacherContactsDTO) request.getData();
                yield execute(() -> teacherService.updateContacts(dto.teacherId(), dto.phoneNumber(), dto.email()), request.getType());
            }
            case UPDATE_TEACHER_ACADEMIC -> {
                UpdateTeacherAcademicDTO dto = (UpdateTeacherAcademicDTO) request.getData();
                yield execute(() -> teacherService.updateAcademicInfo(dto.teacherId(), dto.position(), dto.degree(), dto.rank(), dto.load()), request.getType());
            }
            case FIND_TEACHER_BY_PIB -> {
                String[] pib = (String[]) request.getData();
                yield execute(() -> teacherService.findByPIB(pib[0], pib[1], pib[2]), request.getType());
            }
            case SORT_DEPT_TEACHERS_BY_NAME -> {
                int departmentId = (int) request.getData();
                yield execute(() -> teacherService.findAllByDepartmentIdSortedByName(departmentId), request.getType());
            }
            case GET_FAC_TEACHERS_BY_NAME -> {
                int facultyId = (int) request.getData();
                yield execute(
                        () -> teacherService.findAllByFacultyIdSortedByName(facultyId),
                        request.getType()
                );
            }
            case UPDATE_DEPARTMENT -> {
                UpdateDepartmentDTO dto = (UpdateDepartmentDTO) request.getData();
                yield execute(
                        () -> departmentService.updateDepartmentInfo(dto.departmentId(), dto.location(), dto.email()),
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command in DepartmentController");
        };
    }
}