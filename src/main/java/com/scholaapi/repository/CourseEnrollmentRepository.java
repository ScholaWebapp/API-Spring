package com.scholaapi.repository;

import com.scholaapi.model.CourseEnrollment;
import com.scholaapi.model.Course;
import com.scholaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, UUID> {
    
    // Find all enrollments for a user
    List<CourseEnrollment> findByUserUuid(UUID userUuid);
    List<CourseEnrollment> findByUser(User user);
    
    // Find all enrollments for a course
    List<CourseEnrollment> findByCourseUuid(UUID courseUuid);
    List<CourseEnrollment> findByCourse(Course course);
    
    // Find specific enrollment (user + course)
    Optional<CourseEnrollment> findByUserUuidAndCourseUuid(UUID userUuid, UUID courseUuid);
    
    // Check if user is enrolled in course
    boolean existsByUserUuidAndCourseUuid(UUID userUuid, UUID courseUuid);
    
    // Find enrollments by status
    List<CourseEnrollment> findByUserUuidAndStatus(UUID userUuid, String status);
    
    // Delete all enrollments for a course
    void deleteByCourseUuid(UUID courseUuid);
} 