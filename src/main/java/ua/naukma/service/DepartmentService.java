package ua.naukma.service;

import ua.naukma.domain.Department;
import ua.naukma.domain.Faculty;
import ua.naukma.domain.Teacher;
import ua.naukma.domain.University;
import ua.naukma.repository.Repository;
import ua.naukma.utils.EmailVerificator;
import ua.naukma.utils.FacilityNameVerificator;
import ua.naukma.utils.IdVerificator;

import java.util.Optional;
import java.util.Scanner;

public class DepartmentService {
    private final Repository<Department, Integer> repository;
    private final FacultyService facultyService;
    public DepartmentService(University uni, Faculty faculty) {
        this.repository = faculty.getDepartments();
        this.facultyService = new FacultyService(uni);
    }
    public void addDepartment() {
        Department newD = department_validate_all();
        try_AddDepartment(newD);
    }
    private void try_AddDepartment(Department d) {
        Optional<Department> dep = repository.findById(d.getId());
        if (dep.isPresent()) {
            System.out.println("Department with such id already exists.");
            return;
        }
        repository.save(d);
    }
    private Department department_validate_all(){
        int id = IdVerificator.ask_id();
        String name = FacilityNameVerificator.ask_facility_name();
        String email = EmailVerificator.ask_email();
        Teacher tc = null;
        Faculty fac = facultyService.findFacultyById();
        String location = ask_location();
        Department d = new Department(id, name, fac, tc, location,  email);
        return d;
    }
    private String ask_location(){
        System.out.println("Please enter the location of the faculty.");
        Scanner sc = try_init_scanner();
        String location = sc.nextLine();
        return location;
    }
    public void deleteDepartment() {
        int id = IdVerificator.ask_id();
        Optional<Department> d = repository.findById(id);
        if (d.isPresent()) {
            repository.deleteById(id);
        }
        else {
            System.out.println("Department with such id doesn't exist.");
        }
    }
    public Department findDepartmentById() {
        System.out.println("Finding department by id.");
        int id = IdVerificator.ask_id();
        Optional<Department> department = repository.findById(id);
        if (department.isPresent()){
            System.out.println(department.get());
            return department.get();
        }else{
            System.out.println("Department with such id doesn't exist.");
            return null;
        }
    }
    public void departmentsShowList(){
        repository.showAll();
    }
    private static Scanner try_init_scanner() {
        Scanner scanner = null;
        while (scanner == null) {
            try {
                scanner = new Scanner(System.in);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        return scanner;
    }
}
