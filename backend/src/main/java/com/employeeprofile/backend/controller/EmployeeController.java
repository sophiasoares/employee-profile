package com.employeeprofile.backend.controller;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Get all active employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            List<Employee> employees = employeeService.getAllActiveEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employees with pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Employee>> getAllEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Employee> employees = employeeService.getAllActiveEmployees(pageable);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            return employee.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeByEmail(email);
            return employee.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, 
                                                  @Valid @RequestBody Employee employee) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete employee (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search employees by name
    @GetMapping("/search/name")
    public ResponseEntity<List<Employee>> searchByName(@RequestParam String name) {
        try {
            List<Employee> employees = employeeService.searchByName(name);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search employees by department
    @GetMapping("/search/department")
    public ResponseEntity<List<Employee>> searchByDepartment(@RequestParam String department) {
        try {
            List<Employee> employees = employeeService.searchByDepartment(department);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search employees by position
    @GetMapping("/search/position")
    public ResponseEntity<List<Employee>> searchByPosition(@RequestParam String position) {
        try {
            List<Employee> employees = employeeService.searchByPosition(position);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get direct reports for a manager
    @GetMapping("/{managerId}/direct-reports")
    public ResponseEntity<List<Employee>> getDirectReports(@PathVariable Long managerId) {
        try {
            List<Employee> directReports = employeeService.getDirectReports(managerId);
            return ResponseEntity.ok(directReports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get top-level managers
    @GetMapping("/managers")
    public ResponseEntity<List<Employee>> getTopLevelManagers() {
        try {
            List<Employee> managers = employeeService.getTopLevelManagers();
            return ResponseEntity.ok(managers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get statistics
    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalEmployeeCount() {
        try {
            long count = employeeService.getTotalActiveEmployees();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/department/{department}")
    public ResponseEntity<Long> getEmployeeCountByDepartment(@PathVariable String department) {
        try {
            long count = employeeService.getEmployeeCountByDepartment(department);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Validation endpoints
    @GetMapping("/validate/email")
    public ResponseEntity<Boolean> isEmailAvailable(@RequestParam String email) {
        try {
            boolean available = employeeService.isEmailAvailable(email);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/validate/employee-id")
    public ResponseEntity<Boolean> isEmployeeIdAvailable(@RequestParam String employeeId) {
        try {
            boolean available = employeeService.isEmployeeIdAvailable(employeeId);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
