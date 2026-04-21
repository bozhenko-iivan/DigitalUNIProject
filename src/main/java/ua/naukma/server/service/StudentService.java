package ua.naukma.server.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;
import ua.naukma.server.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class StudentService extends EntityService<Student, Integer> {
    private final PersonRepository<Student, Integer> repository;
    private final Repository<Grade, Integer> gradeRepository;
    private final Repository<Group, Integer> groupRepository;

    public StudentService(PersonRepository<Student, Integer> repository,
                          Repository<Grade, Integer> gradeRepository,
                          Repository<Group, Integer> groupRepository,
                          Class<Student> clazz) {
        super(repository, clazz);
        this.repository = repository;
        this.gradeRepository = gradeRepository;
        this.groupRepository = groupRepository;
    }

    public List<Student> findAllByGroupId(int groupId) {
        return repository.findAll().stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getId() == groupId)
                .collect(Collectors.toList());
    }

    public List<Student> sortByIds(int groupId) {
        return findAllByGroupId(groupId).stream()
                .sorted(java.util.Comparator.comparingInt(Student::getId))
                .collect(Collectors.toList());
    }

    public List<Student> sortByName(int groupId) {
        java.text.Collator ukrainianCollator = java.text.Collator.getInstance(new java.util.Locale("uk", "UA"));
        return findAllByGroupId(groupId).stream()
                .sorted((s1, s2) -> ukrainianCollator.compare(s1.getName(), s2.getName()))
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

    public Double calculateAverageScore(int studentID) {
        findById(studentID);
        return gradeRepository.findAll()
                .stream()
                .filter(grade -> grade.getStudentId() == studentID)
                .collect(Collectors.averagingDouble(Grade::getScore));
    }

    public List<Student> findByPIB(String lastName, String firstName, String middleName) {
        return repository.findAll().stream()
                .filter(s -> s.getLastName().equalsIgnoreCase(lastName) &&
                        s.getFirstName().equalsIgnoreCase(firstName) &&
                        s.getMiddleName().equalsIgnoreCase(middleName))
                .collect(Collectors.toList());
    }

    public List<Student> findAllByCourse(int course) {
        return repository.findAll().stream()
                .filter(s -> s.getCourse() == course)
                .collect(Collectors.toList());
    }

    public Student changeCourse(int studentID, int newCourse) {
        Student student = findById(studentID);
        student.setCourse(newCourse);
        repository.save(student);
        return student;
    }

    public Student transferGroup(int studentId, int newGroupId) {
        Student student = findById(studentId);
        Group newGroup = groupRepository.findById(newGroupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + newGroupId + " not found."));
        student.setGroup(newGroup);
        repository.save(student);

        return student;
    }

    public List<Student> sortByCourse() {
        return findAll().stream()
                .sorted(java.util.Comparator.comparingInt(Student::getCourse))
                .collect(Collectors.toList());
    }

    public List<Student> findAllByFacultyIdSortedByName(int facultyId) {
        java.text.Collator collator = java.text.Collator.getInstance(new java.util.Locale("uk", "UA"));
        return repository.findAll().stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getFaculty() != null && s.getGroup().getFaculty().getId() == facultyId)
                .sorted((s1, s2) -> collator.compare(s1.getName(), s2.getName()))
                .collect(Collectors.toList());
    }

    public List<Student> findAllByFacultyIdSortedByCourse(int facultyId) {
        return repository.findAll().stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getFaculty() != null && s.getGroup().getFaculty().getId() == facultyId)
                .sorted(java.util.Comparator.comparingInt(Student::getCourse))
                .collect(Collectors.toList());
    }
}