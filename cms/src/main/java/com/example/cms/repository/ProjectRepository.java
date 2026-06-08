package com.example.cms.repository;

import com.example.cms.entity.Project;
import com.example.cms.entity.User;
import com.example.cms.enums.StageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwner(User owner);
    List<Project> findByManager(User manager);

    @Query("SELECT p FROM Project p WHERE p.owner.id = :ownerId" +  " AND p.overallStatus = :status")
    List<Project> findByOwnerIdAndStatus(@Param("ownerId") Long ownerId, @Param("status") StageStatus status);

    Page<Project> findByOwner(User owner, Pageable pageable);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.projectId = :projectId")
    Optional<BigDecimal> sumExpensesByProject(@Param("projectId") Long projectId);
}
