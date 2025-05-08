package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Employee;
import org.example.entities.Project;
import org.example.entities.EmployeeActivity;
import org.example.services.EmployeeService;
import org.example.services.ProjectService;
import org.example.repositories.EmployeeActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeDashboardController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmployeeActivityRepository activityRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Check if user is logged in as employee
        Boolean isEmployee = (Boolean) session.getAttribute("isEmployee");
        if (isEmployee == null || !isEmployee) {
            return "redirect:/auth/employee-login";
        }

        // Get employee ID from session
        Long employeeId = (Long) session.getAttribute("employeeId");
        if (employeeId == null) {
            return "redirect:/auth/employee-login";
        }

        Employee employee = employeeService.findByEmployeeId(employeeId);
        if (employee == null) {
            return "redirect:/auth/employee-login";
        }

        List<Project> assignedProjects = projectService.getProjectsByEmployeeId(employeeId);
        int projectCount = assignedProjects.size();
        int completedTasks = 0; // TODO: Implement task tracking
        int pendingTasks = 0;   // TODO: Implement task tracking

        // Get recent activities
        List<EmployeeActivity> recentActivities = activityRepository.findTop5RecentActivitiesByEmployee(employee);

        model.addAttribute("employee", employee);
        model.addAttribute("assignedProjects", assignedProjects);
        model.addAttribute("projectCount", projectCount);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("pendingTasks", pendingTasks);
        model.addAttribute("recentActivities", recentActivities);

        return "employees/dashboard";
    }
} 