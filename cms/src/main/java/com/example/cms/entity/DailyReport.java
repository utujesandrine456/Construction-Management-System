package com.example.cms.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_reports")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reportDate;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String workDone;

    @Column(columnDefinition = "TEXT")
    private String challenges;

    @Column(columnDefinition = "TEXT")
    private String planForTomorrow;

    private int numberOfWorkers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")     // manager
    private User submittedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}