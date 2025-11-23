package com.medtracker.mapper;

import com.medtracker.dto.MedicineDTO;
import com.medtracker.entity.MedicineEntity;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {

    public MedicineDTO toDTO(MedicineEntity entity) {
        if (entity == null) {
            return null;
        }

        MedicineDTO dto = new MedicineDTO();
        dto.setMedicineId(entity.getMedicineId());
        dto.setMedicineName(entity.getMedicineName());
        dto.setType(entity.getType());
        dto.setManufacturer(entity.getManufacturer());
        dto.setDescription(entity.getDescription());
        dto.setPricePerUnit(entity.getPricePerUnit());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setReorderLevel(entity.getReorderLevel());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public MedicineEntity toEntity(MedicineDTO dto) {
        if (dto == null) {
            return null;
        }

        MedicineEntity entity = new MedicineEntity();
        entity.setMedicineId(dto.getMedicineId());
        entity.setMedicineName(dto.getMedicineName());
        entity.setType(dto.getType());
        entity.setManufacturer(dto.getManufacturer());
        entity.setDescription(dto.getDescription());
        entity.setPricePerUnit(dto.getPricePerUnit());
        entity.setStockQuantity(dto.getStockQuantity());
        entity.setReorderLevel(dto.getReorderLevel());

        return entity;
    }
}
