package com.example.cms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalaryRequest {
    @NotNull(message = "Worker ID is mandatory")
    private Long workerId;
    @NotNull(message = "Amount is mandatory")
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String description;
}
