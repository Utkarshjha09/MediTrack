package com.medtracker.service;

import com.medtracker.dto.BatchDTO;
import com.medtracker.entity.BatchEntity;
import com.medtracker.entity.MedicineEntity;
import com.medtracker.entity.SlotEntity;
import com.medtracker.exception.ResourceNotFoundException;
import com.medtracker.mapper.BatchMapper;
import com.medtracker.repository.BatchRepository;
import com.medtracker.repository.MedicineRepository;
import com.medtracker.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;
    private final MedicineRepository medicineRepository;
    private final SlotRepository slotRepository;
    private final BatchMapper batchMapper;

    @Transactional(readOnly = true)
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(batchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BatchDTO getBatchById(Long id) {
        BatchEntity batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + id));
        return batchMapper.toDTO(batch);
    }

    @Transactional(readOnly = true)
    public BatchDTO getBatchByNumber(String batchNumber) {
        BatchEntity batch = batchRepository.findByBatchNumber(batchNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with number: " + batchNumber));
        return batchMapper.toDTO(batch);
    }

    @Transactional(readOnly = true)
    public List<BatchDTO> getBatchesByMedicine(Long medicineId) {
        return batchRepository.findByMedicineMedicineId(medicineId)
                .stream()
                .map(batchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BatchDTO> getExpiringBatches(LocalDate date) {
        return batchRepository.findExpiringBatches(date)
                .stream()
                .map(batchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BatchDTO> getBatchesExpiringBetween(LocalDate startDate, LocalDate endDate) {
        return batchRepository.findBatchesExpiringBetween(startDate, endDate)
                .stream()
                .map(batchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BatchDTO createBatch(BatchDTO batchDTO) {
        if (batchRepository.existsByBatchNumber(batchDTO.getBatchNumber())) {
            throw new IllegalArgumentException("Batch already exists with number: " + batchDTO.getBatchNumber());
        }

        BatchEntity batch = batchMapper.toEntity(batchDTO);

        
        MedicineEntity medicine = medicineRepository.findById(batchDTO.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + batchDTO.getMedicineId()));
        batch.setMedicine(medicine);

        
        if (batchDTO.getSlotId() != null) {
            SlotEntity slot = slotRepository.findById(batchDTO.getSlotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + batchDTO.getSlotId()));
            batch.setSlot(slot);
        }

        BatchEntity savedBatch = batchRepository.save(batch);
        return batchMapper.toDTO(savedBatch);
    }

    @Transactional
    public BatchDTO updateBatch(Long id, BatchDTO batchDTO) {
        BatchEntity existingBatch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + id));

        existingBatch.setBatchNumber(batchDTO.getBatchNumber());
        existingBatch.setManufacturingDate(batchDTO.getManufacturingDate());
        existingBatch.setExpiryDate(batchDTO.getExpiryDate());
        existingBatch.setQuantity(batchDTO.getQuantity());
        existingBatch.setCostPrice(batchDTO.getCostPrice());
        existingBatch.setSellingPrice(batchDTO.getSellingPrice());

        
        if (batchDTO.getMedicineId() != null && !batchDTO.getMedicineId().equals(existingBatch.getMedicine().getMedicineId())) {
            MedicineEntity medicine = medicineRepository.findById(batchDTO.getMedicineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + batchDTO.getMedicineId()));
            existingBatch.setMedicine(medicine);
        }

        
        if (batchDTO.getSlotId() != null) {
            SlotEntity slot = slotRepository.findById(batchDTO.getSlotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + batchDTO.getSlotId()));
            existingBatch.setSlot(slot);
        }

        BatchEntity updatedBatch = batchRepository.save(existingBatch);
        return batchMapper.toDTO(updatedBatch);
    }

    @Transactional
    public void deleteBatch(Long id) {
        if (!batchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Batch not found with id: " + id);
        }
        batchRepository.deleteById(id);
    }
}
