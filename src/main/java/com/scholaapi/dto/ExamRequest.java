package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ExamRequest {
    private UUID moduleUuid;
    private String title;
    private String description;
    private List<ExamQuestionRequest> questions;
    private Integer passingScore; // percentage, optional
    private Boolean allowRetake; // whether students can retake the exam
} 