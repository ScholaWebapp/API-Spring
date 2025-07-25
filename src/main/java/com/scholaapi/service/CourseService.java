package com.scholaapi.service;

import com.scholaapi.dto.CourseRequest;
import com.scholaapi.model.Course;
import com.scholaapi.model.Organization;
import com.scholaapi.model.User;
import com.scholaapi.repository.CourseRepository;
import com.scholaapi.repository.UserRepository;
import com.scholaapi.repository.CourseEnrollmentRepository;
import com.scholaapi.repository.OrganizationRepository;
import com.scholaapi.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
    
    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;
    
    @Autowired
    private ModuleRepository moduleRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    // Delete course and all its enrollments and modules with recursive cascading
    @Transactional
    public void deleteCourse(UUID courseUuid) {
        // Check if course exists
        if (!courseRepository.existsById(courseUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        
        // Delete all enrollments for this course
        courseEnrollmentRepository.deleteByCourseUuid(courseUuid);
        entityManager.flush(); // Force the delete to execute
        
        // Delete all modules in this course (this will cascade to resources)
        moduleRepository.deleteByCourseUuid(courseUuid);
        
        // Delete the course
        courseRepository.deleteById(courseUuid);
    }
    
    // Get specific course by ID
    public Course getCourseById(UUID courseUuid) {
        return courseRepository.findById(courseUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }
    
    // Update course details
    public Course updateCourse(UUID courseUuid, CourseRequest request) {
        Course course = courseRepository.findById(courseUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        
        // Update course fields
        course.setTitle(request.getTitle());
        course.setCategory(request.getCategory());
        course.setDescription(request.getDescription());
        
        // Note: We're not updating organization or professor for data integrity
        // If you need to change these, it should be a separate operation
        
        return courseRepository.save(course);
    }
    
    // Get courses by organization
    public List<Course> getCoursesByOrganization(UUID organizationUuid) {
        // Verify organization exists
        if (!organizationRepository.existsById(organizationUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }
        
        return courseRepository.findByOrganizationUuid(organizationUuid);
    }
} 