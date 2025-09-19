package com.employeeprofile.backend.controller;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import com.employeeprofile.backend.service.EmployeeService;
import com.employeeprofile.backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EmployeeService employeeService;

    // Get all feedback
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        try {
            List<Feedback> feedback = feedbackService.getAllFeedback();
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        try {
            Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
            return feedback.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new feedback
    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback) {
        try {
            Feedback createdFeedback = feedbackService.createFeedback(feedback);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update feedback
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, 
                                                  @Valid @RequestBody Feedback feedback) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedback(id, feedback);
            return ResponseEntity.ok(updatedFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get feedback for an employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Feedback>> getAllFeedbackForEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<Feedback> feedback = feedbackService.getAllFeedbackForEmployee(employee.get());
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get feedback given by an employee
    @GetMapping("/given-by/{employeeId}")
    public ResponseEntity<List<Feedback>> getFeedbackGivenBy(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<Feedback> feedback = feedbackService.getFeedbackGivenBy(employee.get());
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // AI Enhancement
    @PostMapping("/{id}/ai-enhance")
    public ResponseEntity<Void> enhanceFeedbackWithAI(@PathVariable Long id, 
                                                      @RequestBody String aiEnhancedContent) {
        try {
            feedbackService.enhanceFeedbackWithAI(id, aiEnhancedContent);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Statistics
    @GetMapping("/stats/employee/{employeeId}/count")
    public ResponseEntity<Long> getFeedbackCountForEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            long count = feedbackService.getFeedbackCountForEmployee(employee.get());
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
