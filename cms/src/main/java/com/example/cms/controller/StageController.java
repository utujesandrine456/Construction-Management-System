package com.example.cms.controller;

import com.example.cms.dto.request.StageRequest;
import com.example.cms.dto.response.StageResponse;
import com.example.cms.enums.StageStatus;
import com.example.cms.service.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stages")
@RequiredArgsConstructor
@Tag(name = "Stage Management", description = "Endpoints for managing project stages")
public class StageController {

    private final StageService stageService;

    @Operation(summary = "Create a new stage", description = "Creates a new stage for a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stage created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping
    public ResponseEntity<StageResponse> createStage(@Valid @RequestBody StageRequest request) {
        return new ResponseEntity<>(stageService.createStage(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update stage status", description = "Updates the status of a specific stage")
    @ApiResponse(responseCode = "200", description = "Status updated successfully")
    @PatchMapping("/{id}/status")
    public ResponseEntity<StageResponse> updateStageStatus(@PathVariable Long id, @RequestParam StageStatus status) {
        return ResponseEntity.ok(stageService.updateStageStatus(id, status));
    }

    @Operation(summary = "Get stage by ID", description = "Retrieves a specific stage by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval")
    @GetMapping("/{id}")
    public ResponseEntity<StageResponse> getStageById(@PathVariable Long id) {
        return ResponseEntity.ok(stageService.getStageById(id));
    }
}
