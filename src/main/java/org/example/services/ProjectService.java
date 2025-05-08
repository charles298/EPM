package org.example.services;

import org.example.dao.ProjectRepository;
import org.example.dto.ChartData;
import org.example.dto.TimeChartData;
import org.example.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepo;

    public Project save(Project project) {
        return projectRepo.save(project);
    }

    public List<Project> getAll() {
        return projectRepo.findAll();
    }

    public Project findById(long id) {
        return projectRepo.findById(id).orElse(null);
    }

    public void delete(long id) {
        projectRepo.deleteById(id);
    }

    public List<ChartData> getProjectStatus() {
        return projectRepo.getProjectStatus();
    }

    public List<TimeChartData> getTimeData() {
        return projectRepo.getTimeData();
    }

    public List<String> getProjectNamesByEmployeeId(Long employeeId) {
        return projectRepo.findProjectNamesByEmployeeId(employeeId);
    }

    public List<Project> getProjectsByEmployeeId(Long employeeId) {
        return projectRepo.findProjectsByEmployeeId(employeeId);
    }
}