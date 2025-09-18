package com.employeeprofile.backend.controller;

import com.employeeprofile.backend.entity.AbsenceRequest;
import com.employeeprofile.backend.entity.AbsenceStatus;
import com.employeeprofile.backend.entity.AbsenceType;
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

    // Delete absence request (cancel)
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

    // Get pending requests for an employee
    @GetMapping("/employee/{employeeId}/pending")
    public ResponseEntity<List<AbsenceRequest>> getPendingRequestsForEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<AbsenceRequest> requests = absenceRequestService.getPendingRequestsForEmployee(employee.get());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get pending requests for a manager
    @GetMapping("/manager/{managerId}/pending")
    public ResponseEntity<List<AbsenceRequest>> getPendingRequestsForManager(@PathVariable Long managerId) {
        try {
            Optional<Employee> manager = employeeService.getEmployeeById(managerId);
            if (manager.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<AbsenceRequest> requests = absenceRequestService.getPendingRequestsForManager(manager.get());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get pending requests by department
    @GetMapping("/department/{department}/pending")
    public ResponseEntity<List<AbsenceRequest>> getPendingRequestsByDepartment(@PathVariable String department) {
        try {
            List<AbsenceRequest> requests = absenceRequestService.getPendingRequestsByDepartment(department);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Approve absence request
    @PostMapping("/{id}/approve")
    public ResponseEntity<AbsenceRequest> approveRequest(@PathVariable Long id, 
                                                        @RequestParam Long approverId,
                                                        @RequestParam(required = false) String comments) {
        try {
            Optional<Employee> approver = employeeService.getEmployeeById(approverId);
            if (approver.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            AbsenceRequest approvedRequest = absenceRequestService.approveRequest(id, approver.get(), comments);
            return ResponseEntity.ok(approvedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Reject absence request
    @PostMapping("/{id}/reject")
    public ResponseEntity<AbsenceRequest> rejectRequest(@PathVariable Long id, 
                                                       @RequestParam Long approverId,
                                                       @RequestParam(required = false) String comments) {
        try {
            Optional<Employee> approver = employeeService.getEmployeeById(approverId);
            if (approver.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            AbsenceRequest rejectedRequest = absenceRequestService.rejectRequest(id, approver.get(), comments);
            return ResponseEntity.ok(rejectedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get current absences
    @GetMapping("/current")
    public ResponseEntity<List<AbsenceRequest>> getCurrentAbsences() {
        try {
            List<AbsenceRequest> requests = absenceRequestService.getCurrentAbsences();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get upcoming absences
    @GetMapping("/upcoming")
    public ResponseEntity<List<AbsenceRequest>> getUpcomingAbsences() {
        try {
            List<AbsenceRequest> requests = absenceRequestService.getUpcomingAbsences();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get current absences for an employee
    @GetMapping("/employee/{employeeId}/current")
    public ResponseEntity<List<AbsenceRequest>> getCurrentAbsencesForEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<AbsenceRequest> requests = absenceRequestService.getCurrentAbsencesForEmployee(employee.get());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get requests by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AbsenceRequest>> getRequestsByStatus(@PathVariable AbsenceStatus status) {
        try {
            List<AbsenceRequest> requests = absenceRequestService.getRequestsByStatus(status);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get requests by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AbsenceRequest>> getRequestsByType(@PathVariable AbsenceType type) {
        try {
            List<AbsenceRequest> requests = absenceRequestService.getRequestsByType(type);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get requests by department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<AbsenceRequest>> getRequestsByDepartment(@PathVariable String department) {
        try {
            List<AbsenceRequest> requests = absenceRequestService.getRequestsByDepartment(department);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Statistics
    @GetMapping("/stats/employee/{employeeId}/count")
    public ResponseEntity<Long> getRequestCountForEmployee(@PathVariable Long employeeId, 
                                                          @RequestParam AbsenceStatus status) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            long count = absenceRequestService.getRequestCountForEmployee(employee.get(), status);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/pending/count")
    public ResponseEntity<Long> getPendingRequestCount() {
        try {
            long count = absenceRequestService.getPendingRequestCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Check if employee is on leave
    @GetMapping("/employee/{employeeId}/on-leave")
    public ResponseEntity<Boolean> isEmployeeOnLeave(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            boolean onLeave = absenceRequestService.isEmployeeOnLeave(employee.get(), 
                    java.time.LocalDate.now());
            return ResponseEntity.ok(onLeave);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
