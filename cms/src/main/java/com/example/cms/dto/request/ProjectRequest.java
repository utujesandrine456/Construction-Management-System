package com.example.cms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
    private String location;
    @NotNull(message = "Total budget is mandatory")
    private BigDecimal totalBudget;
    private Long managerId;
    private LocalDate expectedEndDate;
}
