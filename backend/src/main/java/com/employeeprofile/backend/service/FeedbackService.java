package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import com.employeeprofile.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openrouter.api.token:}")
    private String openRouterToken;

    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";

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

    public String enhanceFeedbackWithAI(String originalText) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + openRouterToken);
            headers.set("Content-Type", "application/json");
            headers.set("HTTP-Referer", "http://localhost:8080");
            headers.set("X-Title", "Employee Feedback Enhancement");

            Map<String, Object> requestBody = Map.of(
                "model", "mistralai/mistral-7b-instruct:free",  // Free model
                "messages", List.of(
                    Map.of(
                        "role", "user",
                        "content", "Please rewrite this feedback in a more professional and constructive way: \"" + originalText + "\""
                    )
                ),
                "max_tokens", 150,
                "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                OPENROUTER_API_URL,
                HttpMethod.POST,
                entity,
                String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response.getBody());
            
            String enhancedText = jsonResponse.get("choices").get(0).get("message").get("content").asText().trim();

            // Remove unwanted formatting tags
            Pattern pattern = Pattern.compile("<.*?>");
            Matcher matcher = pattern.matcher(enhancedText);
            enhancedText = matcher.replaceAll("");

            return enhancedText;

        } catch (Exception e) {
            throw new RuntimeException("Failed to enhance text with AI: " + e.getMessage());
        }
    }
}
