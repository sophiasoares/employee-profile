package com.employeeprofile.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "absence_requests")
public class AbsenceRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Employee requesting the absence
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    // Manager who approved/rejected the request
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id")
    private Employee approvedBy;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "absence_type", nullable = false)
    private AbsenceType absenceType;
    
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "is_half_day")
    private Boolean isHalfDay = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "half_day_period")
    private HalfDayPeriod halfDayPeriod;
    
    @NotBlank(message = "Reason is required")
    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Size(max = 1000, message = "Manager comments must not exceed 1000 characters")
    @Column(name = "manager_comments", columnDefinition = "TEXT")
    private String managerComments;
    
    @Enumerated(EnumType.STRING)
    private AbsenceStatus status = AbsenceStatus.PENDING;
    
    // Emergency contact during absence
    @Size(max = 100, message = "Emergency contact must not exceed 100 characters")
    @Column(name = "emergency_contact")
    private String emergencyContact;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Emergency contact phone should be valid")
    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;
    
    // Work delegation
    @Size(max = 1000, message = "Work delegation notes must not exceed 1000 characters")
    @Column(name = "work_delegation_notes", columnDefinition = "TEXT")
    private String workDelegationNotes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegated_to_id")
    private Employee delegatedTo;
    
    // Dates for tracking
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;
    
    // System fields
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public AbsenceRequest() {}
    
    public AbsenceRequest(Employee employee, AbsenceType absenceType, LocalDate startDate, 
                         LocalDate endDate, String reason) {
        this.employee = employee;
        this.absenceType = absenceType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.requestedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public Employee getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Employee approvedBy) { this.approvedBy = approvedBy; }
    
    public AbsenceType getAbsenceType() { return absenceType; }
    public void setAbsenceType(AbsenceType absenceType) { this.absenceType = absenceType; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public Boolean getIsHalfDay() { return isHalfDay; }
    public void setIsHalfDay(Boolean isHalfDay) { this.isHalfDay = isHalfDay; }
    
    public HalfDayPeriod getHalfDayPeriod() { return halfDayPeriod; }
    public void setHalfDayPeriod(HalfDayPeriod halfDayPeriod) { this.halfDayPeriod = halfDayPeriod; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getManagerComments() { return managerComments; }
    public void setManagerComments(String managerComments) { this.managerComments = managerComments; }
    
    public AbsenceStatus getStatus() { return status; }
    public void setStatus(AbsenceStatus status) { this.status = status; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    
    public String getWorkDelegationNotes() { return workDelegationNotes; }
    public void setWorkDelegationNotes(String workDelegationNotes) { this.workDelegationNotes = workDelegationNotes; }
    
    public Employee getDelegatedTo() { return delegatedTo; }
    public void setDelegatedTo(Employee delegatedTo) { this.delegatedTo = delegatedTo; }
    
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    
    public LocalDateTime getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(LocalDateTime rejectedAt) { this.rejectedAt = rejectedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Utility Methods
    public long getDurationInDays() {
        if (startDate != null && endDate != null) {
            long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            return isHalfDay ? 1 : days;
        }
        return 0;
    }
    
    public boolean isApproved() {
        return status == AbsenceStatus.APPROVED;
    }
    
    public boolean isPending() {
        return status == AbsenceStatus.PENDING;
    }
    
    public boolean isRejected() {
        return status == AbsenceStatus.REJECTED;
    }
    
    public void approve(Employee approver, String comments) {
        this.status = AbsenceStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
        this.managerComments = comments;
    }
    
    public void reject(Employee approver, String comments) {
        this.status = AbsenceStatus.REJECTED;
        this.approvedBy = approver;
        this.rejectedAt = LocalDateTime.now();
        this.managerComments = comments;
    }
    
    @Override
    public String toString() {
        return "AbsenceRequest{" +
                "id=" + id +
                ", absenceType=" + absenceType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", durationInDays=" + getDurationInDays() +
                '}';
    }
}
