package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Attendance;
import org.example.entities.Employee;
import org.example.entities.EmployeeActivity;
import org.example.repositories.AttendanceRepository;
import org.example.repositories.EmployeeActivityRepository;
import org.example.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeActivityRepository activityRepository;

    @GetMapping("/attendance")
    public String showAttendancePage(HttpSession session, Model model) {
        Long employeeId = (Long) session.getAttribute("employeeId");
        if (employeeId == null) {
            return "redirect:/auth/employee-login";
        }

        Employee employee = employeeService.findById(employeeId);
        List<Attendance> attendanceRecords = attendanceRepository.findRecentAttendanceByEmployee(employee);
        
        // Check if employee is currently clocked in
        Attendance todayRecord = attendanceRepository.findByEmployeeAndDate(employee, LocalDate.now());
        if (todayRecord != null && todayRecord.getClockOut() == null) {
            model.addAttribute("isClockedIn", true);
        } else {
            model.addAttribute("isClockedIn", false);
        }
        
        model.addAttribute("attendanceRecords", attendanceRecords);
        return "employees/attendance";
    }

    @PostMapping("/clock")
    public String handleClockInOut(@RequestParam String action,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long employeeId = (Long) session.getAttribute("employeeId");
        if (employeeId == null) {
            return "redirect:/auth/employee-login";
        }

        Employee employee = employeeService.findById(employeeId);
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        try {
            if ("in".equals(action)) {
                // Check if already clocked in today
                Attendance existingRecord = attendanceRepository.findByEmployeeAndDate(employee, today);
                if (existingRecord != null) {
                    redirectAttributes.addFlashAttribute("error", "You have already clocked in today!");
                    return "redirect:/employees/attendance";
                }

                // Create new attendance record
                Attendance attendance = new Attendance();
                attendance.setEmployee(employee);
                attendance.setDate(today);
                attendance.setClockIn(currentTime);
                attendance.setStatus(Attendance.AttendanceStatus.INCOMPLETE);
                attendanceRepository.save(attendance);

                // Create clock in activity
                EmployeeActivity activity = new EmployeeActivity(
                    employee,
                    "ATTENDANCE",
                    "Clocked in at " + currentTime,
                    "COMPLETED"
                );
                activityRepository.save(activity);

                redirectAttributes.addFlashAttribute("success", "Successfully clocked in at " + currentTime);
            } else if ("out".equals(action)) {
                // Find incomplete attendance record
                Attendance attendance = attendanceRepository.findIncompleteAttendance(employee, today);
                if (attendance == null) {
                    redirectAttributes.addFlashAttribute("error", "No active clock-in record found for today!");
                    return "redirect:/employees/attendance";
                }

                // Update attendance record
                attendance.setClockOut(currentTime);
                attendance.setStatus(Attendance.AttendanceStatus.COMPLETE);
                attendanceRepository.save(attendance);

                // Create clock out activity
                EmployeeActivity activity = new EmployeeActivity(
                    employee,
                    "ATTENDANCE",
                    "Clocked out at " + currentTime,
                    "COMPLETED"
                );
                activityRepository.save(activity);

                redirectAttributes.addFlashAttribute("success", "Successfully clocked out at " + currentTime);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while processing your request.");
            return "redirect:/employees/attendance";
        }

        return "redirect:/employees/attendance";
    }
} 