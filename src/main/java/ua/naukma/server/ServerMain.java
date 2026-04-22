package ua.naukma.server;

import org.slf4j.LoggerFactory;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.controller.*;
import ua.naukma.server.repository.*;
import ua.naukma.server.service.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ServerMain.class);

    static void main() {
        try (final ExecutorService executors = Executors.newFixedThreadPool(10)) {
            List<Socket> sockets = new ArrayList<>();
            for (; ; ) {
                try (ServerSocket serverSocket = new ServerSocket(8080)) {
                    log.info("Starting server on port 8080");
                    Socket socket = serverSocket.accept();
                    sockets.add(socket);
                    executors.submit(() -> {
                        Map<MenuLevel, RequestHandler> router = InitRouter();
                        handleClientConnection(socket, router);
                        sockets.remove(socket);
                    });
                } catch (IOException e) {
                    log.error("Couldn't start server on port 8080 ", e);
                    break;
                } catch (Exception e) {
                    log.error("Exception:", e);
                }
                if (sockets.isEmpty()) break;
            }
            executors.shutdown();
        }
    }

    private static void handleClientConnection(Socket socket, Map<MenuLevel, RequestHandler> router) {
        try {
            String clientIp = socket.getInetAddress().getHostAddress();
            log.info("Client IP: {}", clientIp);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (!socket.isClosed()) {
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
                    log.warn("No handler found: {}", l);
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

    static Map<MenuLevel, RequestHandler> InitRouter() {
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
        FacultyService facultyService = new FacultyService(facultyRepository, teacherRepository, Faculty.class);
        GroupService groupService = new GroupService(groupRepository, studentRepository,Group.class);
        StudentService studentService = new StudentService(studentRepository, gradeRepository, groupRepository,Student.class);
        TeacherService teacherService = new TeacherService(teacherRepository, Teacher.class);
        DepartmentService departmentService = new DepartmentService(departmentRepository, teacherRepository, Department.class);
        GradeService gradeService = new GradeService(gradeRepository, Grade.class);

        Map<MenuLevel, RequestHandler> router = new EnumMap<>(MenuLevel.class);
        router.put(MenuLevel.ADMIN_PANEL, new UserController(userService));
        router.put(MenuLevel.MON, new UniversityController(uniService, studentService, groupService));
        router.put(MenuLevel.UNI, new FacultyController(facultyService, teacherService));
        router.put(MenuLevel.FAC, new FacultyController(facultyService, teacherService));
        router.put(MenuLevel.GRPS, new EntityController<>(groupService));
        router.put(MenuLevel.GROUP, new StudentController(studentService, gradeService, groupService));
        router.put(MenuLevel.DEPS, new EntityController<>(departmentService));
        router.put(MenuLevel.DEPARTAMENT, new DepartmentController(departmentService, studentService,teacherService));
        router.put(MenuLevel.STUDENT, new StudentController(studentService, gradeService, groupService));
        userService.initUser();
        return router;
    }
}