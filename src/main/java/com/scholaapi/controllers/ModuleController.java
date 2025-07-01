package com.scholaapi.controllers;

import com.scholaapi.dto.ModuleRequest;
import com.scholaapi.dto.ModuleResponseDTO;
import com.scholaapi.model.Module;
import com.scholaapi.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    // Get all modules in a course
    @GetMapping("/courses/{courseUuid}/modules")
    public ResponseEntity<List<ModuleResponseDTO>> getModulesByCourse(@PathVariable UUID courseUuid) {
        return ResponseEntity.ok(moduleService.getModulesByCourse(courseUuid));
    }

    // Get specific module
    @GetMapping("/modules/{moduleUuid}")
    public ResponseEntity<ModuleResponseDTO> getModuleById(@PathVariable UUID moduleUuid) {
        Module module = moduleService.getModuleById(moduleUuid);
        return ResponseEntity.ok(moduleService.toModuleDTO(module));
    }

    // Create module
    @PostMapping("/modules/create")
    public ResponseEntity<?> createModule(@RequestBody ModuleRequest request) {
        return ResponseEntity.ok(moduleService.createModule(request.getCourseUuid(), request));
    }

    // Update module
    @PutMapping("/modules/{moduleUuid}")
    public ResponseEntity<?> updateModule(
            @PathVariable UUID moduleUuid,
            @RequestBody ModuleRequest request) {
        return ResponseEntity.ok(moduleService.updateModule(moduleUuid, request));
    }

    // Delete module
    @DeleteMapping("/modules/{moduleUuid}")
    public ResponseEntity<?> deleteModule(@PathVariable UUID moduleUuid) {
        moduleService.deleteModule(moduleUuid);
        return ResponseEntity.ok("Module deleted successfully.");
    }
} 