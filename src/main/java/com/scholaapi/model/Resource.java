package com.scholaapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "resources")
@Getter
@Setter
public class Resource {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "module_uuid", referencedColumnName = "uuid")
    private Module module;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    // One of these will be populated based on resourceType
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_uuid")
    private Video video;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_uuid")
    private Document document;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_uuid")
    private Quiz quiz;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exam_uuid")
    private Exam exam;

    public enum ResourceType {
        VIDEO, DOCUMENT, QUIZ, EXAM
    }

    // Validation method to ensure data integrity
    @PrePersist
    @PreUpdate
    public void validateResource() {
        int populatedCount = 0;
        if (video != null) populatedCount++;
        if (document != null) populatedCount++;
        if (quiz != null) populatedCount++;
        if (exam != null) populatedCount++;
        
        if (populatedCount != 1) {
            throw new IllegalStateException("Resource must have exactly one resource type populated");
        }
        
        // Validate that resourceType matches the populated resource
        switch (resourceType) {
            case VIDEO:
                if (video == null) throw new IllegalStateException("ResourceType.VIDEO but no video set");
                break;
            case DOCUMENT:
                if (document == null) throw new IllegalStateException("ResourceType.DOCUMENT but no document set");
                break;
            case QUIZ:
                if (quiz == null) throw new IllegalStateException("ResourceType.QUIZ but no quiz set");
                break;
            case EXAM:
                if (exam == null) throw new IllegalStateException("ResourceType.EXAM but no exam set");
                break;
        }
    }

    // Helper methods for easier access
    public String getTitle() {
        switch (resourceType) {
            case VIDEO: return video.getTitle();
            case DOCUMENT: return document.getTitle();
            case QUIZ: return quiz.getTitle();
            case EXAM: return exam.getTitle();
            default: return null;
        }
    }

    public String getDescription() {
        switch (resourceType) {
            case VIDEO: return video.getDescription();
            case DOCUMENT: return document.getDescription();
            case QUIZ: return quiz.getDescription();
            case EXAM: return exam.getDescription();
            default: return null;
        }
    }
} 