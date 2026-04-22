package ua.naukma.server.service;

import ua.naukma.domain.Faculty;
import ua.naukma.domain.Person;
import ua.naukma.domain.Teacher;
import ua.naukma.exception.DuplicateEntityException;
import ua.naukma.exception.EntityNotFoundException;
import ua.naukma.server.repository.PersonRepository;
import ua.naukma.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ua.naukma.server.annotation.Service
public class FacultyService extends EntityService<Faculty, Integer> {
    private final Repository<Faculty, Integer> repository;
    private final PersonRepository<Teacher, Integer> teacherRepository;

    public FacultyService(Repository<Faculty, Integer> repository, PersonRepository<Teacher, Integer> teacherRepository,Class<Faculty> faculty) {
        super(repository, faculty);
        this.repository = repository;
        this.teacherRepository = teacherRepository;
    }
    public List<Faculty> findAllByUniId(int uniId) throws EntityNotFoundException {
        return repository.findAll().stream()
                .filter(f -> f.getUniversity().getId() == uniId)
                .toList();
    }

    public int getAllFacultiesCount(int uniId) throws EntityNotFoundException {
        return (int) repository.findAll().stream().filter(f -> f.getUniversity() != null
                && f.getUniversity().getId() == uniId).count();
    }

    public Teacher setDean(int facultyId, int teacherId) {
        if (repository.findById(facultyId).isPresent()) {
            if (teacherRepository.findById(teacherId).isPresent()) {

                Faculty faculty = repository.findById(facultyId).get();
                Teacher teacher = teacherRepository.findById(teacherId).get();

                faculty.setDean(teacher);
                repository.save(faculty);

                return teacher;

            } else {
                throw new EntityNotFoundException("Teacher with id " + teacherId + " doesn't exist.");
            }
        } else {
            throw new EntityNotFoundException("Faculty with id " + facultyId + " doesn't exist.");
        }
    }

    public Faculty updateContacts(int facultyId, String newEmail) {
        Faculty faculty = findById(facultyId);
        faculty.setEmail(newEmail);
        repository.save(faculty);
        return faculty;
    }
}