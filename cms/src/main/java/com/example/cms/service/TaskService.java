package com.example.cms.service;

import com.example.cms.dto.request.TaskRequest;
import com.example.cms.dto.response.TaskResponse;
import com.example.cms.entity.Stage;
import com.example.cms.entity.Task;
import com.example.cms.entity.User;
import com.example.cms.enums.TaskStatus;
import com.example.cms.enums.Role;
import com.example.cms.exception.ResourceNotFoundException;
import com.example.cms.repository.StageRepository;
import com.example.cms.repository.TaskRepository;
import com.example.cms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;




@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final StageRepository stageRepository;

    public TaskResponse createTask(TaskRequest request, Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        User worker = userRepository.findById(request.getAssignedWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (worker.getRole() != Role.WORKER) {
            throw new RuntimeException("Can only assign tasks to workers");
        }

        Stage stage = stageRepository.findById(request.getStageId())
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .assignedWorker(worker)
                .assignedByManager(manager)
                .project(stage.getProject())
                .stage(stage)
                .taskDate(request.getTaskDate())
                .status(TaskStatus.PENDING)
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    public TaskResponse submitTask(Long taskId, String workerNotes, Long workerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getAssignedWorker().getId().equals(workerId)) {
            throw new RuntimeException("Not your task");
        }

        if (task.getStatus() != TaskStatus.PENDING &&
                task.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new RuntimeException("Task cannot be submitted in current status");
        }

        task.setStatus(TaskStatus.SUBMITTED);
        task.setWorkerNotes(workerNotes);
        task.setSubmittedAt(LocalDateTime.now());

        return mapToResponse(taskRepository.save(task));
    }

    public TaskResponse reviewTask(Long taskId, boolean approve,
                                   String feedback, Long managerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getAssignedByManager().getId().equals(managerId)) {
            throw new RuntimeException("You didn't assign this task");
        }

        if (task.getStatus() != TaskStatus.SUBMITTED) {
            throw new RuntimeException("Task must be SUBMITTED before review");
        }

        task.setStatus(approve ? TaskStatus.APPROVED : TaskStatus.REJECTED);
        task.setManagerFeedback(feedback);
        task.setReviewedAt(LocalDateTime.now());

        return mapToResponse(taskRepository.save(task));
    }

    public Page<TaskResponse> getWorkerTasks(Long workerId, Pageable pageable) {
        User worker = userRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        return taskRepository.findByAssignedWorker(worker, pageable)
                .map(this::mapToResponse);
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .assignedWorkerName(task.getAssignedWorker().getFullName())
                .assignedByManagerName(task.getAssignedByManager().getFullName())
                .taskDate(task.getTaskDate())
                .workerNotes(task.getWorkerNotes())
                .managerFeedback(task.getManagerFeedback())
                .build();
    }
}
