package com.medtracker.mapper;

import com.medtracker.dto.SlotDTO;
import com.medtracker.entity.SlotEntity;
import org.springframework.stereotype.Component;

@Component
public class SlotMapper {

    public SlotDTO toDTO(SlotEntity entity) {
        if (entity == null) {
            return null;
        }

        SlotDTO dto = new SlotDTO();
        dto.setSlotId(entity.getSlotId());
        dto.setSlotNumber(entity.getSlotNumber());
        dto.setLocation(entity.getLocation());
        dto.setCapacity(entity.getCapacity());
        dto.setCurrentOccupancy(entity.getCurrentOccupancy());
        dto.setIsAvailable(entity.getIsAvailable());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public SlotEntity toEntity(SlotDTO dto) {
        if (dto == null) {
            return null;
        }

        SlotEntity entity = new SlotEntity();
        entity.setSlotId(dto.getSlotId());
        entity.setSlotNumber(dto.getSlotNumber());
        entity.setLocation(dto.getLocation());
        entity.setCapacity(dto.getCapacity());
        entity.setCurrentOccupancy(dto.getCurrentOccupancy());
        entity.setIsAvailable(dto.getIsAvailable());

        return entity;
    }
}
