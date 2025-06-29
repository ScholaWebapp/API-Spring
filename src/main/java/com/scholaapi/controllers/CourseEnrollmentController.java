package com.scholaapi.controllers;

import com.scholaapi.dto.EnrollmentRequest;
import com.scholaapi.service.CourseEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
public class CourseEnrollmentController {

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    public CourseEnrollmentController(CourseEnrollmentService courseEnrollmentService) {
        this.courseEnrollmentService = courseEnrollmentService;
    }

    // Enroll user in a course
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollUserInCourse(@RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(courseEnrollmentService.enrollUserInCourse(request.getUserUuid(), request.getCourseUuid()));
    }

    // Unenroll user from a course
    @DeleteMapping("/unenroll")
    public ResponseEntity<?> unenrollUserFromCourse(@RequestBody EnrollmentRequest request) {
        courseEnrollmentService.unenrollUserFromCourse(request.getUserUuid(), request.getCourseUuid());
        return ResponseEntity.noContent().build();
    }
} 