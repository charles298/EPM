package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="employee_generator")
    @SequenceGenerator(name = "employee_generator", sequenceName = "employee_seq", allocationSize = 1)
    private long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String department;

    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    private List<Project> projects;

    public Employee() {}

    public Employee(String firstName, String lastName, String email, String username, String password, String department) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}