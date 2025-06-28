package com.scholaapi.controllers;

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
    public ResponseEntity<?> createCourse(@RequestParam UUID organizationUuid,
                                         @RequestParam UUID professorUuid,
                                         @RequestParam String title,
                                         @RequestParam String category,
                                         @RequestParam String description) {
        return ResponseEntity.ok(courseService.createCourse(organizationUuid, professorUuid, title, category, description));
    }
} 