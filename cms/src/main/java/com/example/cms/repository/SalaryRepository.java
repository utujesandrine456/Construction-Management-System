package com.example.cms.repository;

import com.example.cms.entity.SalaryPayment;
import com.example.cms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryPayment, Long> {
    List<SalaryPayment> findByWorker(User worker);
}
