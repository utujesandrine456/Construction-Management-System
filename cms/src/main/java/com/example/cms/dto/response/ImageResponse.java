package com.example.cms.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ImageResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String caption;
    private Long projectId;
    private Long stageId;
    private Long uploadedById;
    private LocalDateTime uploadedAt;
}
