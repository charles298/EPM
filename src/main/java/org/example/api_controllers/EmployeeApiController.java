package org.example.api_controllers;

import org.example.dao.EmployeeRepository;
import org.example.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app-api/employees")
public class EmployeeApiController {
    @Autowired
    EmployeeRepository empRepo;

    @GetMapping
    public List<Employee> getEmployees() {
        return empRepo.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        return empRepo.findById(id).get();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        return empRepo.save(employee);
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Employee update(@RequestBody Employee employee) {
        return empRepo.save(employee);
    }
}
