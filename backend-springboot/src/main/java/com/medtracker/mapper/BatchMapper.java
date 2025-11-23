package com.medtracker.mapper;

import com.medtracker.dto.BatchDTO;
import com.medtracker.entity.BatchEntity;
import org.springframework.stereotype.Component;

@Component
public class BatchMapper {

    public BatchDTO toDTO(BatchEntity entity) {
        if (entity == null) {
            return null;
        }

        BatchDTO dto = new BatchDTO();
        dto.setBatchId(entity.getBatchId());
        dto.setBatchNumber(entity.getBatchNumber());
        
        if (entity.getMedicine() != null) {
            dto.setMedicineId(entity.getMedicine().getMedicineId());
            dto.setMedicineName(entity.getMedicine().getMedicineName());
        }
        
        if (entity.getSlot() != null) {
            dto.setSlotId(entity.getSlot().getSlotId());
            dto.setSlotNumber(entity.getSlot().getSlotNumber());
        }
        
        dto.setManufacturingDate(entity.getManufacturingDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setQuantity(entity.getQuantity());
        dto.setCostPrice(entity.getCostPrice());
        dto.setSellingPrice(entity.getSellingPrice());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public BatchEntity toEntity(BatchDTO dto) {
        if (dto == null) {
            return null;
        }

        BatchEntity entity = new BatchEntity();
        entity.setBatchId(dto.getBatchId());
        entity.setBatchNumber(dto.getBatchNumber());
        entity.setManufacturingDate(dto.getManufacturingDate());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setQuantity(dto.getQuantity());
        entity.setCostPrice(dto.getCostPrice());
        entity.setSellingPrice(dto.getSellingPrice());

        return entity;
    }
}
