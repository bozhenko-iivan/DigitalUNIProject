package ua.naukma.client.handler;

import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.utils.AcademicInfoVerificator;
import ua.naukma.client.utils.EmailVerificator;
import ua.naukma.client.utils.GradeVerificator;
import ua.naukma.client.utils.PhoneNumberVerificator;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.network.dto.SetStudentGrade;
import ua.naukma.network.dto.UpdateContactsDTO;
import ua.naukma.network.dto.UpdateStudentStatusDTO;
import ua.naukma.network.dto.UpdateStudyFormDTO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentHandler extends BasicHandler {
    public StudentHandler(ObjectInputStream in, ObjectOutputStream out, MenuContext menuContext) {
        super(menuContext, out, in);
    }
    @Override
    public void handle() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> go_higher();
            case 2 -> show_info();
            case 3 -> update_contacts();
            case 4 -> update_study_form();
            case 5 -> update_student_status();
            case 6 -> set_grade();
            case 7 -> {
                List<Grade> transcript = show_transcript();
                if (transcript != null) {
                    delete_grade(transcript);
                }
            }
            case 8 -> show_transcript();
            case 9 -> calculate_avg();
            default -> System.out.println("Invalid choice.");
        }
    }
    private void go_higher() {
        menuContext.setCurrent_level(MenuLevel.GROUP);
        menuContext.setCurrent_student(null);
    }
    private void show_info() {
        Response showStudentInfo = sendRequest(Request.RequestType.FIND_STUDENT_BY_ID, menuContext.getCurrent_student().getId(), false);
        if (showStudentInfo.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Student student = (Student) showStudentInfo.getPayload();
            System.out.println(student.toString());
        }
    }
    private void update_contacts() {
        int studentId = menuContext.getCurrent_student().getId();
        String phoneNumber = PhoneNumberVerificator.ask_phonenum();
        String email = EmailVerificator.ask_email();
        UpdateContactsDTO studentData = new UpdateContactsDTO(studentId, phoneNumber, email);
        Response updateResponse = sendRequest(Request.RequestType.UPDATE_STUDENT_CONTACTS, studentData, false);
        if (updateResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            menuContext.setCurrent_student((Student) updateResponse.getPayload());
        }
    }
    private void update_study_form() {
        int studentId = menuContext.getCurrent_student().getId();
        StudyForm studyForm = AcademicInfoVerificator.ask_study_form();
        UpdateStudyFormDTO studentData = new UpdateStudyFormDTO(studentId, studyForm);
        Response updateResponse = sendRequest(Request.RequestType.UPDATE_STUDENT_STUDY_FORM, studentData, false);
        if (updateResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            menuContext.setCurrent_student((Student) updateResponse.getPayload());
        }
    }
    private void update_student_status() {
        int studentId = menuContext.getCurrent_student().getId();
        StudentStatus status = AcademicInfoVerificator.ask_student_status();
        UpdateStudentStatusDTO studentData = new UpdateStudentStatusDTO(studentId, status);
        Response updateResponse = sendRequest(Request.RequestType.UPDATE_STUDENT_STATUS, studentData, false);
        if (updateResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            menuContext.setCurrent_student((Student) updateResponse.getPayload());
        }
    }
    private void set_grade() {
        int studentId = menuContext.getCurrent_student().getId();
        Subject subject = GradeVerificator.askSubject();
        int score = GradeVerificator.askScore();
        SetStudentGrade studentData = new SetStudentGrade(studentId, score, subject);
        Response updateResponse = sendRequest(Request.RequestType.SET_STUDENT_GRADE, studentData, false);
        System.out.println(updateResponse.getMsg());
    }
    private void delete_grade(List<Grade> currentGrades) {
        if (currentGrades.isEmpty()) {
            System.out.println("No grades found for this student.");
        } else {
            int gradeID = GradeVerificator.askGradeID(currentGrades);
            sendRequest(Request.RequestType.DELETE_STUDENT_GRADE, gradeID, false);
        }
    }
    private List<Grade> show_transcript() {
        int studentId = menuContext.getCurrent_student().getId();
        Response response = sendRequest(Request.RequestType.SHOW_TRANSCRIPT, studentId, false);
        if (response.getResponseStatus() != null && response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            List<Grade> grades = (List<Grade>) response.getPayload();
            Map<Subject, List<Grade>> map = grades.stream()
                    .collect(Collectors.groupingBy(Grade::getSubjectName));
            System.out.println("========= STUDENT " + menuContext.getCurrent_student().getLastName().toUpperCase() + " TRANSCRIPT =========");
            for (Subject sub : Subject.values()) {
                System.out.printf("%-20s | ", sub);
                List<Grade> gradeList = map.getOrDefault(sub, new ArrayList<>());
                if (gradeList.isEmpty()) {
                    System.out.println("—");
                } else {
                    String gradeString = gradeList.stream()
                            .map(g -> "(id: " + g.getId() + ") " + g.getScore())
                            .collect(Collectors.joining(" | "));
                    System.out.println(gradeString);
                }
            }
            return grades;
        }
        return null;
    }

    private void calculate_avg() {
        int studentId = menuContext.getCurrent_student().getId();
        Response response = sendRequest(Request.RequestType.CALCULATE_AVG, studentId, false);
        if (response.getResponseStatus() != null && response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
            Double avg = (Double) response.getPayload();
            System.out.println("Student's " + studentId + " GPA is: " + avg);
        }
    }
}
