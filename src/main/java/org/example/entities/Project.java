package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_generator")
    @SequenceGenerator(name = "project_generator", sequenceName = "project_seq", allocationSize = 1)
    private long projectId;
    private String name;
    private String stage;
    private String description;

    public enum ProjectStatus {
        PENDING,
        ACTIVE,
        COMPLETED,
        ON_HOLD,
        CANCELLED
    }
    
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    private int progress;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ManyToMany
    @JoinTable(
            name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @JsonIgnore
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "assigned_member_id")
    private Employee assignedMember;

    public Project() {
        this.employees = new ArrayList<>();
        this.progress = 0;
        this.status = ProjectStatus.PENDING;
    }

    public Project(String name, String stage, String description) {
        this();
        this.name = name;
        this.stage = stage;
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addEmployee(Employee emp) {
        if(employees == null) {
            employees = new ArrayList<>();
        }
        employees.add(emp);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Employee getAssignedMember() {
        return assignedMember;
    }

    public void setAssignedMember(Employee assignedMember) {
        this.assignedMember = assignedMember;
    }
}