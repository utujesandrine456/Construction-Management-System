package com.example.cms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StageRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
    @NotNull(message = "Project ID is mandatory")
    private Long projectId;
    private int orderNumber;
    private BigDecimal stageBudget;
    private LocalDate startDate;
    private LocalDate endDate;
}
