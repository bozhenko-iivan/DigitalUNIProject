package ua.naukma.server.controller;

import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.network.dto.SetStudentGrade;
import ua.naukma.network.dto.UpdateContactsDTO;
import ua.naukma.network.dto.UpdateStudentStatusDTO;
import ua.naukma.network.dto.UpdateStudyFormDTO;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.GradeService;
import ua.naukma.server.service.StudentService;

import java.util.List;

@CommandRoute({
        Request.RequestType.ADD_STUDENT,
        Request.RequestType.REMOVE_STUDENT,
        Request.RequestType.FIND_STUDENT_BY_ID,
        Request.RequestType.GET_ALL_STUDENTS,
        Request.RequestType.GET_STUDENTS_COUNT,
        Request.RequestType.UPDATE_STUDENT_CONTACTS,
        Request.RequestType.UPDATE_STUDENT_STUDY_FORM,
        Request.RequestType.UPDATE_STUDENT_STATUS,
        Request.RequestType.SET_STUDENT_GRADE,
        Request.RequestType.DELETE_STUDENT_GRADE,
        Request.RequestType.SHOW_TRANSCRIPT,
        Request.RequestType.SORT_BY_ID,
        Request.RequestType.SORT_BY_ALPHABETIC_NAME
})

public class StudentController implements RequestHandler {
    private final StudentService studentService;
    private final GradeService gradeService;

    public StudentController(StudentService studentService, GradeService gradeService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
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
            case GET_ALL_STUDENTS -> execute(
                    studentService::findAll,
                    request.getType()
            );
            case GET_STUDENTS_COUNT -> {
                int groupId = (int) request.getData();
                yield execute(
                        () -> studentService.getStudentsCount(groupId),
                        request.getType()
                );
            }
            case UPDATE_STUDENT_CONTACTS ->  {
                UpdateContactsDTO studentData = (UpdateContactsDTO) request.getData();
                int studentId = studentData.studentId();
                String phoneNumber = studentData.phoneNumber();
                String email = studentData.email();
                yield execute(
                        () -> studentService.updateContacts(studentId, phoneNumber, email),
                        request.getType()
                );
            }
            case  UPDATE_STUDENT_STUDY_FORM ->  {
                UpdateStudyFormDTO studentData = (UpdateStudyFormDTO) request.getData();
                int studentId = studentData.studentID();
                StudyForm studyForm = studentData.studyForm();
                yield execute(
                        () -> studentService.updateStudyForm(studentId, studyForm),
                        request.getType()
                );
            }
            case UPDATE_STUDENT_STATUS ->  {
                UpdateStudentStatusDTO studentData = (UpdateStudentStatusDTO) request.getData();
                int studentId = studentData.studentID();
                StudentStatus studentStatus = studentData.status();
                yield execute (
                        () -> studentService.updateStudentStatus(studentId, studentStatus),
                        request.getType()
                );
            }
            case SET_STUDENT_GRADE ->  {
                int gradeID = gradeService.generateID();
                SetStudentGrade studentData = (SetStudentGrade) request.getData();
                int studentId = studentData.studentID();
                int score = studentData.grade();
                Subject subject = studentData.subject();
                Grade grade = new Grade(gradeID ,score, studentId, subject);
                yield execute(
                        () -> gradeService.add(grade),
                        request.getType()
                );
            }
            case DELETE_STUDENT_GRADE ->  {
                int idToDelete = (int) request.getData();
                yield execute(
                        () -> gradeService.deleteById(idToDelete), request.getType()
                );
            }
            case SHOW_TRANSCRIPT ->  {
                int studentId = (int) request.getData();
                yield execute(
                        () -> gradeService.findByStudentId(studentId),
                        request.getType()
                );
            }
            case SORT_BY_ID ->  {
                yield execute(
                        studentService::sortByIds,
                        request.getType()
                );
            }
            case  SORT_BY_ALPHABETIC_NAME ->  {
                yield execute(
                        studentService::sortByName,
                        request.getType()
                );
            }
            case CALCULATE_AVG -> {
                int studentId = (int) request.getData();
                yield execute(
                        () -> studentService.calculateAverageScore(studentId),
                        request.getType()
                );
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}
