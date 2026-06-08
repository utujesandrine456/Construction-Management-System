package com.example.cms.service;

import com.example.cms.dto.response.ImageResponse;
import com.example.cms.entity.Project;
import com.example.cms.entity.ProjectImage;
import com.example.cms.entity.Stage;
import com.example.cms.entity.User;
import com.example.cms.exception.ResourceNotFoundException;
import com.example.cms.repository.ProjectImageRepository;
import com.example.cms.repository.ProjectRepository;
import com.example.cms.repository.StageRepository;
import com.example.cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ProjectImageRepository projectImageRepository;
    private final ProjectRepository projectRepository;
    private final StageRepository stageRepository;
    private final UserRepository userRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @jakarta.annotation.PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public ImageResponse uploadImage(MultipartFile file, Long projectId, Long stageId, String caption) {
        User currentUser = getCurrentUser();

        Project project = null;
        if (projectId != null) {
            project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        }

        Stage stage = null;
        if (stageId != null) {
            stage = stageRepository.findById(stageId)
                    .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/download/")
                .path(fileName)
                .toUriString();

        ProjectImage image = ProjectImage.builder()
                .fileName(fileName)
                .fileUrl(fileDownloadUri)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .caption(caption)
                .project(project)
                .stage(stage)
                .uploadedBy(currentUser)
                .build();

        image = projectImageRepository.save(image);
        return mapToResponse(image);
    }

    @Transactional(readOnly = true)
    public List<ImageResponse> getImagesByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return projectImageRepository.findByProject(project).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Path getImagePath(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }

    private ImageResponse mapToResponse(ProjectImage image) {
        ImageResponse response = new ImageResponse();
        response.setId(image.getId());
        response.setFileName(image.getFileName());
        response.setFileUrl(image.getFileUrl());
        response.setCaption(image.getCaption());
        response.setProjectId(image.getProject() != null ? image.getProject().getId() : null);
        response.setStageId(image.getStage() != null ? image.getStage().getId() : null);
        response.setUploadedById(image.getUploadedBy() != null ? image.getUploadedBy().getId() : null);
        response.setUploadedAt(image.getUploadedAt());
        return response;
    }
}
