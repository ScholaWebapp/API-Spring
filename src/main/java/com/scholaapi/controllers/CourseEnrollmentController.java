package com.scholaapi.controllers;

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
    public ResponseEntity<?> enrollUserInCourse(@RequestParam UUID userUuid, 
                                               @RequestParam UUID courseUuid) {
        return ResponseEntity.ok(courseEnrollmentService.enrollUserInCourse(userUuid, courseUuid));
    }

    // Unenroll user from a course
    @DeleteMapping("/unenroll")
    public ResponseEntity<?> unenrollUserFromCourse(@RequestParam UUID userUuid, 
                                                   @RequestParam UUID courseUuid) {
        courseEnrollmentService.unenrollUserFromCourse(userUuid, courseUuid);
        return ResponseEntity.noContent().build();
    }
} 