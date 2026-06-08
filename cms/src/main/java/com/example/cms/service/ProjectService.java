package com.example.cms.service;

import com.example.cms.dto.request.ProjectRequest;
import com.example.cms.dto.response.ProjectResponse;
import com.example.cms.entity.Project;
import com.example.cms.entity.User;
import com.example.cms.enums.Role;
import com.example.cms.exception.ResourceNotFoundException;
import com.example.cms.repository.ProjectRepository;
import com.example.cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        User owner = getCurrentUser();
        if (owner.getRole() != Role.OWNER) {
            throw new RuntimeException("Only owners can create projects");
        }

        User manager = null;
        if (request.getManagerId() != null) {
            manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
            if (manager.getRole() != Role.MANAGER) {
                throw new RuntimeException("Assigned user is not a manager");
            }
        }

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .totalBudget(request.getTotalBudget())
                .owner(owner)
                .manager(manager)
                .expectedEndDate(request.getExpectedEndDate())
                .build();

        project = projectRepository.save(project);
        return mapToResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        User user = getCurrentUser();
        List<Project> projects;

        if (user.getRole() == Role.OWNER) {
            projects = projectRepository.findByOwner(user);
        } else if (user.getRole() == Role.MANAGER) {
            projects = projectRepository.findByManager(user);
        } else {
            throw new RuntimeException("Unauthorized to view projects");
        }

        return projects.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return mapToResponse(project);
    }

    private ProjectResponse mapToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setLocation(project.getLocation());
        response.setTotalBudget(project.getTotalBudget());
        response.setSpentAmount(project.getSpentAmount());
        response.setOverallStatus(project.getOverallStatus());
        response.setOwnerId(project.getOwner() != null ? project.getOwner().getId() : null);
        response.setManagerId(project.getManager() != null ? project.getManager().getId() : null);
        response.setStartDate(project.getStartDate());
        response.setExpectedEndDate(project.getExpectedEndDate());
        response.setActualEndDate(project.getActualEndDate());
        return response;
    }
}
