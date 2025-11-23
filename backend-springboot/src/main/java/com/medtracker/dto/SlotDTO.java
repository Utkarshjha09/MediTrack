package com.medtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotDTO {
    
    private Long slotId;
    private String slotNumber;
    private String location;
    private Integer capacity;
    private Integer currentOccupancy;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
