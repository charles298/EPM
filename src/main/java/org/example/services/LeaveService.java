package org.example.services;

import org.example.entities.Leave;
import org.example.entities.Leave.LeaveStatus;
import org.example.repositories.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public List<Leave> getLeavesByStatus(LeaveStatus status) {
        return leaveRepository.findByStatus(status);
    }

    public List<Leave> getLeavesByDepartment(String department) {
        return leaveRepository.findByEmployeeDepartment(department);
    }

    public List<Leave> getLeavesByDepartmentAndStatus(String department, LeaveStatus status) {
        return leaveRepository.findByEmployeeDepartmentAndStatus(department, status);
    }

    public List<Leave> getLeavesByEmployee(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    public Leave approveLeave(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        leave.setStatus(LeaveStatus.APPROVED);
        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave rejectLeave(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        leave.setStatus(LeaveStatus.REJECTED);
        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave createLeave(Leave leave) {
        leave.setStatus(LeaveStatus.PENDING);
        return leaveRepository.save(leave);
    }

    public long countPendingLeaves() {
        return leaveRepository.findByStatus(LeaveStatus.PENDING).size();
    }
} 