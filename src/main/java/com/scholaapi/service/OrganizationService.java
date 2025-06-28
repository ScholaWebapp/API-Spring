package com.scholaapi.service;

import com.scholaapi.model.Organization;
import com.scholaapi.model.User;
import com.scholaapi.repository.OrganizationRepository;
import com.scholaapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Get all organizations created by a specific user
    public List<Organization> getOrganizationsByCreator(UUID creatorUuid) {
        return organizationRepository.findByUserUuid(creatorUuid);
    }
    
    // Create organization for testing (just with UUID)
    public Organization createOrganizationForTesting(UUID userUuid) {
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        Organization organization = new Organization();
        organization.setUser(user);
        organization.setName("Test Organization");
        organization.setDescription("This is a test organization");
        
        return organizationRepository.save(organization);
    }
    
    // Create organization with full details
    public Organization createOrganization(UUID userUuid, String name, String description) {
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        Organization organization = new Organization();
        organization.setUser(user);
        organization.setName(name);
        organization.setDescription(description);
        
        return organizationRepository.save(organization);
    }
    
    // Delete organization by ID
    public void deleteOrganization(UUID organizationUuid) {
        if (!organizationRepository.existsById(organizationUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }
        organizationRepository.deleteById(organizationUuid);
    }
} 