package ua.naukma.client.ui;

import ua.naukma.client.utils.*;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.security.Permissions;
import ua.naukma.server.service.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuOptionsHandler{
    private MenuLevel current_level;
    private Faculty current_faculty;
    private Department current_department;
    private University current_university;
    private Group current_group;
    private SystemUser current_user;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public MenuOptionsHandler(MenuLevel current_level, ObjectOutputStream oos, ObjectInputStream ois, SystemUser user) {
        this.current_level = current_level;
        this.oos = oos;
        this.ois = ois;
        this.current_user = user;
    }

    private boolean isIdAlreadyTaken(int id, Request.RequestType requestType) throws IOException {
        try {
            Request checkIfIdPresent = new Request(requestType, id);
            oos.writeObject(checkIfIdPresent);
            oos.flush();

            Response idResponse = (Response) ois.readObject();
            return idResponse.getResponseStatus() == Response.ResponseStatus.SUCCESS;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while trying to read response from server");
            return true;
        }
    }

    private void handle_MON() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice) {
            case 1:
                System.out.println("Exiting..");
                System.exit(0);
                break;
            case 2:
                if (current_user.hasPermission(Permissions.ADD_UNIVERSITY)) {
                    try {
                        int university_id = IdVerificator.ask_id();

                        if (isIdAlreadyTaken(university_id, Request.RequestType.FIND_UNIVERSITY_BY_ID)) {
                            System.out.println("This id is already taken. Please try choose another id");
                            break;
                        }

                        String fullName = UniversityVerificator.ask_full_name("fullName");
                        String shortName = UniversityVerificator.ask_short_name("shortName");
                        String city = UniversityVerificator.ask_city();
                        String address = UniversityVerificator.ask_address();

                        University newUni = new University(university_id, fullName, shortName, city, address);

                        Request addRequest = new Request(Request.RequestType.ADD_UNIVERSITY, newUni);
                        oos.writeObject(addRequest);
                        oos.flush();
                        Response response = (Response) ois.readObject();
                        System.out.println(response.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Access Denied: You cannot add universities.");
                }
                break;
            case 3:
                if (current_user.hasPermission(Permissions.DELETE_UNIVERSITY)) {
                    try {
                        int idToDelete = IdVerificator.ask_id();
                        Request req = new Request(Request.RequestType.REMOVE_UNIVERSITY, idToDelete);
                        oos.writeObject(req);
                        oos.flush();

                        Response res = (Response) ois.readObject();
                        System.out.println(res.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Access Denied: You cannot delete universities.");
                }
                break;
            case 4:
                try {
                    int idToFind = IdVerificator.ask_id();
                    Request req = new Request(Request.RequestType.FIND_UNIVERSITY_BY_ID, idToFind);
                    oos.writeObject(req);
                    oos.flush();

                    Response res = (Response) ois.readObject();
                    if (res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        current_university = (University) res.getPayload();
                        current_level = MenuLevel.UNI;
                        System.out.println(res.getMsg());
                    } else {
                        System.out.println(res.getMsg());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    Request req = new Request(Request.RequestType.GET_ALL_UNIVERSITIES, null);
                    oos.writeObject(req);
                    oos.flush();

                    Response res = (Response) ois.readObject();
                    if (res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        java.util.List<University> list = (java.util.List<University>) res.getPayload();
                        System.out.println("All universities found");
                        list.forEach(System.out::println);
                    } else {
                        System.out.println(res.getMsg());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                if ((current_user.hasPermission(Permissions.MANAGE_USERS))) {
                    current_level = MenuLevel.ADMIN_PANEL;
                } else {
                    System.out.println("You do not have permissions to do this");
                }
                break;
            case 7:
                try {
                    Request logOutRequest = new Request(Request.RequestType.LOGOUT, null);
                    oos.writeObject(logOutRequest);
                    oos.flush();

                    Response response = (Response) ois.readObject();
                    System.out.println(response.getMsg());

                    this.current_level = null;
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;
        }
    }

    private void handle_UNI(University u) {
        System.out.print("⏩ Enter your choice: ");
            int choice = readInt();
            switch (choice){
                case 1 -> {
                    current_level = MenuLevel.MON;
                    current_university = null;
                    break;
                }
                case 2 -> {
                    if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                        try {
                            int facultyId =  IdVerificator.ask_id();

                            if (isIdAlreadyTaken(facultyId, Request.RequestType.FIND_FACULTY_BY_ID)) {
                                System.out.println("This id is already taken. Please try choose another id");
                                break;
                            }

                            String facultyFullName = FacilityNameVerificator.ask_facility_name();
                            String facultyShortName = FacilityNameVerificator.ask_short_name();
                            String facultyEmail = EmailVerificator.ask_email();

                            Faculty facultyToAdd = new Faculty(facultyId, facultyFullName, facultyShortName, null, facultyEmail, u);

                            Request addRequest = new Request(Request.RequestType.ADD_FACULTY, facultyToAdd);
                            oos.writeObject(addRequest);
                            oos.flush();

                            Response response = (Response) ois.readObject();
                            System.out.println(response.getMsg());

                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Access Denied: You cannot add faculty.");
                    }
                }
                case 3 -> {
                    if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                        try {
                            int facultyId =  IdVerificator.ask_id();

                            Request removeRequest = new Request(Request.RequestType.REMOVE_FACULTY, facultyId);
                            oos.writeObject(removeRequest);
                            oos.flush();

                            Response response = (Response) ois.readObject();
                            System.out.println(response.getMsg());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else  {
                        System.out.println("Access Denied: You cannot remove faculty.");
                    }
                }
                case 4 -> {
                    try {
                        int facultyId =  IdVerificator.ask_id();

                        Request findRequest = new Request(Request.RequestType.FIND_FACULTY_BY_ID, facultyId);
                        oos.writeObject(findRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();

                        if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                            current_faculty = (Faculty) response.getPayload();
                            current_level = MenuLevel.FAC;
                            System.out.println(response.getMsg());
                        } else  {
                            System.out.println(response.getMsg());
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 5 -> {
                    try {
                        Request showAllFacultiesRequest = new Request(Request.RequestType.GET_ALL_FACULTIES, u.getId());
                        oos.writeObject(showAllFacultiesRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();

                        if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                            System.out.println("All faculties found");
                            List<Faculty> list = (List<Faculty>) response.getPayload();
                            list.forEach(System.out::println);
                        } else  {
                            System.out.println(response.getMsg());
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    private void handle_GRPS(Faculty f) {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice){
            case 1 -> {
                current_level = MenuLevel.FAC;
                break;
            }
            case 2 -> {
                if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                    try {
                        int groupId =  IdVerificator.ask_id();

                        if (isIdAlreadyTaken(groupId, Request.RequestType.FIND_GROUP_BY_ID)) {
                            System.out.println("This id is already taken. Please try choose another id");
                            break;
                        }

                        String groupName = FacilityNameVerificator.ask_facility_name();
                        Faculty faculty = f;
                        int admissionYear = AcademicInfoVerificator.ask_admission_year();
                        int course = AcademicInfoVerificator.ask_course();

                        Group group = new Group(groupId, groupName, faculty, course, admissionYear);

                        Request addRequest = new Request(Request.RequestType.ADD_GROUP, group);
                        oos.writeObject(addRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();
                        System.out.println(response.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else  {
                    System.out.println("Access Denied: You cannot add group.");
                }
            }
            case 3 -> {
                if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                    try {
                        int groupId =  IdVerificator.ask_id();

                        Request removeRequest = new Request(Request.RequestType.REMOVE_GROUP, groupId);
                        oos.writeObject(removeRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();
                        System.out.println(response.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else   {
                    System.out.println("Access Denied: You cannot remove group.");
                }
            }
            case 4 -> {
                try {
                    int groupId =  IdVerificator.ask_id();

                    Request findRequest = new Request(Request.RequestType.FIND_GROUP_BY_ID, groupId);
                    oos.writeObject(findRequest);
                    oos.flush();

                    Response response = (Response) ois.readObject();

                    if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        System.out.println("Group found");
                        current_group = (Group) response.getPayload();
                        current_level = MenuLevel.GROUP;
                    } else   {
                        System.out.println(response.getMsg());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case 5 -> {
                try {
                    Request showAllGroupsRequest = new Request(Request.RequestType.GET_ALL_GROUPS, f.getId());
                    oos.writeObject(showAllGroupsRequest);
                    oos.flush();

                    Response response = (Response) ois.readObject();

                    if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        System.out.println("All groups found");
                        List<Group> list = (List<Group>) response.getPayload();
                        list.forEach(System.out::println);
                    } else {
                        System.out.println(response.getMsg());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handle_GROUP(Group g) {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice){
            case 1 -> {
                current_level = MenuLevel.GRPS;
                current_group = null;
                break;
            }
            case 2 -> {
                if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                    try {
                        int studentId =  IdVerificator.ask_id();

                        if (isIdAlreadyTaken(studentId, Request.RequestType.FIND_STUDENT_BY_ID)) {
                            System.out.println("This id is already taken. Please try choose another id");
                            break;
                        }

                        boolean alphabet = PersonInfoVerificator.ask_alphabet();
                        String firstName = PersonInfoVerificator.ask_name("first name", alphabet);
                        String lastName = PersonInfoVerificator.ask_name("last name", alphabet);
                        String middleName = PersonInfoVerificator.ask_name("middle name", alphabet);
                        LocalDate dob = PersonInfoVerificator.ask_dob();
                        String email = EmailVerificator.ask_email();
                        String phoneNumber = PhoneNumberVerificator.ask_phonenum();
                        StudyForm studyForm = AcademicInfoVerificator.ask_study_form();
                        StudentStatus status = AcademicInfoVerificator.ask_student_status();

                        Student student = new Student(studentId, firstName, lastName, middleName,dob,
                                email, phoneNumber, null
                                ,g.getCourse(), g,g.getAdmissionYear(), studyForm, status);

                        Request addRequest = new Request(Request.RequestType.ADD_STUDENT, student);
                        oos.writeObject(addRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();
                        System.out.println(response.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else   {
                    System.out.println("Access Denied: You cannot add student.");
                }
            }
            case 3 -> {
                if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                    try {
                        int studentId =  IdVerificator.ask_id();

                        Request removeRequest = new Request(Request.RequestType.REMOVE_STUDENT, studentId);
                        oos.writeObject(removeRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();
                        System.out.println(response.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else   {
                    System.out.println("Access Denied: You cannot remove student.");
                }
            }
            case 4 -> {
               try {
                   int studentId =  IdVerificator.ask_id();

                   Request findRequest = new Request(Request.RequestType.FIND_STUDENT_BY_ID, studentId);
                   oos.writeObject(findRequest);
                   oos.flush();

                   Response response = (Response) ois.readObject();

                   if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                       System.out.println("Student found");
                       Student student = (Student) response.getPayload();
                       System.out.println(student);
                   } else  {
                       System.out.println(response.getMsg());
                   }
               } catch (IOException | ClassNotFoundException e) {
                   e.printStackTrace();
               }
            }
            case 5 -> {
                try {
                    Request showAllStudentsRequest = new Request(Request.RequestType.GET_ALL_STUDENTS, g.getId());
                    oos.writeObject(showAllStudentsRequest);
                    oos.flush();

                    Response response = (Response) ois.readObject();

                    if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        System.out.println("All students found");
                        List<Student> list = (List<Student>) response.getPayload();
                        list.forEach(System.out::println);
                    } else  {
                        System.out.println(response.getMsg());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private   void handle_FAC(Faculty f) {
        System.out.print("⏩ Enter your choice: ");
//        show all faculties
        int choice = readInt();
            switch (choice){
                case 1 -> {
                    current_level = MenuLevel.UNI;
                    current_faculty = null;
                    break;
                }
                case 2 -> {
                    current_level = MenuLevel.DEPS;
                    break;
                }
                case 3 -> {
                    current_level = MenuLevel.GRPS;
                    break;
                }
                case 4 -> {
                    System.out.println("This method is currently not implemented yet.");
                    break;
                }
            }

    }
    private void handle_DEPS(Faculty f) {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
            switch (choice){
                case 1 -> {
                    current_level = MenuLevel.FAC;
                    break;
                }
                case 2 -> {
                    if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                        try {
                            int departmentId = IdVerificator.ask_id();

                            if (isIdAlreadyTaken(departmentId, Request.RequestType.FIND_DEPARTMENT_BY_ID)) {
                                System.out.println("This id is already taken. Please try choose another id");
                                break;
                            }

                            String departmentName = FacilityNameVerificator.ask_facility_name();
                            String email = EmailVerificator.ask_email();
                            String location = UniversityVerificator.ask_address();

                            Department dep = new Department(departmentId, departmentName, f,
                                    null, location, email);

                            Request addRequest = new Request(Request.RequestType.ADD_DEPARTMENT, dep);
                            oos.writeObject(addRequest);
                            oos.flush();

                            Response response = (Response) ois.readObject();
                            System.out.println(response.getMsg());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else   {
                        System.out.println("Access Denied: You cannot add department.");
                    }
                }
                case 3 -> {
                    if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                        try {
                            int departmentId = IdVerificator.ask_id();

                            Request removeRequest = new Request(Request.RequestType.REMOVE_DEPARTMENT, departmentId);
                            oos.writeObject(removeRequest);
                            oos.flush();

                            Response response = (Response) ois.readObject();
                            System.out.println(response.getMsg());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else   {
                        System.out.println("Access Denied: You cannot remove department.");
                    }
                }
                case 4 -> {
                    try {
                        int departmentId = IdVerificator.ask_id();

                        Request findRequest = new Request(Request.RequestType.FIND_DEPARTMENT_BY_ID, departmentId);
                        oos.writeObject(findRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();

                        if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                            System.out.println("Department found");
                            Department department = (Department) response.getPayload();
                            System.out.println(department);
                            current_department = department;
                            current_level = MenuLevel.DEPARTAMENT;
                        } else {
                            System.out.println(response.getMsg());
                        }
                    }  catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 5 -> {
                    try {
                        Request showAllDepartmentsRequest = new Request(Request.RequestType.GET_ALL_DEPARTMENTS, f.getId());
                        oos.writeObject(showAllDepartmentsRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();

                        if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                            System.out.println("All departments found");
                            List<Department> list = (List<Department>) response.getPayload();
                            list.forEach(System.out::println);
                        } else   {
                            System.out.println(response.getMsg());
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    private   void handle_DEPARTAMENT(Department d) {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
            switch (choice){
                case  1 -> {
                    current_level = MenuLevel.DEPS;
                    current_department = null;
                    break;
                }
                case 2 -> {
                    if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                        try {
                            int teacherId = IdVerificator.ask_id();

                            if (isIdAlreadyTaken(teacherId, Request.RequestType.FIND_TEACHER_BY_ID)) {
                                System.out.println("This id is already taken. Please try choose another id");
                                break;
                            }

                            boolean alphabet = PersonInfoVerificator.ask_alphabet();
                            String firstName = PersonInfoVerificator.ask_name("first name", alphabet);
                            String lastName = PersonInfoVerificator.ask_name("last name", alphabet);
                            String middleName = PersonInfoVerificator.ask_name("middle name", alphabet);
                            LocalDate dob = PersonInfoVerificator.ask_dob();
                            String email = EmailVerificator.ask_email();
                            String phoneNumber = PhoneNumberVerificator.ask_phonenum();
                            TeacherPosition position = AcademicInfoVerificator.ask_teacher_position();
                            TeacherDegree degree = AcademicInfoVerificator.ask_teacher_degree();
                            TeacherRank rank = AcademicInfoVerificator.ask_teacher_rank();
                            LocalDate hiringDate = AcademicInfoVerificator.ask_hiring_date(dob);
                            double load = AcademicInfoVerificator.ask_load();

                            Teacher teacher = new Teacher(teacherId, firstName, lastName, middleName, dob,
                                    email, phoneNumber, position, degree, rank, hiringDate, load, d);

                            Request addRequest = new Request(Request.RequestType.ADD_TEACHER, teacher);
                            oos.writeObject(addRequest);
                            oos.flush();

                            Response response = (Response) ois.readObject();
                            System.out.println(response.getMsg());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else     {
                        System.out.println("Access Denied: You cannot add teacher.");
                    }
                }
                case 3 -> {
                    if (current_user.hasPermission(Permissions.MANAGE_STRUCTURE)) {
                        try {
                            int teacherId = IdVerificator.ask_id();

                            Request removeRequest = new Request(Request.RequestType.REMOVE_TEACHER, teacherId);
                            oos.writeObject(removeRequest);
                            oos.flush();

                            Response response = (Response) ois.readObject();
                            System.out.println(response.getMsg());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Access Denied: You cannot remove teacher.");
                    }
                }
                case 4 -> {
                    try {
                        int teacherId = IdVerificator.ask_id();

                        Request findRequest = new Request(Request.RequestType.FIND_TEACHER_BY_ID, teacherId);
                        oos.writeObject(findRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();

                        if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                            System.out.println("Teacher found");
                            Teacher teacher = (Teacher) response.getPayload();
                            System.out.println(teacher);
                        } else {
                            System.out.println(response.getMsg());
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 5 -> {
                    try {
                        Request showAllTeachersRequest = new Request(Request.RequestType.GET_ALL_TEACHERS, d.getId());
                        oos.writeObject(showAllTeachersRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();

                        if  (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                            System.out.println("All teachers found");
                            List<Teacher> list = (List<Teacher>) response.getPayload();
                            list.forEach(System.out::println);
                        } else {
                            System.out.println(response.getMsg());
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    private void handle_ADMIN_PANEL() {
        System.out.print("⏩ Enter your choice: ");
        int choice = readInt();
        switch (choice){
            case  1 -> {
                current_level = MenuLevel.MON;
                break;
            }
            case 2 -> {
                if (current_user.hasPermission(Permissions.MANAGE_USERS)) {
                    try {
                        int userId = IdVerificator.ask_id();

                        if (isIdAlreadyTaken(userId, Request.RequestType.FIND_USER_BY_ID)) {
                            System.out.println("This id is already taken. Please try choose another id");
                            break;
                        }

                        String login = SystemUserVerificator.askLogin();
                        String password = SystemUserVerificator.askPassword();
                        SystemUserRoles role = SystemUserVerificator.askRole();

                        SystemUser user = new SystemUser(userId, login, password, role);

                        Request addRequest = new Request(Request.RequestType.ADD_USER, user);
                        oos.writeObject(addRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();
                        System.out.println(response.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else  {
                    System.out.println("Access Denied: You cannot add users.");
                }
            }
            case 3 -> {
                if  (current_user.hasPermission(Permissions.MANAGE_USERS)) {
                    try {
                        int userId = IdVerificator.ask_id();

                        if (current_user.getId() == userId) {
                            System.out.println("You cannot delete yourself!");
                            break;
                        }

                        Request removeRequest = new Request(Request.RequestType.REMOVE_USER, userId);
                        oos.writeObject(removeRequest);
                        oos.flush();

                        Response response = (Response) ois.readObject();
                        System.out.println(response.getMsg());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else   {
                    System.out.println("Access Denied: You cannot remove users.");
                }
            }
            case 4 -> {
                try {
                    int userId = IdVerificator.ask_id();

                    Request findRequest = new Request(Request.RequestType.FIND_USER_BY_ID, userId);
                    oos.writeObject(findRequest);
                    oos.flush();

                    Response response = (Response) ois.readObject();

                    if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        System.out.println("User found");
                        SystemUser user = (SystemUser) response.getPayload();
                        System.out.println(user);
                    }  else {
                        System.out.println(response.getMsg());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case 5 -> {
                try {
                    Request showAllUsersRequest = new Request(Request.RequestType.GET_ALL_USERS);
                    oos.writeObject(showAllUsersRequest);
                    oos.flush();

                    Response response = (Response) ois.readObject();

                    if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        System.out.println("All users found");
                        List<SystemUser> list = (List<SystemUser>) response.getPayload();
                        list.forEach(System.out::println);
                    } else  {
                        System.out.println(response.getMsg());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String handleUiDrawing() {
        StringBuilder path = new StringBuilder("MON");
        MenuLevel level = current_level;
        switch (level) {
            case MON -> {
               break;
            }
            case UNI -> {
                path.append(" > ").append(current_university.getShortName());
            }
            case FAC ->  {
                path.append(" > ").append(current_university.getShortName())
                        .append(" > ").append(current_faculty.getShortName());
            }
            case DEPS -> {
                path.append(" > ").append(current_university.getShortName())
                        .append(" > ").append(current_faculty.getShortName())
                        .append(" > [DEPS]");
            }

            case DEPARTAMENT -> {
                path.append(" > ").append(current_university.getShortName())
                        .append(" > ").append(current_faculty.getShortName())
                        .append(" > ").append(current_department.getName());
            }
            case GRPS -> {
                path.append(" > ").append(current_university.getShortName())
                        .append(" > ").append(current_faculty.getShortName())
                        .append(" > [GRPS]");
            }
            case GROUP -> {
                path.append(" > ").append(current_university.getShortName())
                        .append(" > ").append(current_faculty.getShortName())
                        .append(" > ").append(current_department.getName())
                        .append(" > ").append(current_group.getName());
            }
            case ADMIN_PANEL -> {
                path.append(" > [Admin Panel]");
            }
        }
        return path.toString();
    }

    public String handleInfoAboutEntityDrawing() {
        if (current_level == MenuLevel.DEPS
                || current_level == MenuLevel.GRPS || current_level == MenuLevel.ADMIN_PANEL) {
            return "";
        }

        String content = "";

        switch (current_level) {
            case MON -> content = DashboardBuilder.buildMONPanel();
            case UNI -> content = DashboardBuilder.buildUniversityPanel(current_university);
            case FAC -> content = DashboardBuilder.buildFacultyPanel(current_faculty);
            case DEPARTAMENT -> content = DashboardBuilder.buildDepartmentPanel(current_department);
            case GROUP -> {
                int studentsCount = fetchStudentsCountFromServer(current_group.getId());
                content = DashboardBuilder.buildGroupPanel(current_group, studentsCount);
            }
        }

        return content;
    }

    private int fetchStudentsCountFromServer(int groupId) {
        try {
            Request req = new Request(Request.RequestType.GET_STUDENTS_COUNT, groupId);
            oos.writeObject(req);
            oos.flush();

            Response res = (Response) ois.readObject();
            if (res.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                return ((Number) res.getPayload()).intValue();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error fetching students count: " + e.getMessage());
        }
        return 0;
    }

    private static int readInt(){
        Scanner scanner = InitScanner.try_init_scanner();
        for(;;) {
            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer.");
                scanner.next();
                continue;
            }
            return choice;
        }
    }
    public MenuLevel handle(MenuLevel lvl){
        switch (lvl){
            case MON:
                handle_MON();
                break;
            case UNI:
                handle_UNI(current_university);
                break;
            case FAC:
                handle_FAC(current_faculty);
                break;
            case DEPS:
                handle_DEPS(current_faculty);
                break;
            case DEPARTAMENT:
                handle_DEPARTAMENT(current_department);
                break;
                case GRPS:
                    handle_GRPS(current_faculty);
                    break;
                    case GROUP:
                        handle_GROUP(current_group);
                        break;
            case ADMIN_PANEL:
                handle_ADMIN_PANEL(); break;
                default: break;
        }
        return current_level;
    }
}