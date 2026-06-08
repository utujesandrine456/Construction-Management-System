package com.example.cms.controller;

import com.example.cms.dto.response.DailyReportResponse;
import com.example.cms.service.DailyReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Daily Reports", description = "Endpoints for viewing auto-generated daily reports")
public class DailyReportController {

    private final DailyReportService dailyReportService;

    @Operation(
        summary = "Get daily reports by project",
        description = "Retrieves all daily reports for a given project. Available to OWNER and MANAGER."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<DailyReportResponse>> getReportsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(dailyReportService.getReportsByProject(projectId));
    }

    @Operation(
        summary = "Trigger daily report manually",
        description = "Manually triggers the daily report generation for all projects. Useful for testing the scheduler."
    )
    @ApiResponse(responseCode = "200", description = "Reports generated successfully")
    @PostMapping("/trigger")
    public ResponseEntity<String> triggerReportGeneration() {
        dailyReportService.generateDailyReportsForToday();
        return ResponseEntity.ok("Daily reports generated successfully for today.");
    }
}
