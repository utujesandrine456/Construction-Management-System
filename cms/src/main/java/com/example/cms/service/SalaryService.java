package com.example.cms.service;

import com.example.cms.dto.request.SalaryRequest;
import com.example.cms.dto.response.SalaryResponse;
import com.example.cms.entity.SalaryPayment;
import com.example.cms.entity.User;
import com.example.cms.enums.Role;
import com.example.cms.exception.ResourceNotFoundException;
import com.example.cms.repository.SalaryRepository;
import com.example.cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public SalaryResponse recordSalaryPayment(SalaryRequest request) {
        User currentUser = getCurrentUser();

        User worker = userRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (worker.getRole() != Role.WORKER) {
            throw new RuntimeException("Salary can only be paid to WORKERs");
        }

        SalaryPayment payment = SalaryPayment.builder()
                .worker(worker)
                .recordedBy(currentUser)
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .description(request.getDescription())
                .build();

        payment = salaryRepository.save(payment);
        return mapToResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<SalaryResponse> getSalariesByWorker(Long workerId) {
        User worker = userRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
        return salaryRepository.findByWorker(worker).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SalaryResponse mapToResponse(SalaryPayment payment) {
        SalaryResponse response = new SalaryResponse();
        response.setId(payment.getId());
        response.setWorkerId(payment.getWorker().getId());
        response.setRecordedById(payment.getRecordedBy() != null ? payment.getRecordedBy().getId() : null);
        response.setAmount(payment.getAmount());
        response.setPaymentDate(payment.getPaymentDate());
        response.setDescription(payment.getDescription());
        return response;
    }
}
