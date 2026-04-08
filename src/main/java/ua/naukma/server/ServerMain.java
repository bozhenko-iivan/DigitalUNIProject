package ua.naukma.server;

import org.slf4j.LoggerFactory;
import ua.naukma.client.ui.MenuLevel;
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
import java.util.EnumMap;
import java.util.Map;

public class ServerMain {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ServerMain.class);

//    public static void registerController(Map<MenuLevel, RequestHandler> router, RequestHandler handler) {
//        if (handler.getClass().isAnnotationPresent(CommandRoute.class)) {
//            CommandRoute annotation = handler.getClass().getAnnotation(CommandRoute.class);
//            Request.RequestType[] requestTypes = annotation.value();
//            for (MenuLevel lvl : MenuLevel.values()) {
//                router.put(requestType, handler);
//            }
//        } else {
//            throw new IllegalArgumentException(handler.getClass().getName() + " is not annotated with @CommandRoute");
//        }
//    }

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            log.info("Starting server on port 8080");

            Repository<University, Integer> uniRepo = new FileRepository<>(University.class);
            Repository<SystemUser, Integer> userRepository = new FileRepository<>(SystemUser.class);
            Repository<Faculty, Integer> facultyRepository = new FileRepository<>(Faculty.class);
            Repository<Group, Integer> groupRepository = new FileRepository<>(Group.class);
            Repository<Department, Integer> departmentRepository = new FileRepository<>(Department.class);
            Repository<Grade, Integer> gradeRepository = new FileRepository<>(Grade.class);
            PersonRepository<Student, Integer> studentRepository = new FilePersonRepository<>(Student.class);
            PersonRepository<Teacher, Integer> teacherRepository = new FilePersonRepository<>(Teacher.class);

            UniversityService uniService = new UniversityService(uniRepo, University.class);
            UserService userService = new UserService(userRepository, SystemUser.class);
            FacultyService facultyService = new FacultyService(facultyRepository, Faculty.class);
            GroupService groupService = new GroupService(groupRepository, Group.class);
            StudentService studentService = new StudentService(studentRepository, Student.class);
            TeacherService teacherService = new TeacherService(teacherRepository, Teacher.class);
            DepartmentService departmentService = new DepartmentService(departmentRepository, Department.class);
            GradeService gradeService = new GradeService(gradeRepository, Grade.class);

            //Map<Request.RequestType, RequestHandler> router = new EnumMap<>(Request.RequestType.class);
            Map<MenuLevel, RequestHandler> router = new EnumMap<>(MenuLevel.class);
            router.put(MenuLevel.ADMIN_PANEL, new UserController(userService));
            router.put(MenuLevel.MON, new EntityController<>(uniService));
            router.put(MenuLevel.UNI, new EntityController<>(facultyService));
            //router.put(MenuLevel.FAC, new EntityController<>(departmentService));
            //router.put(MenuLevel.FAC, new EntityController<>(groupService));
            router.put(MenuLevel.GRPS, new EntityController<>(groupService));
            router.put(MenuLevel.GROUP, new StudentController(studentService, gradeService));
            router.put(MenuLevel.DEPS, new EntityController<>(departmentService));
            router.put(MenuLevel.DEPARTAMENT, new EntityController<>(teacherService));
            router.put(MenuLevel.STUDENT, new StudentController(studentService, gradeService));
           // registerController(router, new UserController(userService));
            //registerController(router, new EntityController<>(uniService));
            //registerController(router, new EntityController<>(facultyService));
            //registerController(router, new EntityController<>(departmentService));
            //registerController(router, new EntityController<>(groupService));
            //registerController(router, new EntityController<>(teacherService));
            //registerController(router, new StudentController(studentService, gradeService));

            userService.initUser();

            while (!serverSocket.isClosed()) {
                handleClientConnection(serverSocket, router);
            }
        } catch (IOException e) {
            log.error("Couldn't start server on port 8080 ", e);
        }
    }

    private static void handleClientConnection(ServerSocket serverSocket, Map<MenuLevel, RequestHandler> router) {
        try(Socket socket = serverSocket.accept()) {
            String clientIp = socket.getInetAddress().getHostAddress();
            log.info("Client IP: {}", clientIp);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (!serverSocket.isClosed()) {
                Request request = (Request) ois.readObject();
                Request.RequestType currentType = request.getType();
                //RequestHandler handler = router.get(currentType);
                MenuLevel l = request.getLevel();
                RequestHandler handler = router.get(l);

                log.info("==========================================");
                log.info("New request received from: {} ", clientIp);
                log.info("Request type: {} ", request.getType());
                log.info("Data attached:  {}", request.getData());
                log.info("==========================================");

                if (handler != null) {
                    Response response = handler.process(request);
                    log.info("Response status:     {}", response.getResponseStatus());
                    log.info("Msg:                 {}", response.getMsg());
                    log.info("Data sent back:      {}", response.getPayload());
                    log.info("==========================================");
                    oos.writeObject(response);
                    oos.flush();
                } else {
                    log.warn("No handler found: ", l);
                    Response errorResponse = new Response(Response.ResponseStatus.FAILURE, "Server Error: Unknown command " + currentType);
                    oos.writeObject(errorResponse);
                    oos.flush();
                }
            }
        } catch (EOFException e) {
            log.info("Client disconnected (EOF): {}", e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFound exception: ", e);
        } catch (Exception e) {
            log.error("Exception:", e);
        }
    }
}