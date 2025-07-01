package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class QuizRequest {
    private UUID moduleUuid;
    private String title;
    private String description;
    private List<QuizQuestionRequest> questions;
    private Integer passingScore; // percentage, optional
} 