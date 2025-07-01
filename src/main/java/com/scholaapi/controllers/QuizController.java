package com.scholaapi.controllers;

import com.scholaapi.dto.QuizRequest;
import com.scholaapi.dto.QuizSubmissionRequest;
import com.scholaapi.model.Resource;
import com.scholaapi.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired
    private ResourceService resourceService;

    // Create quiz resource
    @PostMapping("/quizzes/create")
    public ResponseEntity<?> createQuizResource(@RequestBody QuizRequest request) {
        return ResponseEntity.ok(resourceService.createQuizResource(request));
    }

    // Get all quizzes in a module
    @GetMapping("/modules/{moduleUuid}/quizzes")
    public ResponseEntity<?> getQuizzesByModule(@PathVariable UUID moduleUuid) {
        return ResponseEntity.ok(resourceService.getQuizzesByModule(moduleUuid));
    }

    // Get specific quiz resource
    @GetMapping("/quizzes/{quizUuid}")
    public ResponseEntity<?> getQuizResource(@PathVariable UUID quizUuid) {
        return ResponseEntity.ok(resourceService.getQuizResource(quizUuid));
    }

    // Get quiz without answers (for students)
    @GetMapping("/quizzes/{quizUuid}/take")
    public ResponseEntity<?> getQuizForStudent(@PathVariable UUID quizUuid) {
        return ResponseEntity.ok(resourceService.getQuizForStudent(quizUuid));
    }

    // Submit quiz answers
    @PostMapping("/quizzes/{quizUuid}/submit")
    public ResponseEntity<?> submitQuiz(@PathVariable UUID quizUuid, @RequestBody QuizSubmissionRequest submission) {
        return ResponseEntity.ok(resourceService.submitQuiz(quizUuid, submission));
    }

    // Update quiz resource
    @PutMapping("/quizzes/{quizUuid}")
    public ResponseEntity<?> updateQuizResource(@PathVariable UUID quizUuid, @RequestBody QuizRequest request) {
        return ResponseEntity.ok(resourceService.updateQuizResource(quizUuid, request));
    }

    // Delete quiz resource
    @DeleteMapping("/quizzes/{quizUuid}")
    public ResponseEntity<?> deleteQuizResource(@PathVariable UUID quizUuid) {
        resourceService.deleteQuizResource(quizUuid);
        return ResponseEntity.ok("Quiz resource deleted successfully.");
    }
} 