package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.AbsenceStatus;
import com.employeeprofile.backend.entity.AbsenceType;
import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.repository.AbsenceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AbsenceRequestService {

    @Autowired
    private AbsenceRequestRepository absenceRequestRepository;

    // Basic CRUD operations
    public List<AbsenceRequest> getAllAbsenceRequests() {
        return absenceRequestRepository.findAll();
    }

    public Optional<AbsenceRequest> getAbsenceRequestById(Long id) {
        return absenceRequestRepository.findById(id);
    }

    public AbsenceRequest saveAbsenceRequest(AbsenceRequest absenceRequest) {
        return absenceRequestRepository.save(absenceRequest);
    }

    public AbsenceRequest createAbsenceRequest(AbsenceRequest absenceRequest) {
        // Validate no overlapping absences
        if (hasOverlappingAbsence(absenceRequest)) {
            throw new RuntimeException("Employee already has an approved or pending absence request for this period");
        }
        
        absenceRequest.setStatus(AbsenceStatus.PENDING);
        return absenceRequestRepository.save(absenceRequest);
    }

    public AbsenceRequest updateAbsenceRequest(Long id, AbsenceRequest requestDetails) {
        AbsenceRequest request = absenceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence request not found with id: " + id));

        // Only allow updates if still pending
        if (request.getStatus() != AbsenceStatus.PENDING) {
            throw new RuntimeException("Cannot update absence request that is not pending");
        }

        // Validate no overlapping absences (excluding current request)
        if (hasOverlappingAbsence(requestDetails, id)) {
            throw new RuntimeException("Employee already has an approved or pending absence request for this period");
        }

        request.setAbsenceType(requestDetails.getAbsenceType());
        request.setStartDate(requestDetails.getStartDate());
        request.setEndDate(requestDetails.getEndDate());
        request.setIsHalfDay(requestDetails.getIsHalfDay());
        request.setHalfDayPeriod(requestDetails.getHalfDayPeriod());
        request.setReason(requestDetails.getReason());
        request.setEmergencyContact(requestDetails.getEmergencyContact());
        request.setEmergencyContactPhone(requestDetails.getEmergencyContactPhone());
        request.setWorkDelegationNotes(requestDetails.getWorkDelegationNotes());
        request.setDelegatedTo(requestDetails.getDelegatedTo());

        return absenceRequestRepository.save(request);
    }

    public void deleteAbsenceRequest(Long id) {
        AbsenceRequest request = absenceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence request not found with id: " + id));
        
        // Only allow deletion if pending
        if (request.getStatus() != AbsenceStatus.PENDING) {
            throw new RuntimeException("Cannot delete absence request that is not pending");
        }
        
        request.setStatus(AbsenceStatus.CANCELLED);
        absenceRequestRepository.save(request);
    }

    // Employee-specific operations
    public List<AbsenceRequest> getAbsenceRequestsForEmployee(Employee employee) {
        return absenceRequestRepository.findByEmployeeOrderByRequestedAtDesc(employee);
    }

    public Page<AbsenceRequest> getAbsenceRequestsForEmployee(Employee employee, Pageable pageable) {
        return absenceRequestRepository.findByEmployeeOrderByRequestedAtDesc(employee, pageable);
    }

    public List<AbsenceRequest> getPendingRequestsForEmployee(Employee employee) {
        return absenceRequestRepository.findByEmployeeAndStatusOrderByRequestedAtDesc(employee, AbsenceStatus.PENDING);
    }

    // Manager operations
    public List<AbsenceRequest> getPendingRequestsForManager(Employee manager) {
        return absenceRequestRepository.findPendingRequestsForManager(manager, AbsenceStatus.PENDING);
    }

    public List<AbsenceRequest> getPendingRequestsByDepartment(String department) {
        return absenceRequestRepository.findPendingRequestsByDepartment(department, AbsenceStatus.PENDING);
    }

    // Approval workflow
    public AbsenceRequest approveRequest(Long requestId, Employee approver, String comments) {
        AbsenceRequest request = absenceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Absence request not found with id: " + requestId));

        if (request.getStatus() != AbsenceStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be approved");
        }

        request.approve(approver, comments);
        return absenceRequestRepository.save(request);
    }

    public AbsenceRequest rejectRequest(Long requestId, Employee approver, String comments) {
        AbsenceRequest request = absenceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Absence request not found with id: " + requestId));

        if (request.getStatus() != AbsenceStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be rejected");
        }

        request.reject(approver, comments);
        return absenceRequestRepository.save(request);
    }

    // Current and upcoming absences
    public List<AbsenceRequest> getCurrentAbsences() {
        return absenceRequestRepository.findCurrentAbsences(LocalDate.now(), AbsenceStatus.APPROVED);
    }

    public List<AbsenceRequest> getCurrentAbsencesForEmployee(Employee employee) {
        return absenceRequestRepository.findCurrentAbsencesForEmployee(employee, LocalDate.now(), AbsenceStatus.APPROVED);
    }

    public List<AbsenceRequest> getUpcomingAbsences() {
        return absenceRequestRepository.findUpcomingAbsences(LocalDate.now(), AbsenceStatus.APPROVED);
    }

    public List<AbsenceRequest> getUpcomingAbsencesForEmployee(Employee employee) {
        return absenceRequestRepository.findUpcomingAbsencesForEmployee(employee, LocalDate.now(), AbsenceStatus.APPROVED);
    }

    // Search and filter operations
    public List<AbsenceRequest> getRequestsByStatus(AbsenceStatus status) {
        return absenceRequestRepository.findByStatusOrderByRequestedAtDesc(status);
    }

    public List<AbsenceRequest> getRequestsByType(AbsenceType absenceType) {
        return absenceRequestRepository.findByAbsenceTypeAndStatusOrderByStartDateDesc(absenceType, AbsenceStatus.APPROVED);
    }

    public List<AbsenceRequest> getRequestsByDepartment(String department) {
        return absenceRequestRepository.findByEmployeeDepartment(department);
    }

    // Statistics
    public long getRequestCountForEmployee(Employee employee, AbsenceStatus status) {
        return absenceRequestRepository.countByEmployeeAndStatus(employee, status);
    }

    public long getPendingRequestCount() {
        return absenceRequestRepository.countByStatusAndRequestedAtAfter(AbsenceStatus.PENDING, 
                LocalDate.now().minusYears(1).atStartOfDay());
    }

    // Validation helpers
    private boolean hasOverlappingAbsence(AbsenceRequest request) {
        return hasOverlappingAbsence(request, null);
    }

    private boolean hasOverlappingAbsence(AbsenceRequest request, Long excludeId) {
        List<AbsenceStatus> conflictingStatuses = Arrays.asList(
            AbsenceStatus.PENDING, 
            AbsenceStatus.APPROVED, 
            AbsenceStatus.IN_PROGRESS
        );
        
        return absenceRequestRepository.hasOverlappingAbsence(
            request.getEmployee(),
            request.getStartDate(),
            request.getEndDate(),
            conflictingStatuses,
            excludeId
        );
    }

    public boolean isEmployeeOnLeave(Employee employee, LocalDate date) {
        List<AbsenceRequest> currentAbsences = absenceRequestRepository.findCurrentAbsencesForEmployee(
            employee, date, AbsenceStatus.APPROVED);
        return !currentAbsences.isEmpty();
    }
}
