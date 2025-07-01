package com.scholaapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ExamSubmissionRequest {
    private UUID studentUuid;
    private List<Integer> answers; // 0-based indices of selected answers
} 