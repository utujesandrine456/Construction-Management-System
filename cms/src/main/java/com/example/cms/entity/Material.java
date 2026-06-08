package com.example.cms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "materials")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                  // "Cement bags", "Steel rods"
    private String unit;                  // "bags", "tons", "pieces"
    private Double quantityUsed;
    private Double quantityOrdered;
    private BigDecimal unitPrice;
    private BigDecimal totalCost;         // quantityUsed * unitPrice

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDate dateUsed;
}