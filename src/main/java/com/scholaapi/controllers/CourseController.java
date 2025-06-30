package com.scholaapi.controllers;

import com.scholaapi.dto.CourseRequest;
import com.scholaapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Get all courses in the database
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // Get courses that a user is enrolled in
    @GetMapping("/user/{uuid}")
    public ResponseEntity<?> getCoursesByUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok(courseService.getCoursesByUser(uuid));
    }
    
    // Create course with full details
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(
                request.getOrganizationUuid(),
                request.getProfessorUuid(),
                request.getTitle(),
                request.getCategory(),
                request.getDescription()));
    }
    
    // Delete course by ID
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteCourse(@PathVariable UUID uuid) {
        courseService.deleteCourse(uuid);
        return ResponseEntity.ok("Course deleted successfully.");
    }
    
    // Get specific course by ID
    @GetMapping("/{uuid}")
    public ResponseEntity<?> getCourseById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(courseService.getCourseById(uuid));
    }
    
    // Update course details
    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateCourse(@PathVariable UUID uuid, @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(uuid, request));
    }
    
    // Get courses by organization
    @GetMapping("/organization/{organizationUuid}")
    public ResponseEntity<?> getCoursesByOrganization(@PathVariable UUID organizationUuid) {
        return ResponseEntity.ok(courseService.getCoursesByOrganization(organizationUuid));
    }
} 