package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class QuizQuestionRequest {
    private String question;
    private List<String> options;
    private Integer correctAnswerIndex; // 0-based index of correct option
} 