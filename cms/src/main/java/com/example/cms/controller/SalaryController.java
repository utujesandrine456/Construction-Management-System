package com.example.cms.controller;

import com.example.cms.dto.request.SalaryRequest;
import com.example.cms.dto.response.SalaryResponse;
import com.example.cms.service.SalaryService;
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
@RequestMapping("/api/salaries")
@RequiredArgsConstructor
@Tag(name = "Salary Management", description = "Endpoints for managing worker salary payments")
public class SalaryController {

    private final SalaryService salaryService;

    @Operation(summary = "Record a salary payment", description = "Records a salary payment to a worker")
    @ApiResponse(responseCode = "201", description = "Salary recorded successfully")
    @PostMapping
    public ResponseEntity<SalaryResponse> recordSalaryPayment(@Valid @RequestBody SalaryRequest request) {
        return new ResponseEntity<>(salaryService.recordSalaryPayment(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get salaries by worker", description = "Retrieves all salary payments made to a specific worker")
    @ApiResponse(responseCode = "200", description = "Successful retrieval")
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<SalaryResponse>> getSalariesByWorker(@PathVariable Long workerId) {
        return ResponseEntity.ok(salaryService.getSalariesByWorker(workerId));
    }
}
