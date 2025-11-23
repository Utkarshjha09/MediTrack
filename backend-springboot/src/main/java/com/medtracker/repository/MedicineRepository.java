package com.medtracker.repository;

import com.medtracker.entity.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, Long> {
    
    Optional<MedicineEntity> findByMedicineName(String medicineName);
    
    boolean existsByMedicineName(String medicineName);
}
