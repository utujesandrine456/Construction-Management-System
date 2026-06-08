package com.example.cms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {
    @NotNull(message = "Project ID is mandatory")
    private Long projectId;
    private Long stageId;
    @NotBlank(message = "Category is mandatory")
    private String category;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotNull(message = "Amount is mandatory")
    private BigDecimal amount;
    private LocalDate expenseDate;
}
