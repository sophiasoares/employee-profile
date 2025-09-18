package com.employeeprofile.backend.config;

import com.employeeprofile.backend.entity.*;
import com.employeeprofile.backend.repository.AbsenceRequestRepository;
import com.employeeprofile.backend.repository.EmployeeRepository;
import com.employeeprofile.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AbsenceRequestRepository absenceRequestRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no data exists
        if (employeeRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create employees
        Employee ceo = createEmployee("EMP001", "Sarah", "Johnson", "sarah.johnson@company.com", 
                "CEO", "Executive", null, new BigDecimal("150000"));

        Employee hrManager = createEmployee("EMP002", "Michael", "Chen", "michael.chen@company.com", 
                "HR Manager", "Human Resources", ceo, new BigDecimal("95000"));

        Employee devManager = createEmployee("EMP003", "Emily", "Rodriguez", "emily.rodriguez@company.com", 
                "Development Manager", "Engineering", ceo, new BigDecimal("110000"));

        Employee developer1 = createEmployee("EMP004", "James", "Wilson", "james.wilson@company.com", 
                "Senior Developer", "Engineering", devManager, new BigDecimal("85000"));

        Employee developer2 = createEmployee("EMP005", "Lisa", "Anderson", "lisa.anderson@company.com", 
                "Frontend Developer", "Engineering", devManager, new BigDecimal("75000"));

        Employee hrSpecialist = createEmployee("EMP006", "David", "Brown", "david.brown@company.com", 
                "HR Specialist", "Human Resources", hrManager, new BigDecimal("65000"));

        // Save employees
        employeeRepository.save(ceo);
        employeeRepository.save(hrManager);
        employeeRepository.save(devManager);
        employeeRepository.save(developer1);
        employeeRepository.save(developer2);
        employeeRepository.save(hrSpecialist);

        // Create feedback
        createFeedback(developer1, developer2, "Great Collaboration", 
                "Lisa has been excellent to work with on the frontend components. Her attention to detail is outstanding.",
                FeedbackType.POSITIVE, 5, "Teamwork");

        createFeedback(developer2, devManager, "Performance Review", 
                "Emily provides clear direction and is always available for technical guidance.",
                FeedbackType.MANAGER_FEEDBACK, 4, "Leadership");

        createFeedback(hrSpecialist, hrManager, "Process Improvement", 
                "Michael has streamlined our onboarding process significantly this quarter.",
                FeedbackType.PERFORMANCE_REVIEW, 5, "Process");

        createFeedback(devManager, developer1, "Code Quality", 
                "James consistently delivers high-quality code and mentors junior developers well.",
                FeedbackType.PERFORMANCE_REVIEW, 5, "Technical");

        // Create absence requests
        createAbsenceRequest(developer1, AbsenceType.VACATION, 
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(12),
                "Family vacation", AbsenceStatus.PENDING);

        createAbsenceRequest(developer2, AbsenceType.SICK_LEAVE, 
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(1),
                "Flu symptoms", AbsenceStatus.APPROVED, devManager);

        createAbsenceRequest(hrSpecialist, AbsenceType.PERSONAL_LEAVE, 
                LocalDate.now().plusDays(20), LocalDate.now().plusDays(20),
                "Medical appointment", AbsenceStatus.PENDING);

        System.out.println("✅ Sample data initialized successfully!");
        System.out.println("📊 Created 6 employees, 4 feedback entries, and 3 absence requests");
        System.out.println("🌐 API available at: http://localhost:8080/api");
    }

    private Employee createEmployee(String employeeId, String firstName, String lastName, String email,
                                   String position, String department, Employee manager, BigDecimal salary) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setManager(manager);
        employee.setSalary(salary);
        employee.setCurrency("EUR");
        employee.setHireDate(LocalDate.now().minusMonths((long) (Math.random() * 24)));
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        employee.setEmploymentType(EmploymentType.FULL_TIME);
        employee.setPhoneNumber("+49" + (int)(Math.random() * 900000000 + 100000000));
        employee.setBirthDate(LocalDate.now().minusYears(25 + (int)(Math.random() * 15)));
        employee.setIsActive(true);
        
        // Add some skills
        switch (department) {
            case "Engineering":
                employee.setSkills("Java, Spring Boot, Angular, TypeScript, PostgreSQL");
                break;
            case "Human Resources":
                employee.setSkills("Recruitment, Employee Relations, Performance Management, HRIS");
                break;
            case "Executive":
                employee.setSkills("Strategic Planning, Leadership, Business Development, Team Management");
                break;
        }
        
        return employee;
    }

    private void createFeedback(Employee employee, Employee feedbackGiver, String title, String content,
                               FeedbackType type, Integer rating, String category) {
        Feedback feedback = new Feedback();
        feedback.setEmployee(employee);
        feedback.setFeedbackGiver(feedbackGiver);
        feedback.setTitle(title);
        feedback.setContent(content);
        feedback.setFeedbackType(type);
        feedback.setRating(rating);
        feedback.setCategory(category);
        feedback.setStatus(FeedbackStatus.ACTIVE);
        feedback.setIsPublic(true);
        feedback.setIsAnonymous(false);
        feedback.setIsAiEnhanced(false);
        
        feedbackRepository.save(feedback);
    }

    private void createAbsenceRequest(Employee employee, AbsenceType type, LocalDate startDate, LocalDate endDate,
                                     String reason, AbsenceStatus status) {
        createAbsenceRequest(employee, type, startDate, endDate, reason, status, null);
    }

    private void createAbsenceRequest(Employee employee, AbsenceType type, LocalDate startDate, LocalDate endDate,
                                     String reason, AbsenceStatus status, Employee approvedBy) {
        AbsenceRequest request = new AbsenceRequest();
        request.setEmployee(employee);
        request.setAbsenceType(type);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setReason(reason);
        request.setStatus(status);
        request.setRequestedAt(LocalDateTime.now().minusDays(1));
        request.setIsHalfDay(false);
        
        if (approvedBy != null && status == AbsenceStatus.APPROVED) {
            request.setApprovedBy(approvedBy);
            request.setApprovedAt(LocalDateTime.now().minusHours(2));
            request.setManagerComments("Approved - documented and covered by team");
        }
        
        absenceRequestRepository.save(request);
    }
}
