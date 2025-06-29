package com.scholaapi.controllers;

import com.scholaapi.dto.OrganizationRequest;
import com.scholaapi.dto.OrganizationTestRequest;
import com.scholaapi.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // Get all organizations created by a specific user
    @GetMapping("/user/{uuid}")
    public ResponseEntity<?> getOrganizationsByUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok(organizationService.getOrganizationsByCreator(uuid));
    }
    
    // Create organization for testing (just with UUID)
    @PostMapping("/test")
    public ResponseEntity<?> createOrganizationForTesting(@RequestBody OrganizationTestRequest request) {
        return ResponseEntity.ok(organizationService.createOrganizationForTesting(request.getUserUuid()));
    }
    
    // Create organization with full details
    @PostMapping("/create")
    public ResponseEntity<?> createOrganization(@RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.createOrganization(
                request.getUserUuid(),
                request.getName(),
                request.getDescription()));
    }
    
    // Delete organization by ID
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteOrganization(@PathVariable UUID uuid) {
        organizationService.deleteOrganization(uuid);
        return ResponseEntity.noContent().build();
    }
    
    // Get all organizations except those created by a specific user
    @GetMapping("/others/{uuid}")
    public ResponseEntity<?> getAllOrganizationsExceptByUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok(organizationService.getAllOrganizationsExceptByUser(uuid));
    }
} 