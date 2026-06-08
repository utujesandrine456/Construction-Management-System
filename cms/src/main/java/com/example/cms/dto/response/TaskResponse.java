package com.example.cms.dto.response;

import com.example.cms.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private String assignedWorkerName;
    private String assignedByManagerName;
    private LocalDate taskDate;
    private String workerNotes;
    private String managerFeedback;
}
