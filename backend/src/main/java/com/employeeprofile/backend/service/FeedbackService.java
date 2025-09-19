package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import com.employeeprofile.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));

        feedback.setContent(feedbackDetails.getContent());
        feedback.setFeedbackType(feedbackDetails.getFeedbackType());

        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        feedbackRepository.delete(feedback);
    }

    // Employee-specific operations
    public List<Feedback> getAllFeedbackForEmployee(Employee employee) {
        return feedbackRepository.findByEmployeeOrderByIdDesc(employee);
    }

    public List<Feedback> getFeedbackGivenBy(Employee feedbackGiver) {
        return feedbackRepository.findByFeedbackGiverOrderByIdDesc(feedbackGiver);
    }

    // AI Enhancement
    public void enhanceFeedbackWithAI(Long feedbackId, String aiEnhancedContent) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + feedbackId));
        
        feedback.setAiEnhancedContent(aiEnhancedContent);
        feedback.setIsAiEnhanced(true);
        feedbackRepository.save(feedback);
    }

    // Statistics
    public long getFeedbackCountForEmployee(Employee employee) {
        return feedbackRepository.countByEmployee(employee);
    }

    public long getFeedbackCountByGiver(Employee feedbackGiver) {
        return feedbackRepository.countByFeedbackGiver(feedbackGiver);
    }
}
