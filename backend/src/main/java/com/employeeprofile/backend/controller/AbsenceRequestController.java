package com.employeeprofile.backend.controller;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.service.AbsenceRequestService;
import com.employeeprofile.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/absence-requests")
@CrossOrigin(origins = "http://localhost:4200")
public class AbsenceRequestController {

    @Autowired
    private AbsenceRequestService absenceRequestService;

    @Autowired
    private EmployeeService employeeService;

    // Get all absence requests
    @GetMapping
    public ResponseEntity<List<AbsenceRequest>> getAllAbsenceRequests() {
        try {
            List<AbsenceRequest> requests = absenceRequestService.getAllAbsenceRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get absence request by ID
    @GetMapping("/{id}")
    public ResponseEntity<AbsenceRequest> getAbsenceRequestById(@PathVariable Long id) {
        try {
            Optional<AbsenceRequest> request = absenceRequestService.getAbsenceRequestById(id);
            return request.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new absence request
    @PostMapping
    public ResponseEntity<AbsenceRequest> createAbsenceRequest(@Valid @RequestBody AbsenceRequest request) {
        try {
            AbsenceRequest createdRequest = absenceRequestService.createAbsenceRequest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update absence request
    @PutMapping("/{id}")
    public ResponseEntity<AbsenceRequest> updateAbsenceRequest(@PathVariable Long id, 
                                                              @Valid @RequestBody AbsenceRequest request) {
        try {
            AbsenceRequest updatedRequest = absenceRequestService.updateAbsenceRequest(id, request);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete absence request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAbsenceRequest(@PathVariable Long id) {
        try {
            absenceRequestService.deleteAbsenceRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get absence requests for an employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AbsenceRequest>> getAbsenceRequestsForEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<AbsenceRequest> requests = absenceRequestService.getAbsenceRequestsForEmployee(employee.get());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
