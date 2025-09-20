package com.employeeprofile.backend.service;

import com.employeeprofile.backend.entity.Employee;
import com.employeeprofile.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllByOrderByIdAsc();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setAddress(employeeDetails.getAddress());
        employee.setPosition(employeeDetails.getPosition());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setEmploymentType(employeeDetails.getEmploymentType());
        employee.setSalary(employeeDetails.getSalary());
        employee.setBio(employeeDetails.getBio());
        employee.setSkills(employeeDetails.getSkills());
        employee.setProfilePictureUrl(employeeDetails.getProfilePictureUrl());

        return employeeRepository.save(employee);
    }

    public List<Employee> searchByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }
}
