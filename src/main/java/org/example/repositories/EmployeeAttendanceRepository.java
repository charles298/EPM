package org.example.repositories;

import org.example.entities.Attendance;
import org.example.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeAttendanceRepository extends JpaRepository<Attendance, Long> {
    
    @Query("SELECT a FROM Attendance a WHERE a.employee = :employee AND a.date = :date")
    Attendance findByEmployeeAndDate(@Param("employee") Employee employee, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE a.employee = :employee ORDER BY a.date DESC")
    List<Attendance> findRecentAttendanceByEmployee(@Param("employee") Employee employee);
    
    @Query("SELECT a FROM Attendance a WHERE a.employee = :employee AND a.date = :date AND a.clockOut IS NULL")
    Attendance findIncompleteAttendance(@Param("employee") Employee employee, @Param("date") LocalDate date);
} 