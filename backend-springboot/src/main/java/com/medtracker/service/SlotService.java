package com.medtracker.service;

import com.medtracker.dto.SlotDTO;
import com.medtracker.entity.SlotEntity;
import com.medtracker.exception.ResourceNotFoundException;
import com.medtracker.mapper.SlotMapper;
import com.medtracker.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final SlotMapper slotMapper;

    @Transactional(readOnly = true)
    public List<SlotDTO> getAllSlots() {
        return slotRepository.findAll()
                .stream()
                .map(slotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SlotDTO getSlotById(Long id) {
        SlotEntity slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + id));
        return slotMapper.toDTO(slot);
    }

    @Transactional(readOnly = true)
    public SlotDTO getSlotByNumber(String slotNumber) {
        SlotEntity slot = slotRepository.findBySlotNumber(slotNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with number: " + slotNumber));
        return slotMapper.toDTO(slot);
    }

    @Transactional(readOnly = true)
    public List<SlotDTO> getAvailableSlots() {
        return slotRepository.findByIsAvailable(true)
                .stream()
                .map(slotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SlotDTO> getSlotsByLocation(String location) {
        return slotRepository.findByLocation(location)
                .stream()
                .map(slotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SlotDTO createSlot(SlotDTO slotDTO) {
        if (slotRepository.existsBySlotNumber(slotDTO.getSlotNumber())) {
            throw new IllegalArgumentException("Slot already exists with number: " + slotDTO.getSlotNumber());
        }

        SlotEntity slot = slotMapper.toEntity(slotDTO);
        SlotEntity savedSlot = slotRepository.save(slot);
        return slotMapper.toDTO(savedSlot);
    }

    @Transactional
    public SlotDTO updateSlot(Long id, SlotDTO slotDTO) {
        SlotEntity existingSlot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + id));

        existingSlot.setSlotNumber(slotDTO.getSlotNumber());
        existingSlot.setLocation(slotDTO.getLocation());
        existingSlot.setCapacity(slotDTO.getCapacity());
        existingSlot.setCurrentOccupancy(slotDTO.getCurrentOccupancy());
        existingSlot.setIsAvailable(slotDTO.getIsAvailable());

        SlotEntity updatedSlot = slotRepository.save(existingSlot);
        return slotMapper.toDTO(updatedSlot);
    }

    @Transactional
    public void deleteSlot(Long id) {
        if (!slotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Slot not found with id: " + id);
        }
        slotRepository.deleteById(id);
    }
}
