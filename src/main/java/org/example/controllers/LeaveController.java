package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.entities.Leave;
import org.example.entities.Employee;
import org.example.entities.EmployeeActivity;
import org.example.repositories.LeaveRepository;
import org.example.repositories.EmployeeActivityRepository;
import org.example.services.EmployeeService;
import org.example.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class LeaveController {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeActivityRepository activityRepository;

    // Employee leave request page
    @GetMapping("/employees/leave")
    public String showLeavePage(HttpSession session, Model model) {
        // Check if employee is logged in
        Long employeeId = (Long) session.getAttribute("employeeId");
        String employeeName = (String) session.getAttribute("employeeName");
        
        if (employeeId == null || employeeName == null) {
            return "redirect:/auth/employee-login";
        }

        try {
            Employee employee = employeeService.findById(employeeId);
            if (employee == null) {
                session.invalidate();
                return "redirect:/auth/employee-login";
            }

            List<Leave> leaveHistory = leaveRepository.findLeavesByEmployee(employee);
            
            model.addAttribute("leaveHistory", leaveHistory);
            model.addAttribute("leaveTypes", Leave.LeaveType.values());
            return "employees/leave";
        } catch (Exception e) {
            session.invalidate();
            return "redirect:/auth/employee-login";
        }
    }

    // Employee submit leave request
    @PostMapping("/employees/leave/request")
    public String submitLeaveRequest(@ModelAttribute Leave leave,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        // Check if employee is logged in
        Long employeeId = (Long) session.getAttribute("employeeId");
        String employeeName = (String) session.getAttribute("employeeName");
        
        if (employeeId == null || employeeName == null) {
            return "redirect:/auth/employee-login";
        }

        try {
            Employee employee = employeeService.findById(employeeId);
            if (employee == null) {
                session.invalidate();
                return "redirect:/auth/employee-login";
            }

            leave.setEmployee(employee);
            leave.setStatus(Leave.LeaveStatus.PENDING);
            
            // Validate dates
            Date now = new Date();
            if (leave.getStartDate().before(now)) {
                redirectAttributes.addFlashAttribute("error", "Start date cannot be in the past!");
                return "redirect:/employees/leave";
            }
            
            if (leave.getEndDate().before(leave.getStartDate())) {
                redirectAttributes.addFlashAttribute("error", "End date cannot be before start date!");
                return "redirect:/employees/leave";
            }

            leaveRepository.save(leave);

            // Create leave request activity
            EmployeeActivity activity = new EmployeeActivity(
                employee,
                "LEAVE",
                "Submitted " + leave.getLeaveType() + " request from " + leave.getStartDate() + " to " + leave.getEndDate(),
                "PENDING"
            );
            activityRepository.save(activity);

            redirectAttributes.addFlashAttribute("success", "Leave request submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while submitting your leave request.");
        }

        return "redirect:/employees/leave";
    }

    // Admin leave management page
    @GetMapping("/admin/leaves")
    @PreAuthorize("hasRole('ADMIN')")
    public String listLeaves(Model model) {
        List<Leave> leaves = leaveService.getAllLeaves();
        model.addAttribute("leaveRequests", leaves);
        return "admin/leaves/list-leaves";
    }

    // Admin filter leaves
    @GetMapping("/admin/leaves/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public String filterLeaves(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String department,
            Model model) {
        
        if (status != null && !status.equals("ALL")) {
            if (department != null && !department.equals("ALL")) {
                model.addAttribute("leaveRequests", 
                    leaveService.getLeavesByDepartmentAndStatus(department, Leave.LeaveStatus.valueOf(status)));
            } else {
                model.addAttribute("leaveRequests", 
                    leaveService.getLeavesByStatus(Leave.LeaveStatus.valueOf(status)));
            }
        } else if (department != null && !department.equals("ALL")) {
            model.addAttribute("leaveRequests", 
                leaveService.getLeavesByDepartment(department));
        } else {
            model.addAttribute("leaveRequests", leaveService.getAllLeaves());
        }
        
        return "admin/leaves/list-leaves";
    }

    // Admin approve leave
    @PostMapping("/admin/leaves/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveLeave(@PathVariable Long id) {
        Leave leave = leaveService.approveLeave(id);
        
        // Create leave approval activity
        EmployeeActivity activity = new EmployeeActivity(
            leave.getEmployee(),
            "LEAVE",
            leave.getLeaveType() + " request approved for " + leave.getStartDate() + " to " + leave.getEndDate(),
            "COMPLETED"
        );
        activityRepository.save(activity);
        
        return "redirect:/admin/leaves";
    }

    // Admin reject leave
    @PostMapping("/admin/leaves/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectLeave(@PathVariable Long id) {
        Leave leave = leaveService.rejectLeave(id);
        
        // Create leave rejection activity
        EmployeeActivity activity = new EmployeeActivity(
            leave.getEmployee(),
            "LEAVE",
            leave.getLeaveType() + " request rejected for " + leave.getStartDate() + " to " + leave.getEndDate(),
            "CANCELLED"
        );
        activityRepository.save(activity);
        
        return "redirect:/admin/leaves";
    }
} 