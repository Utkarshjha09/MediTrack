package com.medtracker.repository;

import com.medtracker.entity.SlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<SlotEntity, Long> {
    
    Optional<SlotEntity> findBySlotNumber(String slotNumber);
    
    List<SlotEntity> findByIsAvailable(Boolean isAvailable);
    
    List<SlotEntity> findByLocation(String location);
    
    boolean existsBySlotNumber(String slotNumber);
}
