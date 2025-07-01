package com.scholaapi.dto;

import java.util.UUID;

public class VideoResponseDTO {
    private UUID uuid;
    private String title;
    private String description;
    private String filename;
    private String duration;
    // Getters and setters
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
} 