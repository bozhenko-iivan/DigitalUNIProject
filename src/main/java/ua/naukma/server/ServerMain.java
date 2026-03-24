package ua.naukma.server;

import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.controller.*;
import ua.naukma.server.repository.*;
import ua.naukma.server.service.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {
    public static void main(String[] args) {
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Server started on port 8080");

            Repository<University, Integer> uniRepo = new FileUniversityRepository();
            Repository<SystemUser, Integer> userRepository = new FileUserRepository();
            Repository<Faculty, Integer> facultyRepository = new FileFacultyRepository();
            Repository<Group, Integer> groupRepository = new FileGroupRepository();
            Repository<Department, Integer> departmentRepository = new FileDepartmentRepository();
            PersonRepository<Student, Integer> studentRepository = new FileStudentRepository();
            PersonRepository<Teacher, Integer> teacherRepository = new FileTeacherRepository();

            UniversityService uniService = new UniversityService(uniRepo);
            UserService userService = new UserService(userRepository);
            FacultyService facultyService = new FacultyService(facultyRepository);
            GroupService groupService = new GroupService(groupRepository);
            StudentService studentService = new StudentService(studentRepository);
            TeacherService teacherService = new TeacherService(teacherRepository);
            DepartmentService departmentService = new DepartmentService(departmentRepository);

            Map<Request.RequestType, RequestHandler> router = new HashMap<>();

            UserController userController = new UserController(userService);
            router.put(Request.RequestType.LOGIN, userController);
            router.put(Request.RequestType.ADD_USER, userController);
            router.put(Request.RequestType.REMOVE_USER, userController);
            router.put(Request.RequestType.FIND_USER_BY_ID, userController);
            router.put(Request.RequestType.LOGOUT, userController);
            router.put(Request.RequestType.GET_ALL_USERS, userController);

            UniversityController uniController = new UniversityController(uniService);
            router.put(Request.RequestType.ADD_UNIVERSITY, uniController);
            router.put(Request.RequestType.REMOVE_UNIVERSITY, uniController);
            router.put(Request.RequestType.FIND_UNIVERSITY_BY_ID, uniController);
            router.put(Request.RequestType.GET_ALL_UNIVERSITIES, uniController);

            FacultyController facController = new FacultyController(facultyService);
            router.put(Request.RequestType.ADD_FACULTY, facController);
            router.put(Request.RequestType.REMOVE_FACULTY, facController);
            router.put(Request.RequestType.FIND_FACULTY_BY_ID, facController);
            router.put(Request.RequestType.GET_ALL_FACULTIES, facController);

            GroupController groupController = new GroupController(groupService);
            router.put(Request.RequestType.ADD_GROUP, groupController);
            router.put(Request.RequestType.REMOVE_GROUP, groupController);
            router.put(Request.RequestType.FIND_GROUP_BY_ID, groupController);
            router.put(Request.RequestType.GET_ALL_GROUPS, groupController);

            DepartmentController departmentController = new DepartmentController(departmentService);
            router.put(Request.RequestType.ADD_DEPARTMENT, departmentController);
            router.put(Request.RequestType.REMOVE_DEPARTMENT, departmentController);
            router.put(Request.RequestType.FIND_DEPARTMENT_BY_ID, departmentController);
            router.put(Request.RequestType.GET_ALL_DEPARTMENTS, departmentController);

            StudentController studentController = new StudentController(studentService);
            router.put(Request.RequestType.ADD_STUDENT, studentController);
            router.put(Request.RequestType.REMOVE_STUDENT, studentController);
            router.put(Request.RequestType.FIND_STUDENT_BY_ID, studentController);
            router.put(Request.RequestType.GET_ALL_STUDENTS, studentController);

            TeacherController teacherController = new TeacherController(teacherService);
            router.put(Request.RequestType.ADD_TEACHER, teacherController);
            router.put(Request.RequestType.REMOVE_TEACHER, teacherController);
            router.put(Request.RequestType.FIND_TEACHER_BY_ID, teacherController);
            router.put(Request.RequestType.GET_ALL_TEACHERS, teacherController);


            userService.initUser();

            while (true) {
                try {
                    socket = serverSocket.accept();
                    String clientIp = socket.getInetAddress().getHostAddress();
                    System.out.println("Accepted connection from " + clientIp + " on port 8080");

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    while (true) {
                        Request request = (Request) ois.readObject();

                        RequestHandler handler = router.get(request.getType());

                        System.out.println("========================================");
                        System.out.println("New request from: " + clientIp);
                        System.out.println("Request Type:   " + request.getType());
                        System.out.println("Data attached:  " + request.getData());
                        System.out.println("========================================");

                        if (handler != null) {
                            Response response = handler.process(request);

                            System.out.println("Response Status: " + response.getResponseStatus());
                            System.out.println("Message:         " + response.getMsg());
                            System.out.println("Data sent back:  " + response.getPayload());
                            System.out.println("========================================\n");
                            oos.writeObject(response);
                            oos.flush();
                        }
                    }
                } catch (EOFException e) {
                    System.out.println("Client disconnected: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println("ClassNotFound exception: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start server on port 8080: " + e.getMessage());
        }
    }
}