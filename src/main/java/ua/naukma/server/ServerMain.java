package ua.naukma.server;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.exception.IncorrectDataException;
import ua.naukma.network.Request;
import ua.naukma.network.Response;
import ua.naukma.server.repository.*;
import ua.naukma.server.service.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

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
            Repository<Faculty, Integer> facultyRepository = new InMemoryFacultyRepository();
            Repository<Group, Integer> groupRepository = new InMemoryGroupRepository();
            Repository<Department, Integer> departmentRepository = new InMemoryDepartmentRepository();
            PersonRepository<Student, Integer> studentRepository = new InMemoryStudentRepository();
            PersonRepository<Teacher, Integer> teacherRepository = new InMemoryTeacherRepository();

            UniversityService uniService = new UniversityService(uniRepo);
            UserService userService = new UserService(userRepository);
            FacultyService facultyService = new FacultyService(facultyRepository);
            GroupService groupService = new GroupService(groupRepository);
            StudentService studentService = new StudentService(studentRepository);
            TeacherService teacherService = new TeacherService(teacherRepository);
            DepartmentService departmentService = new DepartmentService(departmentRepository);

            userService.initUser();

            while (true) {
                try {
                    socket = serverSocket.accept();
                    System.out.println("Accepted connection on port 8080");

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    while (true) {
                        Request request = (Request) ois.readObject();
                        request.getType();

                        switch (request.getType()) {
                            case LOGIN -> {
                                SystemUser credentials = (SystemUser) request.getData();
                                Response response;
                                System.out.println("Received login request");
                                try {
                                    SystemUser realUser = userService.authenticate(credentials);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            realUser,
                                            "Login successful");
                                } catch (EntityNotFoundException | IncorrectDataException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Login failed: " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case LOGOUT -> {
                                System.out.println("Received logout request");
                                Response response;
                                try {
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Logout successful");
                                } catch (EntityNotFoundException | IncorrectDataException e) {
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Logout failed: " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case ADD_USER -> {
                                SystemUser userToAdd = (SystemUser) request.getData();
                                Response response;
                                System.out.println("Received add user request");
                                try {
                                    userService.addUser(userToAdd);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Add user successful");
                                } catch (DuplicateEntityException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "User already exists"
                                    );
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case REMOVE_USER -> {
                                int userIdToRemove = (Integer) request.getData();
                                System.out.println("Received remove user request");
                                Response response;
                                try {
                                    userService.deleteById(userIdToRemove);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "User with id " + userIdToRemove + " was successfully deleted");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Remove user failed " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case ADD_GROUP -> {
                                Group groupToAdd = (Group) request.getData();
                                System.out.println("Received add group request");
                                Response response;
                                try {
                                    groupService.addGroup(groupToAdd);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Add group successful");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Added group failed " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case REMOVE_GROUP -> {
                                int groupIdToRemove = (Integer) request.getData();
                                System.out.println("Received remove group request");
                                Response response;
                                try {
                                    groupService.deleteById(groupIdToRemove);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Group with id " + groupIdToRemove + " was successfully deleted");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Error removing group " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case ADD_FACULTY -> {
                                Faculty facultyToAdd = (Faculty) request.getData();
                                System.out.println("Received add faculty request");
                                Response response;
                                try {
                                    facultyService.addFaculty(facultyToAdd);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Faculty with id " + facultyToAdd.getId() + " was successfully added");
                                } catch (DuplicateEntityException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Error adding faculty " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case REMOVE_FACULTY -> {
                                int idFacultyToRemove = (Integer) request.getData();
                                System.out.println("Received remove faculty request");
                                Response response;
                                try {
                                    facultyService.deleteById(idFacultyToRemove);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Faculty with id " + idFacultyToRemove + " was successfully deleted");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Remove faculty failed " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case ADD_STUDENT -> {
                                Student studentToAdd = (Student) request.getData();
                                System.out.println("Received add student request");
                                Response response;
                                try {
                                    studentService.addStudent(studentToAdd);
                                    response = new Response(Response.ResponseStatus.SUCCESS,
                                            "Student with id " + studentToAdd.getId() + " has been successfully added");
                                } catch (DuplicateEntityException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE, "User already exists with id " + studentToAdd.getId()
                                    );
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case REMOVE_STUDENT -> {
                                int idStudentToRemove = (Integer) request.getData();
                                System.out.println("Received remove student request");
                                Response response;
                                try {
                                    studentService.deleteById(idStudentToRemove);
                                    response = new Response(Response.ResponseStatus.SUCCESS,
                                            "Student with id " + idStudentToRemove + " was successfully removed");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Remove student failed " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case ADD_DEPARTMENT -> {
                                Department departmentToAdd = (Department) request.getData();
                                System.out.println("Received add department request");
                                Response response;
                                try {
                                    departmentService.addDepartment(departmentToAdd);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Department with id " + departmentToAdd.getId() + " has been successfully added");
                                } catch (DuplicateEntityException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Add department failed " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case REMOVE_DEPARTMENT -> {
                                int idToRemove = (Integer) request.getData();
                                System.out.println("Received remove department request");
                                Response response;
                                try {
                                    departmentService.deleteById(idToRemove);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Department with id " + idToRemove + " was successfully deleted");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Error removing department " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case ADD_TEACHER -> {
                                Teacher teacherToAdd = (Teacher) request.getData();
                                System.out.println("Received add teacher request");
                                Response response;
                                try {
                                    teacherService.addTeacher(teacherToAdd);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Teacher with id " + teacherToAdd.getId() + " was successfully created");
                                } catch (DuplicateEntityException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Error adding teacher " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case REMOVE_TEACHER -> {
                                int idTeacher = (Integer) request.getData();
                                System.out.println("Received remove teacher request");
                                Response response;
                                try {
                                    teacherService.deleteById(idTeacher);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "Teacher with id " + idTeacher + " was successfully deleted");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Error removing teacher " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case ADD_UNIVERSITY -> {
                                System.out.println("Received add university request");
                                University uniToAdd = (University) request.getData();
                                Response response;
                                try {
                                    uniService.addUniversity(uniToAdd);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            "University " + uniToAdd.getFullName() + " has been added successfully");
                                } catch (DuplicateEntityException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Error adding university: " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case REMOVE_UNIVERSITY -> {
                                System.out.println("Received remove university request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        "Remove university successful");
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case GET_ALL_UNIVERSITIES -> {
                                List<University> universities = uniService.getAllUniversities();
                                System.out.println("Received all university request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        universities, "University list has been successfully retrieved"
                                );
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case GET_ALL_FACULTIES -> {
                                int uniId = (Integer) request.getData();
                                System.out.println("Receive id request");
                                List<Faculty> faculties = facultyService.findAllByUniId(uniId);
                                System.out.println("Received all faculties request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        faculties, "Faculty list has been successfully retrieved"
                                );
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case GET_ALL_DEPARTMENTS -> {
                                int facultyId = (Integer) request.getData();
                                System.out.println("Receive id request");
                                List<Department> departments = departmentService.findAllByFacultyId(facultyId);
                                System.out.println("Received all departments request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        departments, "Department list has been successfully retrieved"
                                );
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case GET_ALL_GROUPS -> {
                                int facultyId = (Integer) request.getData();
                                System.out.println("Receive id request");
                                List<Group> groups = groupService.findAllByFacultyId(facultyId);
                                System.out.println("Received all groups request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        groups, "Group list has been successfully retrieved"
                                );
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case GET_ALL_STUDENTS -> {
                                int groupId = (Integer) request.getData();
                                System.out.println("Receive id request");
                                List<Student> students = studentService.findAllByGroupId(groupId);
                                System.out.println("Received all departments request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        students, "Student list has been successfully retrieved"
                                );
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case GET_ALL_TEACHERS -> {
                                int departmentId = (Integer) request.getData();
                                System.out.println("Receive id request");
                                List<Teacher> teachers = teacherService.findAllByDepartmentId(departmentId);
                                System.out.println("Receive all departments request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        teachers, "Teacher list has been successfully retrieved"
                                );
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case GET_ALL_USERS -> {
                                List<SystemUser> list = userService.getAllUsers();
                                System.out.println("Receive all users request");
                                Response response = new Response(
                                        Response.ResponseStatus.SUCCESS,
                                        list, "User list has been successfully retrieved"
                                );
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case FIND_USER_BY_ID -> {
                                int userIdToFind = (Integer) request.getData();
                                System.out.println("Receive id request");
                                Response response;
                                try {
                                    SystemUser foundUser = userService.findById(userIdToFind);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            foundUser, "User found successfully"
                                    );
                                } catch (Exception e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Error finding user: " + e.getMessage()
                                    );
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case FIND_UNIVERSITY_BY_ID -> {
                                int uniIdToFind = (Integer) request.getData();
                                System.out.println("Receive id request");
                                Response response;
                                try {
                                    University foundId = uniService.findById(uniIdToFind);
                                    response = new Response(
                                            Response.ResponseStatus.SUCCESS,
                                            foundId,
                                            "University with id " + uniIdToFind + " was successfully found"
                                    );
                                } catch (EntityNotFoundException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Error finding university with id " + uniIdToFind);
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case FIND_FACULTY_BY_ID -> {
                                int idToFind = (Integer) request.getData();
                                System.out.println("Received request to find faculty with ID: " + idToFind);
                                Response response;
                                try {
                                    Faculty foundFaculty = facultyService.findById(idToFind);
                                    response = new Response(Response.ResponseStatus.SUCCESS,
                                            foundFaculty,
                                            "Faculty with ID " + idToFind + " was successfully found");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Not found: " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case FIND_DEPARTMENT_BY_ID -> {
                                int idToFind = (Integer) request.getData();
                                System.out.println("Received request to find department with ID: " + idToFind);
                                Response response;
                                try {
                                    Department foundDepartment = departmentService.findById(idToFind);
                                    response = new Response(Response.ResponseStatus.SUCCESS,
                                            foundDepartment,
                                            "Department with ID " + idToFind + " was successfully found");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Not found: " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case FIND_GROUP_BY_ID -> {
                                int idToFind = (Integer) request.getData();
                                System.out.println("Received request to find group with ID: " + idToFind);
                                Response response;
                                try {
                                    Group foundGroup = groupService.findById(idToFind);
                                    response = new Response(Response.ResponseStatus.SUCCESS,
                                            foundGroup,
                                            "Group with ID " + idToFind + " was successfully found");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Not found: " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case FIND_STUDENT_BY_ID -> {
                                int idToFind = (Integer) request.getData();
                                System.out.println("Received request to find student with ID: " + idToFind);
                                Response response;
                                try {
                                    Student foundStudent = studentService.findById(idToFind);
                                    response = new Response(Response.ResponseStatus.SUCCESS,
                                            foundStudent,
                                            "Student with ID " + idToFind + " was successfully found");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(
                                            Response.ResponseStatus.FAILURE,
                                            "Not found: " + e.getMessage()
                                    );
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case FIND_TEACHER_BY_ID -> {
                                int idToFind = (Integer) request.getData();
                                System.out.println("Received request to find teacher with ID: " + idToFind);
                                Response response;
                                try {
                                    Teacher foundTeacher = teacherService.findById(idToFind);
                                    response = new Response(Response.ResponseStatus.SUCCESS,
                                            foundTeacher,
                                            "Teacher with ID " + idToFind + " was successfully found");
                                } catch (EntityNotFoundException e) {
                                    response = new Response(Response.ResponseStatus.FAILURE,
                                            "Not found: " + e.getMessage());
                                }
                                oos.writeObject(response);
                                oos.flush();
                            }
                            case null, default -> {
                                System.out.println("Received default request");
                            }
                        }
                    }
                } catch (EOFException e) {
                    System.out.println("Received EOF exception " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println("Received ClassNotFound exception " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start server on port 8080: " + e.getMessage());
        }
    }
}