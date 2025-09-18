package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Basic CRUD operations
    public List<Employee> getAllActiveEmployees() {
        return employeeRepository.findByIsActiveTrue();
    }

    public Page<Employee> getAllActiveEmployees(Pageable pageable) {
        return employeeRepository.findByIsActiveTrue(pageable);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee createEmployee(Employee employee) {
        // Validate unique constraints
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Employee with email " + employee.getEmail() + " already exists");
        }
        if (employeeRepository.existsByEmployeeId(employee.getEmployeeId())) {
            throw new RuntimeException("Employee with ID " + employee.getEmployeeId() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Validate unique constraints for updates
        if (!employee.getEmail().equals(employeeDetails.getEmail()) && 
            employeeRepository.existsByEmailAndIdNot(employeeDetails.getEmail(), id)) {
            throw new RuntimeException("Employee with email " + employeeDetails.getEmail() + " already exists");
        }
        if (!employee.getEmployeeId().equals(employeeDetails.getEmployeeId()) && 
            employeeRepository.existsByEmployeeIdAndIdNot(employeeDetails.getEmployeeId(), id)) {
            throw new RuntimeException("Employee with ID " + employeeDetails.getEmployeeId() + " already exists");
        }

        // Update fields
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setBirthDate(employeeDetails.getBirthDate());
        employee.setGender(employeeDetails.getGender());
        employee.setAddress(employeeDetails.getAddress());
        employee.setPosition(employeeDetails.getPosition());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setEmploymentStatus(employeeDetails.getEmploymentStatus());
        employee.setEmploymentType(employeeDetails.getEmploymentType());
        employee.setSalary(employeeDetails.getSalary());
        employee.setCurrency(employeeDetails.getCurrency());
        employee.setManager(employeeDetails.getManager());
        employee.setBio(employeeDetails.getBio());
        employee.setSkills(employeeDetails.getSkills());
        employee.setProfilePictureUrl(employeeDetails.getProfilePictureUrl());
        employee.setEmergencyContactName(employeeDetails.getEmergencyContactName());
        employee.setEmergencyContactPhone(employeeDetails.getEmergencyContactPhone());
        employee.setEmergencyContactRelationship(employeeDetails.getEmergencyContactRelationship());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employee.setIsActive(false); // Soft delete
        employeeRepository.save(employee);
    }

    // Search operations
    public List<Employee> searchByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Employee> searchByDepartment(String department) {
        return employeeRepository.findByDepartmentIgnoreCaseAndIsActiveTrue(department);
    }

    public List<Employee> searchByPosition(String position) {
        return employeeRepository.findByPositionIgnoreCaseAndIsActiveTrue(position);
    }

    // Manager operations
    public List<Employee> getDirectReports(Long managerId) {
        return employeeRepository.findByManagerIdAndIsActiveTrue(managerId);
    }

    public List<Employee> getTopLevelManagers() {
        return employeeRepository.findTopLevelManagers();
    }

    // Statistics
    public long getTotalActiveEmployees() {
        return employeeRepository.countByIsActiveTrue();
    }

    public long getEmployeeCountByDepartment(String department) {
        return employeeRepository.countByDepartmentAndIsActiveTrue(department);
    }

    // Validation helpers
    public boolean isEmailAvailable(String email) {
        return !employeeRepository.existsByEmail(email);
    }

    public boolean isEmployeeIdAvailable(String employeeId) {
        return !employeeRepository.existsByEmployeeId(employeeId);
    }
}
