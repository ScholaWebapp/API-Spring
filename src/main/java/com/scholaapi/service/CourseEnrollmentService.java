package com.scholaapi.service;

import com.scholaapi.model.Course;
import com.scholaapi.model.CourseEnrollment;
import com.scholaapi.model.User;
import com.scholaapi.repository.CourseEnrollmentRepository;
import com.scholaapi.repository.CourseRepository;
import com.scholaapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseEnrollmentService {

    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Enroll user in a course
    public CourseEnrollment enrollUserInCourse(UUID userUuid, UUID courseUuid) {
        // Verify user exists
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Verify course exists
        Course course = courseRepository.findById(courseUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        
        // Check if user is already enrolled
        if (courseEnrollmentRepository.existsByUserUuidAndCourseUuid(userUuid, courseUuid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already enrolled in this course");
        }
        
        // Create enrollment
        CourseEnrollment enrollment = new CourseEnrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus("enrolled");
        enrollment.setProgressPercentage(0);
        enrollment.setLastAccessedAt(LocalDateTime.now());
        
        return courseEnrollmentRepository.save(enrollment);
    }

    // Unenroll user from a course
    public void unenrollUserFromCourse(UUID userUuid, UUID courseUuid) {
        // Verify user exists
        if (!userRepository.existsById(userUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        // Verify course exists
        if (!courseRepository.existsById(courseUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        
        // Find and delete enrollment
        CourseEnrollment enrollment = courseEnrollmentRepository.findByUserUuidAndCourseUuid(userUuid, courseUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));
        
        courseEnrollmentRepository.delete(enrollment);
    }

    // Get all courses that a user is enrolled in
    public List<Course> getEnrolledCoursesByUser(UUID userUuid) {
        // Verify user exists
        if (!userRepository.existsById(userUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        // Get enrollments and extract courses
        List<CourseEnrollment> enrollments = courseEnrollmentRepository.findByUserUuid(userUuid);
        return enrollments.stream()
                .map(CourseEnrollment::getCourse)
                .collect(Collectors.toList());
    }

    // Get enrollment details for a user in a specific course
    public CourseEnrollment getEnrollmentByUserAndCourse(UUID userUuid, UUID courseUuid) {
        return courseEnrollmentRepository.findByUserUuidAndCourseUuid(userUuid, courseUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));
    }

    // Update enrollment status
    public CourseEnrollment updateEnrollmentStatus(UUID userUuid, UUID courseUuid, String status) {
        CourseEnrollment enrollment = getEnrollmentByUserAndCourse(userUuid, courseUuid);
        enrollment.setStatus(status);
        
        if ("completed".equals(status)) {
            enrollment.setCompletedAt(LocalDateTime.now());
        }
        
        return courseEnrollmentRepository.save(enrollment);
    }

    // Update progress percentage
    public CourseEnrollment updateProgress(UUID userUuid, UUID courseUuid, Integer progressPercentage) {
        if (progressPercentage < 0 || progressPercentage > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Progress percentage must be between 0 and 100");
        }
        
        CourseEnrollment enrollment = getEnrollmentByUserAndCourse(userUuid, courseUuid);
        enrollment.setProgressPercentage(progressPercentage);
        enrollment.setLastAccessedAt(LocalDateTime.now());
        
        // Update status based on progress
        if (progressPercentage == 100) {
            enrollment.setStatus("completed");
            enrollment.setCompletedAt(LocalDateTime.now());
        } else if (progressPercentage > 0) {
            enrollment.setStatus("in_progress");
        }
        
        return courseEnrollmentRepository.save(enrollment);
    }
} 