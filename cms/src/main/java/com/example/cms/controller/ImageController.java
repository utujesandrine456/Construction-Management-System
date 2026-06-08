package com.example.cms.controller;

import com.example.cms.dto.response.ImageResponse;
import com.example.cms.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image Management", description = "Endpoints for managing project/stage images")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Upload an image", description = "Uploads an image for a project or stage")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "stageId", required = false) Long stageId,
            @RequestParam(value = "caption", required = false) String caption) {
        
        return ResponseEntity.ok(imageService.uploadImage(file, projectId, stageId, caption));
    }

    @Operation(summary = "Get images by project", description = "Retrieves all images associated with a project")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ImageResponse>> getImagesByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(imageService.getImagesByProject(projectId));
    }

    @Operation(summary = "Download an image", description = "Downloads or views an image by filename")
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
        try {
            Path filePath = imageService.getImagePath(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
