package com.scholaapi.controllers;

import com.scholaapi.dto.ApiResponse;
import com.scholaapi.dto.ModuleRequest;
import com.scholaapi.model.Module;
import com.scholaapi.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    // Get all modules in a course
    @GetMapping("/courses/{courseUuid}/modules")
    public ResponseEntity<ApiResponse<List<Module>>> getModulesByCourse(@PathVariable UUID courseUuid) {
        try {
            List<Module> modules = moduleService.getModulesByCourse(courseUuid);
            return ResponseEntity.ok(ApiResponse.success("Modules retrieved successfully", modules));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving modules: " + e.getMessage(), null));
        }
    }

    // Get specific module
    @GetMapping("/modules/{moduleUuid}")
    public ResponseEntity<ApiResponse<Module>> getModuleById(@PathVariable UUID moduleUuid) {
        try {
            Module module = moduleService.getModuleById(moduleUuid);
            return ResponseEntity.ok(ApiResponse.success("Module retrieved successfully", module));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving module: " + e.getMessage(), null));
        }
    }

    // Create module
    @PostMapping("/courses/{courseUuid}/modules")
    public ResponseEntity<ApiResponse<Module>> createModule(
            @PathVariable UUID courseUuid,
            @RequestBody ModuleRequest request) {
        try {
            Module module = moduleService.createModule(courseUuid, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Module created successfully", module));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating module: " + e.getMessage(), null));
        }
    }

    // Update module
    @PutMapping("/modules/{moduleUuid}")
    public ResponseEntity<ApiResponse<Module>> updateModule(
            @PathVariable UUID moduleUuid,
            @RequestBody ModuleRequest request) {
        try {
            Module module = moduleService.updateModule(moduleUuid, request);
            return ResponseEntity.ok(ApiResponse.success("Module updated successfully", module));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating module: " + e.getMessage(), null));
        }
    }

    // Delete module
    @DeleteMapping("/modules/{moduleUuid}")
    public ResponseEntity<ApiResponse<Void>> deleteModule(@PathVariable UUID moduleUuid) {
        try {
            moduleService.deleteModule(moduleUuid);
            return ResponseEntity.ok(ApiResponse.success("Module deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting module: " + e.getMessage(), null));
        }
    }
} 