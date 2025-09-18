package com.employeeprofile.backend.repository;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import com.employeeprofile.backend.entity.FeedbackStatus;
import com.employeeprofile.backend.entity.FeedbackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    // Basic queries by employee
    List<Feedback> findByEmployeeAndStatusOrderByCreatedAtDesc(Employee employee, FeedbackStatus status);
    Page<Feedback> findByEmployeeAndStatusOrderByCreatedAtDesc(Employee employee, FeedbackStatus status, Pageable pageable);
    
    // Basic queries by status
List<Feedback> findByStatusOrderByCreatedAtDesc(FeedbackStatus status);
Page<Feedback> findByStatusOrderByCreatedAtDesc(FeedbackStatus status, Pageable pageable);
    
    // Public feedback only (for co-worker role)
    List<Feedback> findByEmployeeAndIsPublicTrueAndStatusOrderByCreatedAtDesc(Employee employee, FeedbackStatus status);
    Page<Feedback> findByEmployeeAndIsPublicTrueAndStatusOrderByCreatedAtDesc(Employee employee, FeedbackStatus status, Pageable pageable);
    
    // Feedback given by a specific employee
    List<Feedback> findByFeedbackGiverAndStatusOrderByCreatedAtDesc(Employee feedbackGiver, FeedbackStatus status);
    Page<Feedback> findByFeedbackGiverAndStatusOrderByCreatedAtDesc(Employee feedbackGiver, FeedbackStatus status, Pageable pageable);
    
    // Feedback by type
    List<Feedback> findByEmployeeAndFeedbackTypeAndStatusOrderByCreatedAtDesc(Employee employee, FeedbackType feedbackType, FeedbackStatus status);
    List<Feedback> findByFeedbackTypeAndStatusOrderByCreatedAtDesc(FeedbackType feedbackType, FeedbackStatus status);
    
    // Search by content (case-insensitive)
    @Query("SELECT f FROM Feedback f WHERE " +
           "LOWER(f.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByContentContainingIgnoreCase(@Param("searchTerm") String searchTerm, 
                                                     @Param("status") FeedbackStatus status);
    
    @Query("SELECT f FROM Feedback f WHERE " +
           "LOWER(f.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "AND f.employee = :employee " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByEmployeeAndContentContainingIgnoreCase(@Param("employee") Employee employee,
                                                               @Param("searchTerm") String searchTerm,
                                                               @Param("status") FeedbackStatus status);
    
    // Search by title
    @Query("SELECT f FROM Feedback f WHERE " +
           "LOWER(f.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByTitleContainingIgnoreCase(@Param("searchTerm") String searchTerm,
                                                   @Param("status") FeedbackStatus status);
    
    // Rating-based queries
    List<Feedback> findByEmployeeAndRatingAndStatusOrderByCreatedAtDesc(Employee employee, Integer rating, FeedbackStatus status);
    List<Feedback> findByEmployeeAndRatingGreaterThanEqualAndStatusOrderByCreatedAtDesc(Employee employee, Integer minRating, FeedbackStatus status);
    List<Feedback> findByEmployeeAndRatingLessThanEqualAndStatusOrderByCreatedAtDesc(Employee employee, Integer maxRating, FeedbackStatus status);
    
    // Category-based queries
    List<Feedback> findByEmployeeAndCategoryIgnoreCaseAndStatusOrderByCreatedAtDesc(Employee employee, String category, FeedbackStatus status);
    List<Feedback> findByCategoryIgnoreCaseAndStatusOrderByCreatedAtDesc(String category, FeedbackStatus status);
    
    // AI-enhanced feedback queries
    List<Feedback> findByEmployeeAndIsAiEnhancedTrueAndStatusOrderByCreatedAtDesc(Employee employee, FeedbackStatus status);
    List<Feedback> findByIsAiEnhancedTrueAndStatusOrderByCreatedAtDesc(FeedbackStatus status);
    
    // Anonymous feedback queries
    List<Feedback> findByEmployeeAndIsAnonymousTrueAndStatusOrderByCreatedAtDesc(Employee employee, FeedbackStatus status);
    List<Feedback> findByIsAnonymousTrueAndStatusOrderByCreatedAtDesc(FeedbackStatus status);
    
    // Date range queries
    List<Feedback> findByEmployeeAndCreatedAtBetweenAndStatusOrderByCreatedAtDesc(Employee employee, 
                                                                                 LocalDateTime startDate, 
                                                                                 LocalDateTime endDate, 
                                                                                 FeedbackStatus status);
    
    @Query("SELECT f FROM Feedback f WHERE " +
           "f.createdAt >= :startDate AND f.createdAt <= :endDate " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate,
                                  @Param("status") FeedbackStatus status);
    
    // Advanced search with multiple criteria
    @Query("SELECT f FROM Feedback f WHERE " +
           "(:employee IS NULL OR f.employee = :employee) " +
           "AND (:feedbackGiver IS NULL OR f.feedbackGiver = :feedbackGiver) " +
           "AND (:feedbackType IS NULL OR f.feedbackType = :feedbackType) " +
           "AND (:category IS NULL OR LOWER(f.category) = LOWER(:category)) " +
           "AND (:minRating IS NULL OR f.rating >= :minRating) " +
           "AND (:maxRating IS NULL OR f.rating <= :maxRating) " +
           "AND (:isPublic IS NULL OR f.isPublic = :isPublic) " +
           "AND (:isAnonymous IS NULL OR f.isAnonymous = :isAnonymous) " +
           "AND (:isAiEnhanced IS NULL OR f.isAiEnhanced = :isAiEnhanced) " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    Page<Feedback> findByMultipleCriteria(
            @Param("employee") Employee employee,
            @Param("feedbackGiver") Employee feedbackGiver,
            @Param("feedbackType") FeedbackType feedbackType,
            @Param("category") String category,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating,
            @Param("isPublic") Boolean isPublic,
            @Param("isAnonymous") Boolean isAnonymous,
            @Param("isAiEnhanced") Boolean isAiEnhanced,
            @Param("status") FeedbackStatus status,
            Pageable pageable);
    
    // Department-wide feedback (for managers)
    @Query("SELECT f FROM Feedback f WHERE " +
           "f.employee.department = :department " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByEmployeeDepartment(@Param("department") String department,
                                           @Param("status") FeedbackStatus status);
    
    @Query("SELECT f FROM Feedback f WHERE " +
           "f.employee.department = :department " +
           "AND f.isPublic = true " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findPublicFeedbackByDepartment(@Param("department") String department,
                                                 @Param("status") FeedbackStatus status);
    
    // Recent feedback queries
    @Query("SELECT f FROM Feedback f WHERE " +
           "f.createdAt >= :sinceDate " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findRecentFeedback(@Param("sinceDate") LocalDateTime sinceDate,
                                     @Param("status") FeedbackStatus status);
    
    @Query("SELECT f FROM Feedback f WHERE " +
           "f.employee = :employee " +
           "AND f.createdAt >= :sinceDate " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findRecentFeedbackForEmployee(@Param("employee") Employee employee,
                                                @Param("sinceDate") LocalDateTime sinceDate,
                                                @Param("status") FeedbackStatus status);
    
    // Statistics queries
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.employee = :employee AND f.rating IS NOT NULL AND f.status = :status")
    Double findAverageRatingForEmployee(@Param("employee") Employee employee, @Param("status") FeedbackStatus status);
    
    @Query("SELECT f.feedbackType, COUNT(f) FROM Feedback f WHERE f.employee = :employee AND f.status = :status GROUP BY f.feedbackType")
    List<Object[]> countFeedbackTypesByEmployee(@Param("employee") Employee employee, @Param("status") FeedbackStatus status);
    
    @Query("SELECT f.category, COUNT(f) FROM Feedback f WHERE f.employee = :employee AND f.status = :status GROUP BY f.category")
    List<Object[]> countFeedbackCategoriesByEmployee(@Param("employee") Employee employee, @Param("status") FeedbackStatus status);
    
    // Count queries
    long countByEmployeeAndStatus(Employee employee, FeedbackStatus status);
    long countByEmployeeAndIsPublicTrueAndStatus(Employee employee, FeedbackStatus status);
    long countByFeedbackGiverAndStatus(Employee feedbackGiver, FeedbackStatus status);
    long countByFeedbackTypeAndStatus(FeedbackType feedbackType, FeedbackStatus status);
    long countByIsAiEnhancedTrueAndStatus(FeedbackStatus status);
    long countByIsAnonymousTrueAndStatus(FeedbackStatus status);
    
    // Tags search (comma-separated tags field)
    @Query("SELECT f FROM Feedback f WHERE " +
           "LOWER(f.tags) LIKE LOWER(CONCAT('%', :tag, '%')) " +
           "AND f.status = :status " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByTagsContaining(@Param("tag") String tag, @Param("status") FeedbackStatus status);
}
