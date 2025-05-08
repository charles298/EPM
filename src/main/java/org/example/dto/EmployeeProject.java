package org.example.dto;

public class EmployeeProject {
    private Long employeeId;
    private String employeeName;
    private long projectCount;

    public EmployeeProject(Long employeeId, String firstName, String lastName, long projectCount) {
        this.employeeId = employeeId;
        this.employeeName = firstName + " " + lastName;
        this.projectCount = projectCount;
    }

    public EmployeeProject(String firstName, String lastName, long projectCount) {
        this.employeeName = firstName + " " + lastName;
        this.projectCount = projectCount;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(long projectCount) {
        this.projectCount = projectCount;
    }
}