package com.example.cms.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailyReportResponse {
    private Long id;
    private LocalDate reportDate;
    private String summary;
    private String workDone;
    private String challenges;
    private String planForTomorrow;
    private int numberOfWorkers;
    private Long projectId;
    private Long stageId;
    private Long submittedById;
    private LocalDateTime createdAt;
}
