package ua.naukma.server;

import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.annotation.CommandRoute;
import ua.naukma.server.controller.*;
import ua.naukma.server.repository.*;
import ua.naukma.server.service.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {
    public static void registerController(Map<Request.RequestType, RequestHandler> router, RequestHandler handler) {
        if (handler.getClass().isAnnotationPresent(CommandRoute.class)) {
            CommandRoute annotation = handler.getClass().getAnnotation(CommandRoute.class);
            Request.RequestType[] requestTypes = annotation.value();
            for (Request.RequestType requestType : requestTypes) {
                router.put(requestType, handler);
            }
        } else {
            throw new RuntimeException(handler.getClass().getName() + " is not annotated with @CommandRoute");
        }
    }

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

            registerController(router, new UserController(userService));
            registerController(router, new UniversityController(uniService));
            registerController(router, new FacultyController(facultyService));
            registerController(router, new DepartmentController(departmentService));
            registerController(router, new GroupController(groupService));
            registerController(router, new TeacherController(teacherService));
            registerController(router, new StudentController(studentService));

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
                        Request.RequestType currentType = request.getType();
                        RequestHandler handler = router.get(currentType);

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
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("ClassNotFound exception: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start server on port 8080: " + e.getMessage());
        }
    }
}