package com.employeeprofile.backend.repository;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.AbsenceStatus;
import com.employeeprofile.backend.entity.AbsenceType;
import com.employeeprofile.backend.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
    
    // Basic queries by employee
    List<AbsenceRequest> findByEmployeeOrderByRequestedAtDesc(Employee employee);
    Page<AbsenceRequest> findByEmployeeOrderByRequestedAtDesc(Employee employee, Pageable pageable);
    
    List<AbsenceRequest> findByEmployeeAndStatusOrderByRequestedAtDesc(Employee employee, AbsenceStatus status);
    Page<AbsenceRequest> findByEmployeeAndStatusOrderByRequestedAtDesc(Employee employee, AbsenceStatus status, Pageable pageable);
    
    // Queries by status
    List<AbsenceRequest> findByStatusOrderByRequestedAtDesc(AbsenceStatus status);
    Page<AbsenceRequest> findByStatusOrderByRequestedAtDesc(AbsenceStatus status, Pageable pageable);
    
    // Pending requests (for manager approval)
    List<AbsenceRequest> findByStatusOrderByRequestedAtAsc(AbsenceStatus status);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.employee.manager = :manager AND ar.status = :status " +
           "ORDER BY ar.requestedAt ASC")
    List<AbsenceRequest> findPendingRequestsForManager(@Param("manager") Employee manager, 
                                                      @Param("status") AbsenceStatus status);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.employee.department = :department AND ar.status = :status " +
           "ORDER BY ar.requestedAt ASC")
    List<AbsenceRequest> findPendingRequestsByDepartment(@Param("department") String department,
                                                        @Param("status") AbsenceStatus status);
    
    // Queries by absence type
    List<AbsenceRequest> findByEmployeeAndAbsenceTypeOrderByStartDateDesc(Employee employee, AbsenceType absenceType);
    List<AbsenceRequest> findByAbsenceTypeAndStatusOrderByStartDateDesc(AbsenceType absenceType, AbsenceStatus status);
    
    // Date range queries for absence periods
    List<AbsenceRequest> findByEmployeeAndStartDateBetweenOrderByStartDateDesc(Employee employee, 
                                                                              LocalDate startDate, 
                                                                              LocalDate endDate);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.startDate <= :endDate AND ar.endDate >= :startDate " +
           "AND ar.status = :status " +
           "ORDER BY ar.startDate")
    List<AbsenceRequest> findOverlappingAbsences(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 @Param("status") AbsenceStatus status);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.employee = :employee " +
           "AND ar.startDate <= :endDate AND ar.endDate >= :startDate " +
           "AND ar.status IN (:statuses) " +
           "ORDER BY ar.startDate")
    List<AbsenceRequest> findEmployeeOverlappingAbsences(@Param("employee") Employee employee,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("statuses") List<AbsenceStatus> statuses);
    
    // Current and upcoming absences
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.startDate <= :currentDate AND ar.endDate >= :currentDate " +
           "AND ar.status = :status")
    List<AbsenceRequest> findCurrentAbsences(@Param("currentDate") LocalDate currentDate,
                                           @Param("status") AbsenceStatus status);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.employee = :employee " +
           "AND ar.startDate <= :currentDate AND ar.endDate >= :currentDate " +
           "AND ar.status = :status")
    List<AbsenceRequest> findCurrentAbsencesForEmployee(@Param("employee") Employee employee,
                                                       @Param("currentDate") LocalDate currentDate,
                                                       @Param("status") AbsenceStatus status);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.startDate > :currentDate " +
           "AND ar.status = :status " +
           "ORDER BY ar.startDate ASC")
    List<AbsenceRequest> findUpcomingAbsences(@Param("currentDate") LocalDate currentDate,
                                            @Param("status") AbsenceStatus status);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.employee = :employee " +
           "AND ar.startDate > :currentDate " +
           "AND ar.status = :status " +
           "ORDER BY ar.startDate ASC")
    List<AbsenceRequest> findUpcomingAbsencesForEmployee(@Param("employee") Employee employee,
                                                        @Param("currentDate") LocalDate currentDate,
                                                        @Param("status") AbsenceStatus status);
    
    // Approved by queries (for tracking manager decisions)
    List<AbsenceRequest> findByApprovedByOrderByApprovedAtDesc(Employee approvedBy);
    List<AbsenceRequest> findByApprovedByAndStatusOrderByApprovedAtDesc(Employee approvedBy, AbsenceStatus status);
    
    // Department-wide queries (for managers)
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.employee.department = :department " +
           "ORDER BY ar.requestedAt DESC")
    List<AbsenceRequest> findByEmployeeDepartment(@Param("department") String department);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.employee.department = :department " +
           "AND ar.status = :status " +
           "ORDER BY ar.requestedAt DESC")
    List<AbsenceRequest> findByEmployeeDepartmentAndStatus(@Param("department") String department,
                                                          @Param("status") AbsenceStatus status);
    
    // Advanced search with multiple criteria
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "(:employee IS NULL OR ar.employee = :employee) " +
           "AND (:absenceType IS NULL OR ar.absenceType = :absenceType) " +
           "AND (:status IS NULL OR ar.status = :status) " +
           "AND (:startDate IS NULL OR ar.startDate >= :startDate) " +
           "AND (:endDate IS NULL OR ar.endDate <= :endDate) " +
           "AND (:approvedBy IS NULL OR ar.approvedBy = :approvedBy) " +
           "ORDER BY ar.requestedAt DESC")
    Page<AbsenceRequest> findByMultipleCriteria(
            @Param("employee") Employee employee,
            @Param("absenceType") AbsenceType absenceType,
            @Param("status") AbsenceStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("approvedBy") Employee approvedBy,
            Pageable pageable);
    
    // Half-day requests
    List<AbsenceRequest> findByEmployeeAndIsHalfDayTrueOrderByStartDateDesc(Employee employee);
    List<AbsenceRequest> findByIsHalfDayTrueAndStatusOrderByStartDateDesc(AbsenceStatus status);
    
    // Recent requests (for notifications)
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.requestedAt >= :sinceDate " +
           "ORDER BY ar.requestedAt DESC")
    List<AbsenceRequest> findRecentRequests(@Param("sinceDate") LocalDateTime sinceDate);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.requestedAt >= :sinceDate " +
           "AND ar.status = :status " +
           "ORDER BY ar.requestedAt DESC")
    List<AbsenceRequest> findRecentRequestsByStatus(@Param("sinceDate") LocalDateTime sinceDate,
                                                   @Param("status") AbsenceStatus status);
    
    // Work delegation queries
    List<AbsenceRequest> findByDelegatedToOrderByStartDateDesc(Employee delegatedTo);
    
    @Query("SELECT ar FROM AbsenceRequest ar WHERE " +
           "ar.delegatedTo = :delegatedTo " +
           "AND ar.startDate <= :currentDate AND ar.endDate >= :currentDate " +
           "AND ar.status = :status")
    List<AbsenceRequest> findCurrentDelegatedWork(@Param("delegatedTo") Employee delegatedTo,
                                                 @Param("currentDate") LocalDate currentDate,
                                                 @Param("status") AbsenceStatus status);
    
    // Simple statistics queries
    @Query("SELECT ar.absenceType, COUNT(ar) FROM AbsenceRequest ar WHERE ar.employee = :employee GROUP BY ar.absenceType")
    List<Object[]> countAbsenceTypesByEmployee(@Param("employee") Employee employee);
    
    @Query("SELECT ar.status, COUNT(ar) FROM AbsenceRequest ar WHERE ar.employee = :employee GROUP BY ar.status")
    List<Object[]> countAbsenceStatusByEmployee(@Param("employee") Employee employee);
    
    // Count queries
    long countByEmployeeAndStatus(Employee employee, AbsenceStatus status);
    long countByStatusAndRequestedAtAfter(AbsenceStatus status, LocalDateTime since);
    long countByAbsenceTypeAndStatus(AbsenceType absenceType, AbsenceStatus status);
    long countByApprovedByAndStatus(Employee approvedBy, AbsenceStatus status);
    
    // Validation queries
    @Query("SELECT COUNT(ar) > 0 FROM AbsenceRequest ar WHERE " +
           "ar.employee = :employee " +
           "AND ar.startDate <= :endDate AND ar.endDate >= :startDate " +
           "AND ar.status IN (:statuses) " +
           "AND (:excludeId IS NULL OR ar.id != :excludeId)")
    boolean hasOverlappingAbsence(@Param("employee") Employee employee,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate,
                                 @Param("statuses") List<AbsenceStatus> statuses,
                                 @Param("excludeId") Long excludeId);
}
