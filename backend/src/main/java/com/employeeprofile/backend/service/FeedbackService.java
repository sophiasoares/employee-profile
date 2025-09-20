package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import com.employeeprofile.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Basic CRUD operations
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
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
}
