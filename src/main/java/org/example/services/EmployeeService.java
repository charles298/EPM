package org.example.services;

import org.example.dao.EmployeeRepository;
import org.example.dto.EmployeeProject;
import org.example.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee save(Employee employee) {
        // Encrypt password before saving
        if (employee.getPassword() != null && !employee.getPassword().startsWith("$2a$")) {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        return employeeRepo.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public List<EmployeeProject> employeeProjects() {
        return employeeRepo.employeeProjects();
    }

    public Employee findByEmployeeId(Long id) {
        return employeeRepo.findByEmployeeId(id);
    }

    public Employee findByUsername(String username) {
        return employeeRepo.findByUsername(username);
    }

    public void delete(Employee theEmp) {
        employeeRepo.delete(theEmp);
    }

    public Employee findById(Long id) {
        return employeeRepo.findById(id).orElse(null);
    }

    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }
}