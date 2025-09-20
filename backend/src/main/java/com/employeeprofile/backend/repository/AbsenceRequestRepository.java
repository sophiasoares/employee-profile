package com.employeeprofile.backend.repository;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
    
    List<AbsenceRequest> findByEmployeeOrderByCreatedAtDesc(Employee employee);
}
