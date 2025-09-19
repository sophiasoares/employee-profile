package com.employeeprofile.backend.repository;

import com.employeeprofile.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmployeeId(String employeeId);
    
    // Search by name (using firstName and lastName)
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByNameContaining(@Param("name") String name);
    
    // Count queries
    long count();
}
