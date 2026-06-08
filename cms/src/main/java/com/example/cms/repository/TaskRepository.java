package com.example.cms.repository;

import com.example.cms.entity.Project;
import com.example.cms.entity.Task;
import com.example.cms.entity.User;
import com.example.cms.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAssignedWorker(User worker, Pageable pageable);

    List<Task> findByAssignedWorkerAndTaskDate(User worker, LocalDate date);

    List<Task> findByProjectAndStatus(Project project, TaskStatus status);

    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.assignedByManager.id = :managerId " + "AND t.status = 'SUBMITTED'")
    List<Task> findSubmittedTasksForManager(@Param("managerId") Long managerId);

    Page<Task> findByProject(Project project, Pageable pageable);

    long countByProjectAndStatus(Project project, TaskStatus status);

}
