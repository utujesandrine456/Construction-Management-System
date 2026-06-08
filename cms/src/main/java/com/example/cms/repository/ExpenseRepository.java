package com.example.cms.repository;

import com.example.cms.entity.Expense;
import com.example.cms.entity.Project;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByProject(Project project);

    List<Expense> findByProjectAndExpenseDateBetween(Project project, LocalDate from, LocalDate to);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.project.id = :projectId")
    Optional<BigDecimal> getTotalExpenseByProject(@Param("projectId") Long id);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e " + "WHERE e.project.id = :projectId GROUP BY e.category")
    List<Object[]> getExpenseByCategory(@Param("projectId") Long id);

    Page<Expense> findByProject(Project project, Pageable pageable);
}
