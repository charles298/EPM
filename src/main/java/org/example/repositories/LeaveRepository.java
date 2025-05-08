package org.example.repositories;

import org.example.entities.Leave;
import org.example.entities.Employee;
import org.example.entities.Leave.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    
    @Query("SELECT l FROM Leave l WHERE l.employee = :employee ORDER BY l.startDate DESC")
    List<Leave> findLeavesByEmployee(@Param("employee") Employee employee);
    
    @Query("SELECT l FROM Leave l WHERE l.employee = :employee AND l.status = 'PENDING' ORDER BY l.startDate DESC")
    List<Leave> findPendingLeavesByEmployee(@Param("employee") Employee employee);

    @Query("SELECT l FROM Leave l WHERE l.employee.employeeId = :employeeId")
    List<Leave> findByEmployeeId(@Param("employeeId") Long employeeId);
    
    List<Leave> findByStatus(LeaveStatus status);
    
    @Query("SELECT l FROM Leave l WHERE l.employee.department = :department")
    List<Leave> findByEmployeeDepartment(@Param("department") String department);
    
    @Query("SELECT l FROM Leave l WHERE l.employee.department = :department AND l.status = :status")
    List<Leave> findByEmployeeDepartmentAndStatus(@Param("department") String department, @Param("status") LeaveStatus status);
} 