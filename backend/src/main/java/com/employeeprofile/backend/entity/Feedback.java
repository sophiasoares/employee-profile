package com.employeeprofile.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "feedback")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Employee receiving the feedback
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"feedbacks", "absenceRequests"})
    private Employee employee;
    
    // Employee giving the feedback
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feedback_giver_id", nullable = false)
    @JsonIgnoreProperties({"feedbacks", "absenceRequests"})
    private Employee feedbackGiver;
    
    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Content must not exceed 2000 characters")
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type", nullable = false)
    private FeedbackType feedbackType = FeedbackType.POSITIVE;
    
    // AI Enhancement fields
    @Column(name = "is_ai_enhanced")
    private Boolean isAiEnhanced = false;
    
    @Size(max = 3000, message = "AI enhanced content must not exceed 3000 characters")
    @Column(name = "ai_enhanced_content", columnDefinition = "TEXT")
    private String aiEnhancedContent;
    
    public Feedback() {}
    
    public Feedback(Employee employee, String content, FeedbackType feedbackType) {
        this.employee = employee;
        this.content = content;
        this.feedbackType = feedbackType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public Employee getFeedbackGiver() { return feedbackGiver; }
    public void setFeedbackGiver(Employee feedbackGiver) { this.feedbackGiver = feedbackGiver; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public FeedbackType getFeedbackType() { return feedbackType; }
    public void setFeedbackType(FeedbackType feedbackType) { this.feedbackType = feedbackType; }
    
    public String getAiEnhancedContent() { return aiEnhancedContent; }
    public void setAiEnhancedContent(String aiEnhancedContent) { this.aiEnhancedContent = aiEnhancedContent; }
    
    public Boolean getIsAiEnhanced() { return isAiEnhanced; }
    public void setIsAiEnhanced(Boolean isAiEnhanced) { this.isAiEnhanced = isAiEnhanced; }
    
    public String getFeedbackGiverName() {
        if (feedbackGiver == null) {
            return "Anonymous";
        }
        return feedbackGiver.getFullName();
    }
    
    public boolean hasAiEnhancement() {
        return isAiEnhanced && aiEnhancedContent != null && !aiEnhancedContent.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", feedbackType=" + feedbackType +
                '}';
    }
}
