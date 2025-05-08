package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Employee;
import org.example.entities.Project;
import org.example.services.EmployeeService;
import org.example.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeProjectController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/my-projects")
    public String showAssignedProjects(HttpSession session, Model model) {
        // Check if user is logged in as employee
        Boolean isEmployee = (Boolean) session.getAttribute("isEmployee");
        if (isEmployee == null || !isEmployee) {
            return "redirect:/auth/employee-login";
        }

        // Get employee ID from session
        Long employeeId = (Long) session.getAttribute("employeeId");
        String employeeName = (String) session.getAttribute("employeeName");
        
        if (employeeId == null || employeeName == null) {
            session.invalidate();
            return "redirect:/auth/employee-login";
        }

        try {
            Employee employee = employeeService.findById(employeeId);
            if (employee == null) {
                session.invalidate();
                return "redirect:/auth/employee-login";
            }

            List<Project> assignedProjects = projectService.getProjectsByEmployeeId(employeeId);
            
            // If no projects are assigned, set an empty list instead of null
            if (assignedProjects == null) {
                assignedProjects = new ArrayList<>();
            }
            
            model.addAttribute("assignedProjects", assignedProjects);
            model.addAttribute("employee", employee);
            model.addAttribute("employeeName", employeeName);
            return "employees/projects";
        } catch (Exception e) {
            session.invalidate();
            return "redirect:/auth/employee-login";
        }
    }
} 