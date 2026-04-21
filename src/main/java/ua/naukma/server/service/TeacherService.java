package ua.naukma.server.service;

import ua.naukma.domain.*;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class TeacherService extends EntityService<Teacher, Integer> {
    private final PersonRepository<Teacher, Integer> repository;
    public TeacherService(PersonRepository<Teacher, Integer> repository, Class<Teacher> clazz) {
        super(repository, clazz);
        this.repository = repository;
    }
    public List<Teacher> findAllByDepartmentId(int departmentId) {
        return repository.findAll().stream()
                .filter(t -> t.getDepartment() != null && t.getDepartment().getId() == departmentId)
                .collect(Collectors.toList());
    }

    public int getAllTeachersCount(int departmentId) {
        return (int) repository.findAll().stream().filter(t -> t.getDepartment().getId() == departmentId).count();
    }

    public List<Teacher> findByPIB(String lastName, String firstName, String middleName) {
        return repository.findAll().stream()
                .filter(t -> t.getLastName().equalsIgnoreCase(lastName) &&
                        t.getFirstName().equalsIgnoreCase(firstName) &&
                        t.getMiddleName().equalsIgnoreCase(middleName))
                .collect(Collectors.toList());
    }

    public Teacher updateContacts(int teacherId, String newPhone, String newEmail) {
        Teacher teacher = findById(teacherId);
        teacher.setPhoneNumber(newPhone);
        teacher.setEmail(newEmail);
        repository.save(teacher);
        return teacher;
    }

    public Teacher updateAcademicInfo(int teacherId, TeacherPosition position, TeacherDegree degree, TeacherRank rank, double load) {
        Teacher teacher = findById(teacherId);
        teacher.setPosition(position);
        teacher.setDegree(degree);
        teacher.setRank(rank);
        teacher.setLoad(load);
        repository.save(teacher);
        return teacher;
    }

    public List<Teacher> findAllByDepartmentIdSortedByName(int departmentId) {
        java.text.Collator ukrainianCollator = java.text.Collator.getInstance(new java.util.Locale("uk", "UA"));
        return findAllByDepartmentId(departmentId).stream()
                .sorted((t1, t2) -> ukrainianCollator.compare(t1.getName(), t2.getName()))
                .collect(Collectors.toList());
    }

    public List<Teacher> findAllByFacultyIdSortedByName(int facultyId) {
        java.text.Collator ukrainianCollator = java.text.Collator.getInstance(new java.util.Locale("uk", "UA"));
        return repository.findAll().stream()
                .filter(t -> t.getDepartment() != null
                        && t.getDepartment().getFaculty() != null
                        && t.getDepartment().getFaculty().getId() == facultyId)
                .sorted((t1, t2) -> ukrainianCollator.compare(t1.getName(), t2.getName()))
                .collect(Collectors.toList());
    }
}