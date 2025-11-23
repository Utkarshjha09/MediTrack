package com.medtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedTrackerApplication.class, args);
    }
}
