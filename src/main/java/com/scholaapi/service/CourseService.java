package com.scholaapi.service;

import com.scholaapi.model.Course;
import com.scholaapi.model.Organization;
import com.scholaapi.model.User;
import com.scholaapi.repository.CourseRepository;
import com.scholaapi.repository.OrganizationRepository;
import com.scholaapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CourseEnrollmentService courseEnrollmentService;
    
    @Autowired
    private OrganizationRepository organizationRepository;

    // Get all courses in the database
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get courses that a user is enrolled in
    public List<Course> getCoursesByUser(UUID userUuid) {
        // Verify user exists
        if (!userRepository.existsById(userUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        // Get enrolled courses using the enrollment service
        return courseEnrollmentService.getEnrolledCoursesByUser(userUuid);
    }
    
    // Create course with full details
    public Course createCourse(UUID organizationUuid, UUID professorUuid, String title, String category, String description) {
        Organization organization = organizationRepository.findById(organizationUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));
        
        User professor = userRepository.findById(professorUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));
        
        // Validate that professor is a member of the organization
        if (!organization.getMembers().contains(professor.getUuid())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Professor must be a member of the organization");
        }
        
        Course course = new Course();
        course.setOrganization(organization);
        course.setProfessor(professor);
        course.setTitle(title);
        course.setCategory(category);
        course.setDescription(description);
        
        return courseRepository.save(course);
    }
} 