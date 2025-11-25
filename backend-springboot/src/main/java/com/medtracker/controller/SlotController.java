package com.medtracker.controller;

import com.medtracker.dto.ApiResponse;
import com.medtracker.dto.SlotDTO;
import com.medtracker.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Tag(name = "Warehouse Storage Slots", description = "APIs for managing pharmaceutical storage locations and warehouse organization")
public class SlotController {

    private final SlotService warehouseService;

    @GetMapping
    @Operation(summary = "Fetch complete storage slot catalog",
               description = "Returns comprehensive list of all warehouse storage positions with availability status")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> retrieveCompleteSlotCatalog() {
        log.debug("Compiling complete warehouse storage catalog");
        List<SlotDTO> storageCatalog = warehouseService.getAllSlots();
        log.info("Retrieved {} storage slot records", storageCatalog.size());
        return ResponseEntity.ok(
            ApiResponse.success("Storage catalog compiled", storageCatalog)
        );
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve storage slot by ID",
               description = "Fetches detailed information for specific warehouse storage position")
    public ResponseEntity<ApiResponse<SlotDTO>> locateSlotByIdentifier(
            @Parameter(description = "Unique slot database ID") @PathVariable("id") Long slotId) {
        log.debug("Searching for storage slot with ID: {}", slotId);
        SlotDTO locatedSlot = warehouseService.getSlotById(slotId);
        return ResponseEntity.ok(
            ApiResponse.success("Storage slot details retrieved", locatedSlot)
        );
    }

    
    @GetMapping("/number/{slotNumber}")
    @Operation(summary = "Locate slot by warehouse number",
               description = "Searches storage locations using assigned warehouse position identifier")
    public ResponseEntity<ApiResponse<SlotDTO>> searchByWarehouseNumber(
            @Parameter(description = "Warehouse position number") @PathVariable("slotNumber") String locationNumber) {
        log.debug("Executing slot search for number: {}", locationNumber);
        SlotDTO matchedSlot = warehouseService.getSlotByNumber(locationNumber);
        return ResponseEntity.ok(
            ApiResponse.success("Storage slot located", matchedSlot)
        );
    }

    
    @GetMapping("/available")
    @Operation(summary = "Monitor available storage capacity",
               description = "Returns all vacant storage slots ready for pharmaceutical inventory placement")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> identifyVacantStoragePositions() {
        log.debug("Scanning for available storage capacity");
        List<SlotDTO> availableSlots = warehouseService.getAvailableSlots();
        log.info("Identified {} available storage positions", availableSlots.size());
        return ResponseEntity.ok(
            ApiResponse.success("Warehouse capacity analysis complete", availableSlots)
        );
    }

    
    @GetMapping("/location/{location}")
    @Operation(summary = "Get slots by warehouse zone",
               description = "Returns all storage positions within specified warehouse location or section")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> fetchSlotsByWarehouseZone(
            @Parameter(description = "Warehouse zone/location identifier") @PathVariable("location") String warehouseZone) {
        log.debug("Retrieving slots for warehouse zone: {}", warehouseZone);
        List<SlotDTO> zoneSlots = warehouseService.getSlotsByLocation(warehouseZone);
        log.info("Found {} slots in zone: {}", zoneSlots.size(), warehouseZone);
        return ResponseEntity.ok(
            ApiResponse.success("Zone-specific slots retrieved", zoneSlots)
        );
    }

    
    @PostMapping
    @Operation(summary = "Register new warehouse storage slot",
               description = "Creates new storage position entry for warehouse inventory management")
    public ResponseEntity<ApiResponse<SlotDTO>> registerNewStorageSlot(
            @Valid @RequestBody SlotDTO slotData) {
        log.info("Registering new storage slot: {}", slotData.getSlotNumber());
        SlotDTO registeredSlot = warehouseService.createSlot(slotData);
        log.info("Successfully registered storage slot ID: {} ({})", 
                 registeredSlot.getId(), registeredSlot.getSlotNumber());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Storage slot registered", registeredSlot));
    }

    
    @PutMapping("/{id}")
    @Operation(summary = "Update storage slot details",
               description = "Modifies existing warehouse storage position with new specifications")
    public ResponseEntity<ApiResponse<SlotDTO>> modifyStorageSlotData(
            @PathVariable("id") Long slotId,
            @Valid @RequestBody SlotDTO modifiedData) {
        log.info("Updating storage slot: ID {}", slotId);
        SlotDTO updatedSlot = warehouseService.updateSlot(slotId, modifiedData);
        log.info("Storage slot {} successfully updated", updatedSlot.getSlotNumber());
        return ResponseEntity.ok(
            ApiResponse.success("Slot information updated", updatedSlot)
        );
    }

    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove storage slot from system",
               description = "Deletes warehouse storage position from tracking system")
    public ResponseEntity<ApiResponse<Void>> removeStorageSlotFromSystem(
            @PathVariable("id") Long slotId) {
        log.warn("Deleting storage slot: ID {}", slotId);
        warehouseService.deleteSlot(slotId);
        log.info("Storage slot successfully removed");
        return ResponseEntity.ok(
            ApiResponse.success("Storage slot removed from warehouse system", null)
        );
    }
}
