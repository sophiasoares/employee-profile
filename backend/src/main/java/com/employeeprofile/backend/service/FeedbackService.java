package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import com.employeeprofile.backend.entity.FeedbackStatus;
import com.employeeprofile.backend.entity.FeedbackType;
import com.employeeprofile.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Basic CRUD operations
    public List<Feedback> getAllActiveFeedback() {
        return feedbackRepository.findByStatusOrderByCreatedAtDesc(FeedbackStatus.ACTIVE);
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback createFeedback(Feedback feedback) {
        feedback.setStatus(FeedbackStatus.ACTIVE);
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));

        feedback.setTitle(feedbackDetails.getTitle());
        feedback.setContent(feedbackDetails.getContent());
        feedback.setFeedbackType(feedbackDetails.getFeedbackType());
        feedback.setRating(feedbackDetails.getRating());
        feedback.setCategory(feedbackDetails.getCategory());
        feedback.setTags(feedbackDetails.getTags());
        feedback.setIsPublic(feedbackDetails.getIsPublic());
        feedback.setIsAnonymous(feedbackDetails.getIsAnonymous());

        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        feedback.setStatus(FeedbackStatus.ARCHIVED);
        feedbackRepository.save(feedback);
    }

    // Role-based access methods
    
    // For Manager/Owner role - can see all feedback
    public List<Feedback> getAllFeedbackForEmployee(Employee employee) {
        return feedbackRepository.findByEmployeeAndStatusOrderByCreatedAtDesc(employee, FeedbackStatus.ACTIVE);
    }

    public Page<Feedback> getAllFeedbackForEmployee(Employee employee, Pageable pageable) {
        return feedbackRepository.findByEmployeeAndStatusOrderByCreatedAtDesc(employee, FeedbackStatus.ACTIVE, pageable);
    }

    // For Co-worker role - can only see public feedback
    public List<Feedback> getPublicFeedbackForEmployee(Employee employee) {
        return feedbackRepository.findByEmployeeAndIsPublicTrueAndStatusOrderByCreatedAtDesc(employee, FeedbackStatus.ACTIVE);
    }

    public Page<Feedback> getPublicFeedbackForEmployee(Employee employee, Pageable pageable) {
        return feedbackRepository.findByEmployeeAndIsPublicTrueAndStatusOrderByCreatedAtDesc(employee, FeedbackStatus.ACTIVE, pageable);
    }

    // Feedback given by a user
    public List<Feedback> getFeedbackGivenBy(Employee feedbackGiver) {
        return feedbackRepository.findByFeedbackGiverAndStatusOrderByCreatedAtDesc(feedbackGiver, FeedbackStatus.ACTIVE);
    }

    // Search operations
    public List<Feedback> searchByContent(String searchTerm) {
        return feedbackRepository.findByContentContainingIgnoreCase(searchTerm, FeedbackStatus.ACTIVE);
    }

    public List<Feedback> searchByTitle(String searchTerm) {
        return feedbackRepository.findByTitleContainingIgnoreCase(searchTerm, FeedbackStatus.ACTIVE);
    }

    public List<Feedback> getFeedbackByType(FeedbackType feedbackType) {
        return feedbackRepository.findByFeedbackTypeAndStatusOrderByCreatedAtDesc(feedbackType, FeedbackStatus.ACTIVE);
    }

    public List<Feedback> getFeedbackByCategory(String category) {
        return feedbackRepository.findByCategoryIgnoreCaseAndStatusOrderByCreatedAtDesc(category, FeedbackStatus.ACTIVE);
    }

    // Department-wide feedback (for managers)
    public List<Feedback> getFeedbackByDepartment(String department) {
        return feedbackRepository.findByEmployeeDepartment(department, FeedbackStatus.ACTIVE);
    }

    public List<Feedback> getPublicFeedbackByDepartment(String department) {
        return feedbackRepository.findPublicFeedbackByDepartment(department, FeedbackStatus.ACTIVE);
    }

    // AI Enhancement methods
    public void enhanceFeedbackWithAI(Long feedbackId, String aiEnhancedContent) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + feedbackId));
        
        feedback.setAiEnhancedContent(aiEnhancedContent);
        feedback.setIsAiEnhanced(true);
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getAiEnhancedFeedback() {
        return feedbackRepository.findByIsAiEnhancedTrueAndStatusOrderByCreatedAtDesc(FeedbackStatus.ACTIVE);
    }

    // Statistics
    public long getFeedbackCountForEmployee(Employee employee) {
        return feedbackRepository.countByEmployeeAndStatus(employee, FeedbackStatus.ACTIVE);
    }

    public long getPublicFeedbackCountForEmployee(Employee employee) {
        return feedbackRepository.countByEmployeeAndIsPublicTrueAndStatus(employee, FeedbackStatus.ACTIVE);
    }

    public Double getAverageRatingForEmployee(Employee employee) {
        return feedbackRepository.findAverageRatingForEmployee(employee, FeedbackStatus.ACTIVE);
    }

    public long getFeedbackCountByType(FeedbackType feedbackType) {
        return feedbackRepository.countByFeedbackTypeAndStatus(feedbackType, FeedbackStatus.ACTIVE);
    }
}
