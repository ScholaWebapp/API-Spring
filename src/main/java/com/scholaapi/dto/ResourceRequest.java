package com.scholaapi.dto;

import com.scholaapi.model.Resource;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceRequest {
    private Integer orderIndex;
    private Resource.ResourceType resourceType;
    
    // Video fields
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    
    // Document fields
    private String documentTitle;
    private String documentDescription;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    
    // Quiz fields
    private String quizTitle;
    private String quizDescription;
    
    // Exam fields
    private String examTitle;
    private String examDescription;
    private Integer passingGrade;
    private Integer timeLimitMinutes;
    private Integer maxRetakes;
} 