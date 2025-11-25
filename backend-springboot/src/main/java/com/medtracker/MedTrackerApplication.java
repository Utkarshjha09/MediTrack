package com.medtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;

@SpringBootApplication
@EnableScheduling
public class MedTrackerApplication {

    private static final Logger log = LoggerFactory.getLogger(MedTrackerApplication.class);

    public static void main(String[] args) {
        log.info("╔═══════════════════════════════════════════════════════════╗");
        log.info("║     MedTracker - Pharmaceutical Management System        ║");
        log.info("║     Initializing Medicine Inventory & Expiry Monitor     ║");
        log.info("╚═══════════════════════════════════════════════════════════╝");
        
        SpringApplication application = new SpringApplication(MedTrackerApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        
        log.info("✓ MedTracker system operational - API endpoints active");
        log.info("✓ Scheduled expiry monitoring enabled");
    }
}
