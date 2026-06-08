package com.example.cms.service;

import com.example.cms.dto.response.DailyReportResponse;
import com.example.cms.entity.*;
import com.example.cms.enums.TaskStatus;
import com.example.cms.exception.ResourceNotFoundException;
import com.example.cms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ExpenseRepository expenseRepository;

        @Transactional
    public void generateDailyReportsForToday() {
        LocalDate today = LocalDate.now();
        List<Project> activeProjects = projectRepository.findAll();

        for (Project project : activeProjects) {
            try {
                generateReportForProject(project, today);
            } catch (Exception e) {
                log.error("Failed to generate report for project {}", project.getId(), e);
            }
        }
    }

    private void generateReportForProject(Project project, LocalDate date) {
        List<Task> tasks = taskRepository.findByProjectAndStatus(project, TaskStatus.COMPLETED);
        List<Expense> expenses = expenseRepository.findByProjectAndExpenseDateBetween(project, date, date);

        if (tasks.isEmpty() && expenses.isEmpty()) {
            log.info("No activity for project {} on {}", project.getId(), date);
            return;
        }

        String workDone = tasks.stream()
                .map(t -> "- " + t.getTitle() + ": " + t.getDescription())
                .collect(Collectors.joining("\n"));

        String expenseSummary = expenses.stream()
                .map(e -> "- " + e.getCategory() + ": $" + e.getAmount() + " (" + e.getDescription() + ")")
                .collect(Collectors.joining("\n"));

        String summary = "Tasks Completed:\n" + (workDone.isEmpty() ? "None" : workDone)
                + "\n\nExpenses:\n" + (expenseSummary.isEmpty() ? "None" : expenseSummary);

        DailyReport report = DailyReport.builder()
                .reportDate(date)
                .project(project)
                .summary(summary)
                .workDone(workDone.isEmpty() ? "No tasks completed" : workDone)
                .submittedBy(project.getManager())
                .numberOfWorkers((int) tasks.stream()
                        .filter(t -> t.getAssignedWorker() != null)
                        .map(t -> t.getAssignedWorker().getId())
                        .distinct()
                        .count())
                .build();

        dailyReportRepository.save(report);
        log.info("Saved daily report for project {} on {}", project.getId(), date);
    }

    @Transactional(readOnly = true)
    public List<DailyReportResponse> getReportsByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return dailyReportRepository.findByProjectOrderByReportDateDesc(project).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DailyReportResponse mapToResponse(DailyReport report) {
        DailyReportResponse response = new DailyReportResponse();
        response.setId(report.getId());
        response.setReportDate(report.getReportDate());
        response.setSummary(report.getSummary());
        response.setWorkDone(report.getWorkDone());
        response.setChallenges(report.getChallenges());
        response.setPlanForTomorrow(report.getPlanForTomorrow());
        response.setNumberOfWorkers(report.getNumberOfWorkers());
        response.setProjectId(report.getProject() != null ? report.getProject().getId() : null);
        response.setStageId(report.getStage() != null ? report.getStage().getId() : null);
        response.setSubmittedById(report.getSubmittedBy() != null ? report.getSubmittedBy().getId() : null);
        response.setCreatedAt(report.getCreatedAt());
        return response;
    }
}
