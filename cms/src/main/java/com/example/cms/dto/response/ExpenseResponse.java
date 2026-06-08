package com.example.cms.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseResponse {
    private Long id;
    private Long projectId;
    private Long stageId;
    private String category;
    private String description;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String recordedBy;
}
