package com.example.cms.dto.response;

import com.example.cms.enums.StageStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private BigDecimal totalBudget;
    private BigDecimal spentAmount;
    private StageStatus overallStatus;
    private Long ownerId;
    private Long managerId;
    private LocalDateTime startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;
}
