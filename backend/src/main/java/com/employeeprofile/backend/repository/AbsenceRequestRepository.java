package com.employeeprofile.backend.repository;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
    
    // Basic queries by employee
    List<AbsenceRequest> findByEmployeeOrderByCreatedAtDesc(Employee employee);
    Page<AbsenceRequest> findByEmployeeOrderByCreatedAtDesc(Employee employee, Pageable pageable);
}
