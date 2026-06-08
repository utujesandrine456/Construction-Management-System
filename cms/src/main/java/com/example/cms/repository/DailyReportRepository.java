package com.example.cms.repository;

import com.example.cms.entity.DailyReport;
import com.example.cms.entity.Project;
import com.example.cms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    List<DailyReport> findByProjectOrderByReportDateDesc(Project project);

    Optional<DailyReport> findByProjectAndReportDate(Project project, LocalDate date);

    Page<DailyReport> findByProject(Project project, Pageable pageable);

    List<DailyReport> findBySubmittedByOrderByReportDateDesc(User manager);
}
