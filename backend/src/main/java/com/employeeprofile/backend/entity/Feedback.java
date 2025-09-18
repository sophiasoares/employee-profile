package com.employeeprofile.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Employee receiving the feedback
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    // Employee giving the feedback (can be null for anonymous feedback)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_giver_id")
    private Employee feedbackGiver;
    
    @NotBlank(message = "Feedback title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @NotBlank(message = "Feedback content is required")
    @Size(max = 2000, message = "Content must not exceed 2000 characters")
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type")
    private FeedbackType feedbackType;
    
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;
    
    // AI Enhanced content
    @Column(name = "ai_enhanced_content", columnDefinition = "TEXT")
    private String aiEnhancedContent;
    
    @Column(name = "is_ai_enhanced")
    private Boolean isAiEnhanced = false;
    
    // Visibility and Status
    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;
    
    @Column(name = "is_public")
    private Boolean isPublic = true;
    
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status = FeedbackStatus.ACTIVE;
    
    // Categories for better organization
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;
    
    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags; // Comma-separated tags
    
    // System fields
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Feedback() {}
    
    public Feedback(Employee employee, String title, String content, FeedbackType feedbackType) {
        this.employee = employee;
        this.title = title;
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
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public FeedbackType getFeedbackType() { return feedbackType; }
    public void setFeedbackType(FeedbackType feedbackType) { this.feedbackType = feedbackType; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getAiEnhancedContent() { return aiEnhancedContent; }
    public void setAiEnhancedContent(String aiEnhancedContent) { this.aiEnhancedContent = aiEnhancedContent; }
    
    public Boolean getIsAiEnhanced() { return isAiEnhanced; }
    public void setIsAiEnhanced(Boolean isAiEnhanced) { this.isAiEnhanced = isAiEnhanced; }
    
    public Boolean getIsAnonymous() { return isAnonymous; }
    public void setIsAnonymous(Boolean isAnonymous) { this.isAnonymous = isAnonymous; }
    
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    
    public FeedbackStatus getStatus() { return status; }
    public void setStatus(FeedbackStatus status) { this.status = status; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Utility Methods
    public String getFeedbackGiverName() {
        if (isAnonymous || feedbackGiver == null) {
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
                ", title='" + title + '\'' +
                ", feedbackType=" + feedbackType +
                ", rating=" + rating +
                ", isAnonymous=" + isAnonymous +
                ", createdAt=" + createdAt +
                '}';
    }
}
