package ua.naukma.server.service;

import ua.naukma.domain.Student;
import ua.naukma.domain.StudentStatus;
import ua.naukma.domain.StudyForm;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class StudentService extends EntityService<Student, Integer> {
    private final PersonRepository<Student, Integer> repository;
    public StudentService(PersonRepository<Student, Integer> repository, Class<Student> clazz) {
        super(repository, clazz);
        this.repository = repository;
    }

//    public Student findByPIB(String firstName, String lastName, String middleName) throws EntityNotFoundException {
//        Optional<Student> student = repository.findByPIB(firstName, lastName, middleName);
//        if (student.isPresent()) {
//            return student.get();
//        } else {
//            throw new EntityNotFoundException("Student " + lastName + " " + firstName + " doesn't exist.");
//        }
//    }

    public List<Student> findAllByGroupId(int groupId) {
        return repository.findAll().stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getId() == groupId)
                .collect(Collectors.toList());
    }

    public void updateStudent(Student student) throws EntityNotFoundException {
        if (repository.findById(student.getId()).isEmpty()) {
            throw new EntityNotFoundException("Student with id " + student.getId() + " doesn't exist.");
        }
        repository.save(student);
    }

    public String generateRecordbookNum(String lastName, int id, int year) {
        Random rand = new Random();

        char firstChar = !lastName.isEmpty() ? lastName.charAt(0) : 'X';
        char secondChar = lastName.length() > 1 ? lastName.charAt(1) : 'Y';
        int safeYear = (year > 0) ? year : LocalDate.now().getYear();

        return firstChar + "-" + (id % safeYear) + secondChar + "-" + rand.nextInt(999);
    }

    public long getStudentsCount(int groupId) throws EntityNotFoundException {
        if (repository.findAll().isEmpty()) {
            return 0;
        }
        return repository.findAll().stream().filter(s -> s.getGroup() != null && s.getGroup().getId() == groupId).count();
    }

    public Student updateContacts(int studentID, String newPhoneNum, String newEmail) {
        if (repository.findById(studentID).isPresent()) {
            Student student = repository.findById(studentID).get();
            student.setPhoneNumber(newPhoneNum);
            student.setEmail(newEmail);
            repository.save(student);
            return student;
        } else  {
            throw new EntityNotFoundException("Student with id " + studentID + " doesn't exist.");
        }
    }

    public Student updateStudyForm(int studentID, StudyForm studyForm) {
        if (repository.findById(studentID).isPresent()) {
            Student student = repository.findById(studentID).get();
            student.setStudyForm(studyForm);
            repository.save(student);
            return student;
        } else  {
            throw new EntityNotFoundException("Student with id " + studentID + " doesn't exist.");
        }
    }

    public Student updateStudentStatus(int studentID, StudentStatus studentStatus) {
        if (repository.findById(studentID).isPresent()) {
            Student student = repository.findById(studentID).get();
            student.setStatus(studentStatus);
            repository.save(student);
            return student;
        } else {
            throw new EntityNotFoundException("Student with id " + studentID + " doesn't exist.");
        }
    }
}