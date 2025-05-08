package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TimeChartData;
import org.example.entities.Employee;
import org.example.entities.Project;
import org.example.services.EmployeeService;
import org.example.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public String displayProjects(Model model) {
        List<Project> projects = projectService.getAll();
        model.addAttribute("projectsList", projects);
        return "admin/projects/list-projects";
    }

    @GetMapping("/new")
    public String displayProjectForm(Model model) {
        Project aProject = new Project();
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("project", aProject);
        model.addAttribute("allEmployees", employees);
        return "admin/projects/new-project";
    }

    @PostMapping("/save")
    public String createProject(Project project, Model model) {
        projectService.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable("id") long id) {
        projectService.delete(id);
        return "redirect:/projects";
    }

    @GetMapping("/timelines")
    public String displayProjectTimelines(Model model) throws JsonProcessingException {
        List<TimeChartData> timelineData = projectService.getTimeData();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTimelineString = objectMapper.writeValueAsString(timelineData);
        model.addAttribute("projectTimeList", jsonTimelineString);
        return "admin/projects/project-timelines";
    }

    @GetMapping("/edit/{id}")
    public String displayEditProjectForm(@PathVariable("id") long id, Model model) {
        Project project = projectService.findById(id);
        if (project == null) {
            return "redirect:/projects";
        }
        List<Employee> allEmployees = employeeService.getAllEmployees();
        model.addAttribute("project", project);
        model.addAttribute("allEmployees", allEmployees);
        return "admin/projects/edit-project";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable("id") long id, @ModelAttribute Project project) {
        project.setProjectId(id);
        projectService.save(project);
        return "redirect:/projects";
    }

}