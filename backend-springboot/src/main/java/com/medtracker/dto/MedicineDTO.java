package com.medtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDTO {
    
    private Long medicineId;
    private String medicineName;
    private String type;
    private String manufacturer;
    private String description;
    private Double pricePerUnit;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
