package com.employeeprofile.backend.repository;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    List<Feedback> findByEmployeeOrderByIdDesc(Employee employee);
    List<Feedback> findByFeedbackGiverOrderByIdDesc(Employee feedbackGiver);
    long countByEmployee(Employee employee);
    long countByFeedbackGiver(Employee feedbackGiver);
}
