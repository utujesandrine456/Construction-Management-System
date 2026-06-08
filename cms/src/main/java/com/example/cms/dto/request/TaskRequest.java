package com.example.cms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank
    private String title;
    private String description;
    private Long assignedWorkerId;
    private Long stageId;
    private LocalDate taskDate;
}
