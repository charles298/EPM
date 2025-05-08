package org.example.controllers;

import org.example.entities.Attendance;
import org.example.entities.Employee;
import org.example.repositories.AttendanceRepository;
import org.example.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminAttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/attendance-list")
    public String showAttendanceList(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // Get all employees for the dropdown
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);

        // Parse dates if provided
        LocalDate fromDate = null;
        LocalDate toDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (dateFrom != null && !dateFrom.isEmpty()) {
            fromDate = LocalDate.parse(dateFrom, formatter);
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            toDate = LocalDate.parse(dateTo, formatter);
        }

        // Create pageable object for pagination
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        // Get attendance records based on filters
        Page<Attendance> attendancePage;
        if (employeeId != null) {
            Employee employee = employeeService.findById(employeeId);
            if (fromDate != null && toDate != null) {
                attendancePage = attendanceRepository.findByEmployeeAndDateBetween(employee, fromDate, toDate, pageable);
            } else {
                attendancePage = attendanceRepository.findByEmployee(employee, pageable);
            }
        } else {
            if (fromDate != null && toDate != null) {
                attendancePage = attendanceRepository.findByDateBetween(fromDate, toDate, pageable);
            } else {
                attendancePage = attendanceRepository.findAll(pageable);
            }
        }

        // Add attributes to the model
        model.addAttribute("attendanceList", attendancePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/attendance-list";
    }
} 