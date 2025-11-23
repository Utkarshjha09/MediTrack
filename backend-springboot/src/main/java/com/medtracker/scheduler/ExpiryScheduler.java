package com.medtracker.scheduler;

import com.medtracker.dto.BatchDTO;
import com.medtracker.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiryScheduler {

    private final BatchService batchService;

    // Run every day at 9:00 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkExpiringBatches() {
        log.info("Starting daily expiry check...");
        
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysFromNow = today.plusDays(30);
        
        // Get batches expiring within 30 days
        List<BatchDTO> expiringBatches = batchService.getBatchesExpiringBetween(today, thirtyDaysFromNow);
        
        if (!expiringBatches.isEmpty()) {
            log.warn("Found {} batches expiring within 30 days", expiringBatches.size());
            
            for (BatchDTO batch : expiringBatches) {
                long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(today, batch.getExpiryDate());
                log.warn("ALERT: Batch {} ({}) expires in {} days on {}", 
                        batch.getBatchNumber(), 
                        batch.getMedicineName(),
                        daysUntilExpiry,
                        batch.getExpiryDate());
            }
        } else {
            log.info("No batches expiring within 30 days");
        }
    }

    // Run every Monday at 10:00 AM
    @Scheduled(cron = "0 0 10 * * MON")
    public void weeklyExpiryReport() {
        log.info("Generating weekly expiry report...");
        
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysFromNow = today.plusDays(7);
        
        List<BatchDTO> expiringBatches = batchService.getBatchesExpiringBetween(today, sevenDaysFromNow);
        
        log.info("Weekly Report: {} batches expiring within the next 7 days", expiringBatches.size());
    }
}
