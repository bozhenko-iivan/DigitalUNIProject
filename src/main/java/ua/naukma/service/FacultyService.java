package ua.naukma.service;

import ua.naukma.domain.*;
import ua.naukma.repository.Repository;
import ua.naukma.utils.EmailVerificator;
import ua.naukma.utils.FacilityNameVerificator;
import ua.naukma.utils.IdVerificator;

import java.util.Optional;


public class FacultyService {
    private final Repository<Faculty, Integer> repository;
    public FacultyService(University uni) {
        this.repository = uni.getFaculties();
    }
    public void addFaculty() {
        Faculty newF = faculty_validate_all();
        try_AddFaculty(newF);
    }
    private void try_AddFaculty(Faculty f) {
            Optional<Faculty> faculty = repository.findById(f.getId());
            if (faculty.isPresent()) {
                System.out.println("Faculty with such id already exists.");
                return;
            }
            repository.save(f);
    }
    private Faculty faculty_validate_all(){
        int id = IdVerificator.ask_id();
        String name = FacilityNameVerificator.ask_facility_name();
        String shortname = FacilityNameVerificator.ask_short_name();
        String email = EmailVerificator.ask_email();
        Teacher tc = null;
        Faculty f = new Faculty(id, name, shortname, tc, email);
        return f;
    }
    public void deleteFaculty() {
        int id = IdVerificator.ask_id();
        Optional<Faculty> f = repository.findById(id);
        if (f.isPresent()) {
            repository.deleteById(id);
        }
        else {
            System.out.println("Faculty with such id doesn't exist.");
        }
    }
    public Faculty findFacultyById() {
        System.out.println("Finding faculty with id.");
        int id = IdVerificator.ask_id();
        Optional<Faculty> faculty = repository.findById(id);
        if (faculty.isPresent()){
            System.out.println(faculty.get());
            return faculty.get();
        }else{
            System.out.println("Faculty with such id doesn't exist.");
            return null;
        }
    }
    public void facultiesShowList(){
        repository.showAll();
    }
}