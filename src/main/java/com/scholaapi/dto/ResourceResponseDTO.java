package com.scholaapi.dto;

import java.util.UUID;

public class ResourceResponseDTO {
    private UUID uuid;
    private String resourceType;
    private String title;
    private String description;
    private String videoFilename;
    private String documentFilename;
    // Add more fields as needed

    // Getters and setters
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVideoFilename() { return videoFilename; }
    public void setVideoFilename(String videoFilename) { this.videoFilename = videoFilename; }
    public String getDocumentFilename() { return documentFilename; }
    public void setDocumentFilename(String documentFilename) { this.documentFilename = documentFilename; }
} 