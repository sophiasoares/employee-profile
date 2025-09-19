package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.repository.AbsenceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AbsenceRequestService {

    @Autowired
    private AbsenceRequestRepository absenceRequestRepository;

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
        return absenceRequestRepository.save(absenceRequest);
    }

    public AbsenceRequest updateAbsenceRequest(Long id, AbsenceRequest requestDetails) {
        AbsenceRequest request = absenceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence request not found with id: " + id));

        request.setAbsenceType(requestDetails.getAbsenceType());
        request.setStartDate(requestDetails.getStartDate());
        request.setEndDate(requestDetails.getEndDate());
        request.setReason(requestDetails.getReason());

        return absenceRequestRepository.save(request);
    }

    public void deleteAbsenceRequest(Long id) {
        AbsenceRequest request = absenceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence request not found with id: " + id));
        
        absenceRequestRepository.delete(request);
    }

    public List<AbsenceRequest> getAbsenceRequestsForEmployee(Employee employee) {
        return absenceRequestRepository.findByEmployeeOrderByCreatedAtDesc(employee);
    }
}
