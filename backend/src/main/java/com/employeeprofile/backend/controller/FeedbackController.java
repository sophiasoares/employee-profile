package com.employeeprofile.backend.controller;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.entity.Feedback;
import com.employeeprofile.backend.entity.FeedbackType;
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

    // Get all active feedback
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        try {
            List<Feedback> feedback = feedbackService.getAllActiveFeedback();
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

    // Delete feedback (archive)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all feedback for an employee (Manager/Owner role)
    @GetMapping("/employee/{employeeId}/all")
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

    // Get public feedback for an employee (Co-worker role)
    @GetMapping("/employee/{employeeId}/public")
    public ResponseEntity<List<Feedback>> getPublicFeedbackForEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<Feedback> feedback = feedbackService.getPublicFeedbackForEmployee(employee.get());
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

    // Search feedback by content
    @GetMapping("/search/content")
    public ResponseEntity<List<Feedback>> searchByContent(@RequestParam String searchTerm) {
        try {
            List<Feedback> feedback = feedbackService.searchByContent(searchTerm);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search feedback by title
    @GetMapping("/search/title")
    public ResponseEntity<List<Feedback>> searchByTitle(@RequestParam String searchTerm) {
        try {
            List<Feedback> feedback = feedbackService.searchByTitle(searchTerm);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get feedback by type
    @GetMapping("/type/{feedbackType}")
    public ResponseEntity<List<Feedback>> getFeedbackByType(@PathVariable FeedbackType feedbackType) {
        try {
            List<Feedback> feedback = feedbackService.getFeedbackByType(feedbackType);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get feedback by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Feedback>> getFeedbackByCategory(@PathVariable String category) {
        try {
            List<Feedback> feedback = feedbackService.getFeedbackByCategory(category);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get department feedback (for managers)
    @GetMapping("/department/{department}/all")
    public ResponseEntity<List<Feedback>> getFeedbackByDepartment(@PathVariable String department) {
        try {
            List<Feedback> feedback = feedbackService.getFeedbackByDepartment(department);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get public department feedback
    @GetMapping("/department/{department}/public")
    public ResponseEntity<List<Feedback>> getPublicFeedbackByDepartment(@PathVariable String department) {
        try {
            List<Feedback> feedback = feedbackService.getPublicFeedbackByDepartment(department);
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
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get AI enhanced feedback
    @GetMapping("/ai-enhanced")
    public ResponseEntity<List<Feedback>> getAiEnhancedFeedback() {
        try {
            List<Feedback> feedback = feedbackService.getAiEnhancedFeedback();
            return ResponseEntity.ok(feedback);
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

    @GetMapping("/stats/employee/{employeeId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Double averageRating = feedbackService.getAverageRatingForEmployee(employee.get());
            return ResponseEntity.ok(averageRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
