package org.example.dao;

import org.example.dto.EmployeeProject;
import org.example.entities.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    @Override
    List<Employee> findAll();

    @Query("SELECT new org.example.dto.EmployeeProject(e.firstName, e.lastName, COUNT(p.projectId)) " +
            "FROM Employee e LEFT JOIN e.projects p GROUP BY e.firstName, e.lastName")
    List<EmployeeProject> employeeProjects();

    Employee findByEmployeeId(Long id);

    Employee findByUsername(String username);
}