package ua.naukma.server.controller;

import ua.naukma.domain.Student;
import ua.naukma.domain.StudentStatus;
import ua.naukma.domain.StudyForm;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.StudentService;

@CommandRoute({
        Request.RequestType.ADD_STUDENT,
        Request.RequestType.REMOVE_STUDENT,
        Request.RequestType.FIND_STUDENT_BY_ID,
        Request.RequestType.GET_ALL_STUDENTS,
        Request.RequestType.GET_STUDENTS_COUNT,
        Request.RequestType.UPDATE_STUDENT_CONTACTS,
        Request.RequestType.UPDATE_STUDENT_STUDY_FORM,
        Request.RequestType.UPDATE_STUDENT_STATUS
})

public class StudentController implements RequestHandler {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_STUDENT -> {
                Student studentToAdd = (Student) request.getData();
                yield execute(
                        () -> studentService.add(studentToAdd),
                        request.getType()
                );
            }
            case REMOVE_STUDENT -> {
                int idStudentToRemove = (int) request.getData();
                yield execute(
                        () -> studentService.deleteById(idStudentToRemove),
                        request.getType()
                );
            }
            case FIND_STUDENT_BY_ID -> {
                int idStudentToFind = (int) request.getData();
                yield execute(
                        () -> studentService.findById(idStudentToFind),
                        request.getType()
                );
            }
            case GET_ALL_STUDENTS -> {
                yield execute(
                        studentService::findAll,
                        request.getType()
                );
            }
            case GET_STUDENTS_COUNT -> {
                int groupId = (int) request.getData();
                yield execute(
                        () -> studentService.getStudentsCount(groupId),
                        request.getType()
                );
            }
            case UPDATE_STUDENT_CONTACTS ->  {
                Object[] studentData = (Object[]) request.getData();
                int studentId = (int) studentData[0];
                String phoneNumber = (String) studentData[1];
                String email = (String) studentData[2];
                yield execute(
                        () -> studentService.updateContacts(studentId, phoneNumber, email),
                        request.getType()
                );
            }
            case  UPDATE_STUDENT_STUDY_FORM ->  {
                Object[] studentData = (Object[]) request.getData();
                int studentId = (int) studentData[0];
                StudyForm studyForm = (StudyForm) studentData[1];
                yield execute(
                        () -> studentService.updateStudyForm(studentId, studyForm),
                        request.getType()
                );
            }
            case  UPDATE_STUDENT_STATUS ->  {
                Object[] studentData = (Object[]) request.getData();
                int studentId = (int) studentData[0];
                StudentStatus studentStatus = (StudentStatus) studentData[1];
                yield execute (
                        () -> studentService.updateStudentStatus(studentId, studentStatus),
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
