package com.medtracker.service;

import com.medtracker.dto.MedicineDTO;
import com.medtracker.entity.MedicineEntity;
import com.medtracker.exception.ResourceNotFoundException;
import com.medtracker.mapper.MedicineMapper;
import com.medtracker.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Transactional(readOnly = true)
    public List<MedicineDTO> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(medicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicineDTO getMedicineById(Long id) {
        MedicineEntity medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return medicineMapper.toDTO(medicine);
    }

    @Transactional(readOnly = true)
    public MedicineDTO getMedicineByName(String name) {
        MedicineEntity medicine = medicineRepository.findByMedicineName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with name: " + name));
        return medicineMapper.toDTO(medicine);
    }

    @Transactional
    public MedicineDTO createMedicine(MedicineDTO medicineDTO) {
        if (medicineRepository.existsByMedicineName(medicineDTO.getMedicineName())) {
            throw new IllegalArgumentException("Medicine already exists with name: " + medicineDTO.getMedicineName());
        }

        MedicineEntity medicine = medicineMapper.toEntity(medicineDTO);
        MedicineEntity savedMedicine = medicineRepository.save(medicine);
        return medicineMapper.toDTO(savedMedicine);
    }

    @Transactional
    public MedicineDTO updateMedicine(Long id, MedicineDTO medicineDTO) {
        MedicineEntity existingMedicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        existingMedicine.setMedicineName(medicineDTO.getMedicineName());
        existingMedicine.setType(medicineDTO.getType());
        existingMedicine.setManufacturer(medicineDTO.getManufacturer());
        existingMedicine.setDescription(medicineDTO.getDescription());
        existingMedicine.setPricePerUnit(medicineDTO.getPricePerUnit());
        existingMedicine.setStockQuantity(medicineDTO.getStockQuantity());
        existingMedicine.setReorderLevel(medicineDTO.getReorderLevel());

        MedicineEntity updatedMedicine = medicineRepository.save(existingMedicine);
        return medicineMapper.toDTO(updatedMedicine);
    }

    @Transactional
    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicine not found with id: " + id);
        }
        medicineRepository.deleteById(id);
    }
}
