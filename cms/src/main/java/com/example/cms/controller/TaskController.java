package com.example.cms.controller;

import com.example.cms.dto.request.TaskRequest;
import com.example.cms.dto.response.TaskResponse;
import com.example.cms.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task Management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task (Manager only)")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request, Authentication authentication) {
        Long managerId = extractUserId(authentication);
        return ResponseEntity.ok(taskService.createTask(request, managerId));
    }

    @Operation(summary = "Submit a task (Worker only)")
    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("/{id}/submit")
    public ResponseEntity<TaskResponse> submitTask(
            @PathVariable Long id,
            @RequestParam String notes,
            Authentication authentication) {
        Long workerId = extractUserId(authentication);
        return ResponseEntity.ok(taskService.submitTask(id, notes, workerId));
    }

    @Operation(summary = "Review a task (Manager only)")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/review")
    public ResponseEntity<TaskResponse> reviewTask(
            @PathVariable Long id,
            @RequestParam boolean approve,
            @RequestParam(required = false) String feedback,
            Authentication authentication) {
        Long managerId = extractUserId(authentication);
        return ResponseEntity.ok(taskService.reviewTask(id, approve, feedback, managerId));
    }

    @Operation(summary = "Get tasks assigned to worker (Worker only)")
    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/worker")
    public ResponseEntity<Page<TaskResponse>> getWorkerTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            Authentication authentication) {
        Long workerId = extractUserId(authentication);
        return ResponseEntity.ok(taskService.getWorkerTasks(workerId, PageRequest.of(page, size, Sort.by(sortBy).descending())));
    }

    private Long extractUserId(Authentication authentication) {
        // Need to parse user id from UserDetails or modify UserDetailsImpl
        com.example.cms.entity.User user = (com.example.cms.entity.User) authentication.getPrincipal();
        return user.getId();
    }
}
