package com.example.cms.service;

import com.example.cms.dto.request.StageRequest;
import com.example.cms.dto.response.StageResponse;
import com.example.cms.entity.Project;
import com.example.cms.entity.Stage;
import com.example.cms.enums.StageStatus;
import com.example.cms.exception.ResourceNotFoundException;
import com.example.cms.repository.ProjectRepository;
import com.example.cms.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StageService {

    private final StageRepository stageRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public StageResponse createStage(StageRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Stage stage = Stage.builder()
                .name(request.getName())
                .description(request.getDescription())
                .orderNumber(request.getOrderNumber())
                .project(project)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .stageBudget(request.getStageBudget())
                .status(StageStatus.NOT_STARTED)
                .build();

        stage = stageRepository.save(stage);
        return mapToResponse(stage);
    }

    @Transactional
    public StageResponse updateStageStatus(Long id, StageStatus status) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        stage.setStatus(status);
        stage = stageRepository.save(stage);
        return mapToResponse(stage);
    }

    @Transactional(readOnly = true)
    public StageResponse getStageById(Long id) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        return mapToResponse(stage);
    }

    private StageResponse mapToResponse(Stage stage) {
        StageResponse response = new StageResponse();
        response.setId(stage.getId());
        response.setName(stage.getName());
        response.setDescription(stage.getDescription());
        response.setOrderNumber(stage.getOrderNumber());
        response.setStatus(stage.getStatus());
        response.setProjectId(stage.getProject().getId());
        response.setStartDate(stage.getStartDate());
        response.setEndDate(stage.getEndDate());
        response.setStageBudget(stage.getStageBudget());
        return response;
    }
}
