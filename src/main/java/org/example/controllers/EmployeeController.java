package org.example.controllers;

import org.example.dto.EmployeeProject;
import org.example.entities.Employee;
import org.example.entities.Project;
import org.example.services.EmployeeService;
import org.example.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    ProjectService projectService;

    @GetMapping
    public String displayEmployees(Model model) {
        List<Employee> employeesList = employeeService.getAllEmployees();
        model.addAttribute("employeesList", employeesList);
        return "admin/employees/list-employees";
    }

    @GetMapping("/new")
    public String displayEmployeeForm(Model model) {
        Employee anEmployee = new Employee();
        model.addAttribute("employee", anEmployee);
        return "admin/employees/new-employee";
    }

    @PostMapping("/save")
    public String createEmployee(Employee employee, Model model) {
        employeeService.save(employee);
        return "redirect:/admin/employees";
    }

    @GetMapping("/update")
    public String displayEmployeeUpdateForm(@RequestParam("id") Long id, Model model) {
        Employee theEmp = employeeService.findByEmployeeId(id);
        model.addAttribute("employee", theEmp);
        return "admin/employees/new-employee";
    }

    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam("id") Long id, Model model) {
        Employee theEmp = employeeService.findByEmployeeId(id);
        employeeService.delete(theEmp);
        return "redirect:/admin/employees";
    }

    @GetMapping("/")
    public String displayHome(Model model) {
        List<EmployeeProject> employeeProjects = employeeService.employeeProjects();
        List<Project> projectsList = projectService.getAll();
        model.addAttribute("employeeProjects", employeeProjects);
        model.addAttribute("projectsList", projectsList);
        return "admin/home";
    }
}