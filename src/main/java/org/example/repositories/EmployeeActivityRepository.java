package org.example.repositories;

import org.example.entities.EmployeeActivity;
import org.example.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeActivityRepository extends JpaRepository<EmployeeActivity, Long> {
    
    @Query("SELECT a FROM EmployeeActivity a WHERE a.employee = :employee ORDER BY a.timestamp DESC")
    List<EmployeeActivity> findRecentActivitiesByEmployee(@Param("employee") Employee employee);
    
    @Query("SELECT a FROM EmployeeActivity a WHERE a.employee = :employee ORDER BY a.timestamp DESC LIMIT 5")
    List<EmployeeActivity> findTop5RecentActivitiesByEmployee(@Param("employee") Employee employee);
} 