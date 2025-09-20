package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.repository.AbsenceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AbsenceRequestService {

    @Autowired
    private AbsenceRequestRepository absenceRequestRepository;

    public List<AbsenceRequest> getAllAbsenceRequests() {
        return absenceRequestRepository.findAll();
    }

    public AbsenceRequest createAbsenceRequest(AbsenceRequest absenceRequest) {
        return absenceRequestRepository.save(absenceRequest);
    }

    public List<AbsenceRequest> getAbsenceRequestsForEmployee(Employee employee) {
        return absenceRequestRepository.findByEmployeeOrderByCreatedAtDesc(employee);
    }
}
