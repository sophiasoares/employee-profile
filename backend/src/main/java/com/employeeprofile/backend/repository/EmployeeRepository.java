package com.employeeprofile.backend.repository;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.EmploymentStatus;
import com.employeeprofile.backend.entity.EmploymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Basic lookups
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmployeeId(String employeeId);
    
    // Active employees only
    List<Employee> findByIsActiveTrue();
    Page<Employee> findByIsActiveTrue(Pageable pageable);
    
    // Search by name (case-insensitive, partial match)
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND e.isActive = true")
    List<Employee> findByNameContainingIgnoreCase(@Param("name") String name);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND e.isActive = true")
    Page<Employee> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    
    // Search by department
    List<Employee> findByDepartmentIgnoreCaseAndIsActiveTrue(String department);
    Page<Employee> findByDepartmentIgnoreCaseAndIsActiveTrue(String department, Pageable pageable);
    
    // Search by position
    List<Employee> findByPositionIgnoreCaseAndIsActiveTrue(String position);
    Page<Employee> findByPositionIgnoreCaseAndIsActiveTrue(String position, Pageable pageable);
    
    // Manager relationships
    List<Employee> findByManagerAndIsActiveTrue(Employee manager);
    List<Employee> findByManagerIdAndIsActiveTrue(Long managerId);
    
    @Query("SELECT e FROM Employee e WHERE e.manager IS NULL AND e.isActive = true")
    List<Employee> findTopLevelManagers();
    
    // Employment status and type filtering
    List<Employee> findByEmploymentStatusAndIsActiveTrue(EmploymentStatus status);
    List<Employee> findByEmploymentTypeAndIsActiveTrue(EmploymentType type);
    
    // Advanced search with multiple criteria
    @Query("SELECT e FROM Employee e WHERE " +
           "(:name IS NULL OR LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:department IS NULL OR LOWER(e.department) = LOWER(:department)) " +
           "AND (:position IS NULL OR LOWER(e.position) = LOWER(:position)) " +
           "AND (:employmentStatus IS NULL OR e.employmentStatus = :employmentStatus) " +
           "AND (:employmentType IS NULL OR e.employmentType = :employmentType) " +
           "AND e.isActive = true " +
           "ORDER BY e.lastName, e.firstName")
    Page<Employee> findByMultipleCriteria(
            @Param("name") String name,
            @Param("department") String department,
            @Param("position") String position,
            @Param("employmentStatus") EmploymentStatus employmentStatus,
            @Param("employmentType") EmploymentType employmentType,
            Pageable pageable);
    
    // Hire date range queries
    List<Employee> findByHireDateBetweenAndIsActiveTrue(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "e.hireDate >= :startDate AND e.hireDate <= :endDate " +
           "AND e.isActive = true " +
           "ORDER BY e.hireDate DESC")
    List<Employee> findByHireDateRange(@Param("startDate") LocalDate startDate, 
                                      @Param("endDate") LocalDate endDate);
    
    // Department statistics
    @Query("SELECT e.department, COUNT(e) FROM Employee e WHERE e.isActive = true GROUP BY e.department")
    List<Object[]> countEmployeesByDepartment();
    
    @Query("SELECT e.position, COUNT(e) FROM Employee e WHERE e.isActive = true GROUP BY e.position")
    List<Object[]> countEmployeesByPosition();
    
    // Birthday queries (for HR notifications)
    @Query("SELECT e FROM Employee e WHERE " +
           "MONTH(e.birthDate) = MONTH(:date) AND DAY(e.birthDate) = DAY(:date) " +
           "AND e.isActive = true")
    List<Employee> findByBirthday(@Param("date") LocalDate date);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "MONTH(e.birthDate) = :month AND e.isActive = true " +
           "ORDER BY DAY(e.birthDate)")
    List<Employee> findByBirthMonth(@Param("month") int month);
    
    // Work anniversary queries
    @Query("SELECT e FROM Employee e WHERE " +
           "MONTH(e.hireDate) = MONTH(:date) AND DAY(e.hireDate) = DAY(:date) " +
           "AND e.isActive = true")
    List<Employee> findByWorkAnniversary(@Param("date") LocalDate date);
    
    // Skills search (for finding employees with specific skills)
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.skills) LIKE LOWER(CONCAT('%', :skill, '%')) " +
           "AND e.isActive = true")
    List<Employee> findBySkillsContaining(@Param("skill") String skill);
    
    // Manager hierarchy queries
    @Query("SELECT e FROM Employee e WHERE e.manager.id = :managerId AND e.isActive = true")
    List<Employee> findDirectReports(@Param("managerId") Long managerId);
    
    @Query(value = "WITH RECURSIVE employee_hierarchy AS (" +
                   "SELECT id, first_name, last_name, manager_id, 0 as level " +
                   "FROM employees WHERE id = :employeeId " +
                   "UNION ALL " +
                   "SELECT e.id, e.first_name, e.last_name, e.manager_id, eh.level + 1 " +
                   "FROM employees e " +
                   "INNER JOIN employee_hierarchy eh ON e.manager_id = eh.id " +
                   "WHERE eh.level < 10) " +
                   "SELECT * FROM employee_hierarchy", nativeQuery = true)
    List<Object[]> findEmployeeHierarchy(@Param("employeeId") Long employeeId);
    
    // Recent hires (for onboarding tracking)
    @Query("SELECT e FROM Employee e WHERE " +
           "e.hireDate >= :sinceDate AND e.isActive = true " +
           "ORDER BY e.hireDate DESC")
    List<Employee> findRecentHires(@Param("sinceDate") LocalDate sinceDate);
    
    // Employees without managers (potential data quality issues)
    @Query("SELECT e FROM Employee e WHERE e.manager IS NULL AND e.isActive = true")
    List<Employee> findEmployeesWithoutManager();
    
    // Count queries for dashboard statistics
    long countByIsActiveTrue();
    long countByDepartmentAndIsActiveTrue(String department);
    long countByEmploymentStatusAndIsActiveTrue(EmploymentStatus status);
    long countByEmploymentTypeAndIsActiveTrue(EmploymentType type);
    
    // Exists queries for validation
    boolean existsByEmail(String email);
    boolean existsByEmployeeId(String employeeId);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByEmployeeIdAndIdNot(String employeeId, Long id);
}
