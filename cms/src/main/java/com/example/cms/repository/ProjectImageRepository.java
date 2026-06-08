package com.example.cms.repository;

import com.example.cms.entity.Project;
import com.example.cms.entity.ProjectImage;
import com.example.cms.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    List<ProjectImage> findByProject(Project project);
    List<ProjectImage> findByStage(Stage stage);
}
