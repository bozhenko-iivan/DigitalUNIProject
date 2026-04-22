package ua.naukma.server.controller;

import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.network.dto.*;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.service.GradeService;
import ua.naukma.server.service.GroupService;
import ua.naukma.server.service.StudentService;

import java.util.List;

import static ua.naukma.network.Request.RequestType.TRANSFER_GROUP;

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
        Request.RequestType.SORT_BY_ALPHABETIC_NAME,
        Request.RequestType.CHANGE_COURSE,
        Request.RequestType.FIND_STUDENT_BY_PIB,
        TRANSFER_GROUP
})
public class StudentController implements RequestHandler {
    private final StudentService studentService;
    private final GradeService gradeService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GradeService gradeService, GroupService groupService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.groupService = groupService;
    }

    @Override
    public Response process(Request request) {
        return switch (request.getType()) {
            case ADD_STUDENT -> {
                Student studentToAdd = (Student) request.getData();
                yield execute(() -> studentService.add(studentToAdd), request.getType());
            }
            case REMOVE_STUDENT -> {
                int idStudentToRemove = (int) request.getData();
                yield execute(() -> studentService.deleteById(idStudentToRemove), request.getType());
            }
            case FIND_STUDENT_BY_ID -> {
                int idStudentToFind = (int) request.getData();
                yield execute(() -> studentService.findById(idStudentToFind), request.getType());
            }
            case GET_ALL_STUDENTS -> {
                Object data = request.getData();
                if (data != null) {
                    yield execute(() -> studentService.findAllByGroupId((int) data), request.getType());
                } else {
                    yield execute(studentService::findAll, request.getType());
                }
            }
            case GET_STUDENTS_COUNT -> {
                int groupId = (int) request.getData();
                yield execute(() -> studentService.getStudentsCount(groupId), request.getType());
            }
            case UPDATE_STUDENT_CONTACTS ->  {
                UpdateContactsDTO studentData = (UpdateContactsDTO) request.getData();
                yield execute(() -> studentService.updateContacts(studentData.studentId(), studentData.phoneNumber(), studentData.email()), request.getType());
            }
            case  UPDATE_STUDENT_STUDY_FORM ->  {
                UpdateStudyFormDTO studentData = (UpdateStudyFormDTO) request.getData();
                yield execute(() -> studentService.updateStudyForm(studentData.studentID(), studentData.studyForm()), request.getType());
            }
            case UPDATE_STUDENT_STATUS ->  {
                UpdateStudentStatusDTO studentData = (UpdateStudentStatusDTO) request.getData();
                yield execute (() -> studentService.updateStudentStatus(studentData.studentID(), studentData.status()), request.getType());
            }
            case SET_STUDENT_GRADE ->  {
                int gradeID = gradeService.generateID();
                SetStudentGrade studentData = (SetStudentGrade) request.getData();
                Grade grade = new Grade(gradeID, studentData.grade(), studentData.studentID(), studentData.subject());
                yield execute(() -> gradeService.add(grade), request.getType());
            }
            case DELETE_STUDENT_GRADE ->  {
                int idToDelete = (int) request.getData();
                yield execute(() -> gradeService.deleteById(idToDelete), request.getType());
            }
            case SHOW_TRANSCRIPT ->  {
                int studentId = (int) request.getData();
                yield execute(() -> gradeService.findByStudentId(studentId), request.getType());
            }

            case SORT_BY_ID ->  {
                Object data = request.getData();
                if (data != null) {
                    yield execute(() -> studentService.sortByIds((int) data), request.getType());
                } else {
                    yield execute(() -> studentService.sortByIds(), request.getType());
                }
            }

            case SORT_BY_ALPHABETIC_NAME ->  {
                Object data = request.getData();
                if (data != null) {
                    yield execute(() -> studentService.sortByName((int) data), request.getType());
                } else {
                    yield execute(() -> studentService.sortByName(), request.getType());
                }
            }
            case CALCULATE_AVG -> {
                int studentId = (int) request.getData();
                yield execute(() -> studentService.calculateAverageScore(studentId), request.getType());
            }
            case UPGRADE_GROUP_COURSE -> {
                int groupId = (int) request.getData();
                yield execute(() -> studentService.upgradeGroupCourse(groupId), request.getType());
            }
            case TRANSFER_GROUP -> {
                TransferDTO dto = (TransferDTO) request.getData();
                yield execute(() -> studentService.transferGroup(dto.studentId(), dto.newGroupID()), request.getType());
            }
            case FIND_STUDENT_BY_PIB -> {
                String[] pib = (String[]) request.getData();
                yield execute(() -> studentService.findByPIB(pib[0], pib[1], pib[2]), request.getType());
            }
            case GET_FAC_STUDENTS_BY_NAME -> {
                int facultyId = (int) request.getData();
                yield execute(() -> studentService.findAllByFacultyIdSortedByName(facultyId), request.getType());
            }
            case GET_FAC_STUDENTS_BY_COURSE -> {
                int facultyId = (int) request.getData();
                yield execute(() -> studentService.findAllByFacultyIdSortedByCourse(facultyId), request.getType());
            }
            case FIND_STUDENTS_BY_COURSE -> {
                int targetCourse = (int) request.getData();
                yield execute(() -> studentService.findAllByCourse(targetCourse), request.getType());
            }
            default -> new Response(Response.ResponseStatus.FAILURE, "Unknown command");
        };
    }
}