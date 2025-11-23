package com.medtracker.controller;

import com.medtracker.dto.ApiResponse;
import com.medtracker.dto.SlotDTO;
import com.medtracker.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Tag(name = "Slot", description = "Slot management APIs")
public class SlotController {

    private final SlotService slotService;

    @GetMapping
    @Operation(summary = "Get all slots")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> getAllSlots() {
        List<SlotDTO> slots = slotService.getAllSlots();
        return ResponseEntity.ok(ApiResponse.success("Slots retrieved successfully", slots));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get slot by ID")
    public ResponseEntity<ApiResponse<SlotDTO>> getSlotById(@PathVariable Long id) {
        SlotDTO slot = slotService.getSlotById(id);
        return ResponseEntity.ok(ApiResponse.success("Slot retrieved successfully", slot));
    }

    @GetMapping("/number/{slotNumber}")
    @Operation(summary = "Get slot by slot number")
    public ResponseEntity<ApiResponse<SlotDTO>> getSlotByNumber(@PathVariable String slotNumber) {
        SlotDTO slot = slotService.getSlotByNumber(slotNumber);
        return ResponseEntity.ok(ApiResponse.success("Slot retrieved successfully", slot));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available slots")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> getAvailableSlots() {
        List<SlotDTO> slots = slotService.getAvailableSlots();
        return ResponseEntity.ok(ApiResponse.success("Available slots retrieved successfully", slots));
    }

    @GetMapping("/location/{location}")
    @Operation(summary = "Get slots by location")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> getSlotsByLocation(@PathVariable String location) {
        List<SlotDTO> slots = slotService.getSlotsByLocation(location);
        return ResponseEntity.ok(ApiResponse.success("Slots retrieved successfully", slots));
    }

    @PostMapping
    @Operation(summary = "Create new slot")
    public ResponseEntity<ApiResponse<SlotDTO>> createSlot(@RequestBody SlotDTO slotDTO) {
        SlotDTO createdSlot = slotService.createSlot(slotDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Slot created successfully", createdSlot));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update slot")
    public ResponseEntity<ApiResponse<SlotDTO>> updateSlot(
            @PathVariable Long id,
            @RequestBody SlotDTO slotDTO) {
        SlotDTO updatedSlot = slotService.updateSlot(id, slotDTO);
        return ResponseEntity.ok(ApiResponse.success("Slot updated successfully", updatedSlot));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete slot")
    public ResponseEntity<ApiResponse<Void>> deleteSlot(@PathVariable Long id) {
        slotService.deleteSlot(id);
        return ResponseEntity.ok(ApiResponse.success("Slot deleted successfully", null));
    }
}
