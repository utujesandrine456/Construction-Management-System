package com.example.cms.controller;

import com.example.cms.dto.request.ExpenseRequest;
import com.example.cms.dto.response.ExpenseResponse;
import com.example.cms.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expense Management", description = "Endpoints for managing project expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Add an expense", description = "Adds a new expense to a project or stage")
    @ApiResponse(responseCode = "201", description = "Expense added successfully")
    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest request) {
        return new ResponseEntity<>(expenseService.addExpense(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get expenses by project", description = "Retrieves all expenses associated with a project")
    @ApiResponse(responseCode = "200", description = "Successful retrieval")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(expenseService.getExpensesByProject(projectId));
    }
}
