package com.scholaapi.controllers;

import com.scholaapi.dto.ExamRequest;
import com.scholaapi.dto.ExamSubmissionRequest;
import com.scholaapi.model.Resource;
import com.scholaapi.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// Temporarily commented out to focus on video/document APIs
/*
@RestController
@RequestMapping("/api")
public class ExamController {

    @Autowired
    private ResourceService resourceService;

    // Create exam resource
    @PostMapping("/exams/create")
    public ResponseEntity<?> createExamResource(@RequestBody ExamRequest request) {
        return ResponseEntity.ok(resourceService.createExamResource(request));
    }

    // Get all exams in a module
    @GetMapping("/modules/{moduleUuid}/exams")
    public ResponseEntity<?> getExamsByModule(@PathVariable UUID moduleUuid) {
        return ResponseEntity.ok(resourceService.getExamsByModule(moduleUuid));
    }

    // Get specific exam resource
    @GetMapping("/exams/{examUuid}")
    public ResponseEntity<?> getExamResource(@PathVariable UUID examUuid) {
        return ResponseEntity.ok(resourceService.getExamResource(examUuid));
    }

    // Get exam without answers (for students)
    @GetMapping("/exams/{examUuid}/take")
    public ResponseEntity<?> getExamForStudent(@PathVariable UUID examUuid) {
        return ResponseEntity.ok(resourceService.getExamForStudent(examUuid));
    }

    // Submit exam answers
    @PostMapping("/exams/{examUuid}/submit")
    public ResponseEntity<?> submitExam(@PathVariable UUID examUuid, @RequestBody ExamSubmissionRequest submission) {
        return ResponseEntity.ok(resourceService.submitExam(examUuid, submission));
    }

    // Update exam resource
    @PutMapping("/exams/{examUuid}")
    public ResponseEntity<?> updateExamResource(@PathVariable UUID examUuid, @RequestBody ExamRequest request) {
        return ResponseEntity.ok(resourceService.updateExamResource(examUuid, request));
    }

    // Delete exam resource
    @DeleteMapping("/exams/{examUuid}")
    public ResponseEntity<?> deleteExamResource(@PathVariable UUID examUuid) {
        resourceService.deleteExamResource(examUuid);
        return ResponseEntity.ok("Exam resource deleted successfully.");
    }
}
*/ 