package com.example.cms.entity;
import com.example.cms.enums.StageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "stages")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int orderNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StageStatus status = StageStatus.NOT_STARTED;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Material> materials = new ArrayList<>();


    @OneToMany(mappedBy = "stage")
    @Builder.Default
    private List<ProjectImage> images = new ArrayList<>();


    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal stageBudget;
}
