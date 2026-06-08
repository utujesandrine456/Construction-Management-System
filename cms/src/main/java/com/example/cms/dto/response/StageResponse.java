package com.example.cms.dto.response;

import com.example.cms.enums.StageStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StageResponse {
    private Long id;
    private String name;
    private String description;
    private int orderNumber;
    private StageStatus status;
    private Long projectId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal stageBudget;
}
