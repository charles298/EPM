package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ChartData;
import org.example.dto.EmployeeProject;
import org.example.entities.Project;
import org.example.services.EmployeeService;
import org.example.services.ProjectService;
import org.example.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    ProjectService projectService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    LeaveService leaveService;

    @GetMapping("/")
    public String displayHome(Model model) throws JsonProcessingException {
        // Get all projects
        List<Project> projects = projectService.getAll();
        model.addAttribute("projectsList", projects);

        // Get project status data for charts
        List<ChartData> projectData = projectService.getProjectStatus();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(projectData);
        model.addAttribute("projectStatusCount", jsonString);

        // Get employee project counts
        List<EmployeeProject> employeesProjectCount = employeeService.employeeProjects();
        model.addAttribute("employeesListProjectCount", employeesProjectCount);

        // Get project names for each employee
        Map<Long, List<String>> employeeProjectNames = new HashMap<>();
        for (EmployeeProject ep : employeesProjectCount) {
            List<String> projectNames = projectService.getProjectNamesByEmployeeId(ep.getEmployeeId());
            employeeProjectNames.put(ep.getEmployeeId(), projectNames);
        }
        model.addAttribute("employeeProjectNames", employeeProjectNames);

        // Get pending leaves count
        long pendingLeaves = leaveService.countPendingLeaves();
        model.addAttribute("pendingLeaves", pendingLeaves);

        // Get project counts by status
        long pendingCount = projects.stream()
                .filter(p -> p.getStatus() != null && p.getStatus().name().equals("PENDING"))
                .count();
        model.addAttribute("pendingCount", pendingCount);

        return "admin/home";
    }
}