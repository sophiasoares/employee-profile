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
        // Create employees with complete data
        Employee ceo = createEmployee("EMP001", "Sarah", "Johnson", "sarah.johnson@company.com", 
                "CEO", "Executive", new BigDecimal("150000"),
                "Hauptstraße 123, 10115 Berlin, Germany", "https://i.pravatar.cc/150?img=1");

        Employee hrManager = createEmployee("EMP002", "Michael", "Chen", "michael.chen@company.com", 
                "HR Manager", "Human Resources", new BigDecimal("95000"),
                "Friedrichstraße 45, 10117 Berlin, Germany", "https://i.pravatar.cc/150?img=2");

        Employee devManager = createEmployee("EMP003", "Emily", "Rodriguez", "emily.rodriguez@company.com", 
                "Development Manager", "Engineering", new BigDecimal("110000"),
                "Unter den Linden 78, 10117 Berlin, Germany", "https://i.pravatar.cc/150?img=3");

        Employee developer1 = createEmployee("EMP004", "James", "Wilson", "james.wilson@company.com", 
                "Senior Developer", "Engineering", new BigDecimal("85000"),
                "Alexanderplatz 12, 10178 Berlin, Germany", "https://i.pravatar.cc/150?img=4");

        Employee developer2 = createEmployee("EMP005", "Lisa", "Anderson", "lisa.anderson@company.com", 
                "Frontend Developer", "Engineering", new BigDecimal("75000"),
                "Potsdamer Platz 9, 10785 Berlin, Germany", "https://i.pravatar.cc/150?img=5");

        Employee designer = createEmployee("EMP006", "Anna", "Mueller", "anna.mueller@company.com", 
                "UX Designer", "Design", new BigDecimal("72000"),
                "Hackescher Markt 34, 10178 Berlin, Germany", "https://i.pravatar.cc/150?img=6");

        Employee intern = createEmployee("EMP007", "Tom", "Schmidt", "tom.schmidt@company.com", 
                "Software Intern", "Engineering", new BigDecimal("35000"),
                "Prenzlauer Berg 67, 10405 Berlin, Germany", "https://i.pravatar.cc/150?img=7");

        Employee partTimeMarketing = createEmployee("EMP008", "Sophie", "Weber", "sophie.weber@company.com", 
                "Part-time Marketing Coordinator", "Marketing", new BigDecimal("45000"),
                "Kreuzberg 89, 10961 Berlin, Germany", "https://i.pravatar.cc/150?img=8");

        Employee securityConsultant = createEmployee("EMP009", "Marcus", "Fischer", "marcus.fischer@company.com", 
                "IT Security Consultant", "Engineering", new BigDecimal("95000"),
                "Charlottenburg 234, 10623 Berlin, Germany", "https://i.pravatar.cc/150?img=9");

        Employee strategyConsultant = createEmployee("EMP010", "Elena", "Kowalski", "elena.kowalski@company.com", 
                "Business Strategy Consultant", "Consulting", new BigDecimal("120000"),
                "Mitte 456, 10178 Berlin, Germany", "https://i.pravatar.cc/150?img=10");

        // Save employees
        employeeRepository.save(ceo);
        employeeRepository.save(hrManager);
        employeeRepository.save(devManager);
        employeeRepository.save(developer1);
        employeeRepository.save(developer2);
        employeeRepository.save(designer);
        employeeRepository.save(intern);
        employeeRepository.save(partTimeMarketing);
        employeeRepository.save(securityConsultant);
        employeeRepository.save(strategyConsultant);

        // Create feedback
        createFeedback(developer1, developer2, 
                "Lisa has been excellent to work with on the frontend components. Her attention to detail is outstanding.",
                FeedbackType.POSITIVE);

        createFeedback(developer2, devManager, 
                "Emily provides clear direction and is always available for technical guidance.",
                FeedbackType.MANAGER_FEEDBACK);

        createFeedback(devManager, developer1, 
                "James consistently delivers high-quality code and mentors junior developers well.",
                FeedbackType.PERFORMANCE_REVIEW);

        createFeedback(designer, ceo, 
                "Anna has created an excellent design system that improved our product consistency.",
                FeedbackType.POSITIVE);

        createFeedback(intern, developer1, 
                "James has been an amazing mentor, helping me understand complex concepts.",
                FeedbackType.POSITIVE);

        createFeedback(securityConsultant, devManager, 
                "Marcus has significantly improved our security posture with comprehensive audits.",
                FeedbackType.PERFORMANCE_REVIEW);

        // Create absence requests
        createAbsenceRequest(developer1, AbsenceType.VACATION, 
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(12),
                "Family vacation");

        createAbsenceRequest(developer2, AbsenceType.SICK_LEAVE, 
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(1),
                "Flu symptoms");

        createAbsenceRequest(designer, AbsenceType.VACATION, 
                LocalDate.now().plusDays(30), LocalDate.now().plusDays(37),
                "Summer vacation in Italy");

        createAbsenceRequest(intern, AbsenceType.TRAINING, 
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(7),
                "Angular workshop");

        createAbsenceRequest(partTimeMarketing, AbsenceType.PERSONAL_LEAVE, 
                LocalDate.now().plusDays(15), LocalDate.now().plusDays(15),
                "Medical appointment");

        System.out.println("✅ Sample data initialized successfully!");
        System.out.println("📊 Created 10 employees, 6 feedback entries, and 5 absence requests");
        System.out.println("🌐 API available at: http://localhost:8080/api");
    }

    private Employee createEmployee(String employeeId, String firstName, String lastName, String email,
                                   String position, String department, BigDecimal salary,
                                   String address, String profilePicture) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setSalary(salary);
        employee.setHireDate(LocalDate.now().minusMonths((long) (Math.random() * 24)));
        
        // Set employment type based on position
        if (position.contains("Intern")) {
            employee.setEmploymentType(EmploymentType.INTERN);
        } else if (position.contains("Consultant") || position.contains("Contractor")) {
            employee.setEmploymentType(EmploymentType.CONTRACT);
        } else if (position.contains("Part-time")) {
            employee.setEmploymentType(EmploymentType.PART_TIME);
        } else {
            employee.setEmploymentType(EmploymentType.FULL_TIME);
        }
        
        employee.setPhoneNumber("+49" + (int)(Math.random() * 900000000 + 100000000));
        employee.setAddress(address);
        employee.setProfilePictureUrl(profilePicture);
        
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
            case "Design":
                employee.setSkills("UX Design, UI Design, Graphic Design, Branding");
                break;
            case "Marketing":
                employee.setSkills("Digital Marketing, Social Media, Content Creation, SEO");
                break;
            case "Consulting":
                employee.setSkills("Business Strategy, IT Consulting, Project Management, Change Management");
                break;
        }
        
        // Initialize bio
        employee.setBio("Highly motivated and experienced " + position + " with a strong background in " + department + ". Proven track record of delivering high-quality results and exceeding expectations.");
        
        return employee;
    }

    private void createFeedback(Employee employee, Employee feedbackGiver, String content,
                               FeedbackType type) {
        Feedback feedback = new Feedback();
        feedback.setEmployee(employee);
        feedback.setFeedbackGiver(feedbackGiver);
        feedback.setContent(content);
        feedback.setFeedbackType(type);
        feedback.setIsAiEnhanced(false);
        
        feedbackRepository.save(feedback);
    }

    private void createAbsenceRequest(Employee employee, AbsenceType type, LocalDate startDate, LocalDate endDate,
                                     String reason) {
        AbsenceRequest request = new AbsenceRequest();
        request.setEmployee(employee);
        request.setAbsenceType(type);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setReason(reason);
        
        absenceRequestRepository.save(request);
    }
}
