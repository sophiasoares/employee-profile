package com.employeeprofile.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "absence_requests")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AbsenceRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Employee requesting the absence
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"feedbacks", "absenceRequests", "directReports", "manager"})
    private Employee employee;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "absence_type", nullable = false)
    private AbsenceType absenceType;
    
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @NotBlank(message = "Reason is required")
    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public AbsenceRequest() {}
    
    public AbsenceRequest(Employee employee, AbsenceType absenceType, LocalDate startDate, 
                         LocalDate endDate, String reason) {
        this.employee = employee;
        this.absenceType = absenceType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public AbsenceType getAbsenceType() { return absenceType; }
    public void setAbsenceType(AbsenceType absenceType) { this.absenceType = absenceType; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public long getDurationInDays() {
        if (startDate != null && endDate != null) {
            long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            return days;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "AbsenceRequest{" +
                "id=" + id +
                ", absenceType=" + absenceType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", durationInDays=" + getDurationInDays() +
                '}';
    }
}
