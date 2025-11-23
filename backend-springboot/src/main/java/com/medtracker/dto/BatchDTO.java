package com.medtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchDTO {
    
    private Long batchId;
    private String batchNumber;
    private Long medicineId;
    private String medicineName;
    private Long slotId;
    private String slotNumber;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Integer quantity;
    private Double costPrice;
    private Double sellingPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
