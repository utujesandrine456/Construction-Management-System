package com.example.cms.service;

import com.example.cms.dto.request.ExpenseRequest;
import com.example.cms.dto.response.ExpenseResponse;
import com.example.cms.entity.Expense;
import com.example.cms.entity.Project;
import com.example.cms.entity.Stage;
import com.example.cms.entity.User;
import com.example.cms.exception.ResourceNotFoundException;
import com.example.cms.repository.ExpenseRepository;
import com.example.cms.repository.ProjectRepository;
import com.example.cms.repository.StageRepository;
import com.example.cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ProjectRepository projectRepository;
    private final StageRepository stageRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public ExpenseResponse addExpense(ExpenseRequest request) {
        User currentUser = getCurrentUser();

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Stage stage = null;
        if (request.getStageId() != null) {
            stage = stageRepository.findById(request.getStageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        }

        Expense expense = Expense.builder()
                .description(request.getDescription())
                .amount(request.getAmount())
                .category(request.getCategory())
                .expenseDate(request.getExpenseDate())
                .project(project)
                .stage(stage)
                .recordedBy(currentUser)
                .build();

        expense = expenseRepository.save(expense);

        // Update project spent amount
        project.setSpentAmount(project.getSpentAmount().add(expense.getAmount()));
        projectRepository.save(project);

        return mapToResponse(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return expenseRepository.findByProject(project).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setProjectId(expense.getProject().getId());
        response.setStageId(expense.getStage() != null ? expense.getStage().getId() : null);
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setExpenseDate(expense.getExpenseDate());
        response.setRecordedBy(expense.getRecordedBy() != null ? expense.getRecordedBy().getEmail() : null);
        return response;
    }
}
