package org.example.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_generator")
    @SequenceGenerator(name = "leave_generator", sequenceName = "leave_seq", allocationSize = 1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column
    private String comments;

    public enum LeaveType {
        ANNUAL,
        SICK,
        MATERNITY,
        PATERNITY,
        UNPAID,
        OTHER
    }

    public enum LeaveStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public Leave() {
    }

    public Leave(Employee employee, LeaveType leaveType, Date startDate, Date endDate, String reason) {
        this.employee = employee;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
} 