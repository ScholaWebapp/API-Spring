package com.scholaapi.controllers;

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
    @PostMapping("/test/{uuid}")
    public ResponseEntity<?> createOrganizationForTesting(@PathVariable UUID uuid) {
        return ResponseEntity.ok(organizationService.createOrganizationForTesting(uuid));
    }
    
    // Create organization with full details
    @PostMapping("/create")
    public ResponseEntity<?> createOrganization(@RequestParam UUID userUuid, 
                                               @RequestParam String name, 
                                               @RequestParam String description) {
        return ResponseEntity.ok(organizationService.createOrganization(userUuid, name, description));
    }
    
    // Delete organization by ID
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteOrganization(@PathVariable UUID uuid) {
        organizationService.deleteOrganization(uuid);
        return ResponseEntity.noContent().build();
    }
} 