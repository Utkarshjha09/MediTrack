package com.medtracker.repository;

import com.medtracker.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<BatchEntity, Long> {
    
    Optional<BatchEntity> findByBatchNumber(String batchNumber);
    
    List<BatchEntity> findByMedicineMedicineId(Long medicineId);
    
    @Query("SELECT b FROM BatchEntity b WHERE b.expiryDate <= :date AND b.quantity > 0")
    List<BatchEntity> findExpiringBatches(LocalDate date);
    
    @Query("SELECT b FROM BatchEntity b WHERE b.expiryDate BETWEEN :startDate AND :endDate AND b.quantity > 0")
    List<BatchEntity> findBatchesExpiringBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByBatchNumber(String batchNumber);
}
