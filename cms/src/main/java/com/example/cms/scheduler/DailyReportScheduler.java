package com.example.cms.scheduler;

import com.example.cms.service.DailyReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyReportScheduler {

    private final DailyReportService dailyReportService;

    /**
     * Runs every day at 6:00 PM to automatically generate daily reports
     * for all active projects, summarising completed tasks and expenses.
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void generateDailyReports() {
        log.info(">>> Starting automated daily report generation at {}", LocalDateTime.now());
        dailyReportService.generateDailyReportsForToday();
        log.info("<<< Finished automated daily report generation at {}", LocalDateTime.now());
    }
}
