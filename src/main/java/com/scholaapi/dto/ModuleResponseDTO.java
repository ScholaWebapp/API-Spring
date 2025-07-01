package com.scholaapi.dto;

import java.util.List;
import java.util.UUID;

public class ModuleResponseDTO {
    private UUID uuid;
    private String title;
    private String description;
    private List<ResourceResponseDTO> resources;

    // Getters and setters
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<ResourceResponseDTO> getResources() { return resources; }
    public void setResources(List<ResourceResponseDTO> resources) { this.resources = resources; }
} 