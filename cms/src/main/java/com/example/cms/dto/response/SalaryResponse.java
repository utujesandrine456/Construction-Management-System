package com.example.cms.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalaryResponse {
    private Long id;
    private Long workerId;
    private Long recordedById;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String description;
}
