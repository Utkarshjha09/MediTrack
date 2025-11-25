package com.medtracker.controller;

import com.medtracker.dto.ApiResponse;
import com.medtracker.dto.BatchDTO;
import com.medtracker.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
@Tag(name = "Product Batch Management", description = "Comprehensive APIs for pharmaceutical batch tracking and expiry monitoring")
public class BatchController {

    private final BatchService batchManagementService;

    @GetMapping
    @Operation(summary = "Fetch complete batch inventory",
               description = "Returns comprehensive list of all registered pharmaceutical batches with expiry details")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> fetchCompleteInventory() {
        log.debug("Processing request for complete batch inventory");
        List<BatchDTO> inventoryBatches = batchManagementService.getAllBatches();
        log.info("Retrieved {} batch records from inventory", inventoryBatches.size());
        return ResponseEntity.ok(
            ApiResponse.success("Batch inventory compiled successfully", inventoryBatches)
        );
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve batch by unique ID",
               description = "Fetches detailed information for specific pharmaceutical batch")
    public ResponseEntity<ApiResponse<BatchDTO>> locateBatchByIdentifier(
            @Parameter(description = "Unique batch database ID") @PathVariable("id") Long batchId) {
        log.debug("Searching for batch with ID: {}", batchId);
        BatchDTO locatedBatch = batchManagementService.getBatchById(batchId);
        return ResponseEntity.ok(
            ApiResponse.success("Batch details retrieved", locatedBatch)
        );
    }

    
    @GetMapping("/number/{batchNumber}")
    @Operation(summary = "Locate batch by manufacturing number",
               description = "Searches inventory using pharmaceutical batch production identifier")
    public ResponseEntity<ApiResponse<BatchDTO>> searchByManufactureNumber(
            @Parameter(description = "Production batch number") @PathVariable("batchNumber") String manufactureNumber) {
        log.debug("Executing batch search for number: {}", manufactureNumber);
        BatchDTO matchedBatch = batchManagementService.getBatchByNumber(manufactureNumber);
        return ResponseEntity.ok(
            ApiResponse.success("Batch located successfully", matchedBatch)
        );
    }

    
    @GetMapping("/medicine/{medicineId}")
    @Operation(summary = "Get batches for specific medicine",
               description = "Returns all batch records associated with particular pharmaceutical product")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> fetchBatchesByPharmaceuticalProduct(
            @Parameter(description = "Medicine product ID") @PathVariable Long medicineId) {
        log.debug("Retrieving batches for medicine ID: {}", medicineId);
        List<BatchDTO> productBatches = batchManagementService.getBatchesByMedicine(medicineId);
        log.info("Found {} batches for medicine ID: {}", productBatches.size(), medicineId);
        return ResponseEntity.ok(
            ApiResponse.success("Medicine-specific batches retrieved", productBatches)
        );
    }

    
    @GetMapping("/expiring")
    @Operation(summary = "Monitor batches nearing expiry",
               description = "Retrieves batches expiring on or before specified date for proactive inventory management")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> monitorExpiringInventory(
            @Parameter(description = "Expiry date threshold (ISO format: YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryThreshold) {
        log.info("Scanning for batches expiring by: {}", expiryThreshold);
        List<BatchDTO> expiringBatches = batchManagementService.getExpiringBatches(expiryThreshold);
        log.warn("Identified {} batches approaching expiry", expiringBatches.size());
        return ResponseEntity.ok(
            ApiResponse.success("Expiry analysis completed", expiringBatches)
        );
    }

    
    @GetMapping("/expiring-between")
    @Operation(summary = "Analyze expiry within date range",
               description = "Identifies batches with expiry dates falling between specified start and end dates")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> analyzeDateRangeExpiry(
            @Parameter(description = "Range start date (ISO format)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeStart,
            @Parameter(description = "Range end date (ISO format)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangeEnd) {
        log.info("Analyzing batch expiry for range: {} to {}", rangeStart, rangeEnd);
        List<BatchDTO> rangedBatches = batchManagementService.getBatchesExpiringBetween(rangeStart, rangeEnd);
        log.info("Located {} batches within expiry range", rangedBatches.size());
        return ResponseEntity.ok(
            ApiResponse.success("Date range expiry analysis complete", rangedBatches)
        );
    }

    
    @PostMapping
    @Operation(summary = "Register new pharmaceutical batch",
               description = "Creates new batch entry with expiry tracking and medicine association")
    public ResponseEntity<ApiResponse<BatchDTO>> registerNewBatch(
            @Valid @RequestBody BatchDTO batchData) {
        log.info("Processing new batch registration: {}", batchData.getBatchNumber());
        BatchDTO registeredBatch = batchManagementService.createBatch(batchData);
        log.info("Successfully registered batch ID: {} ({})", 
                 registeredBatch.getId(), registeredBatch.getBatchNumber());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Batch registered in inventory", registeredBatch));
    }

    
    @PutMapping("/{id}")
    @Operation(summary = "Update batch information",
               description = "Modifies existing batch record with new data including expiry dates")
    public ResponseEntity<ApiResponse<BatchDTO>> modifyBatchRecord(
            @PathVariable("id") Long batchId,
            @Valid @RequestBody BatchDTO modifiedData) {
        log.info("Updating batch record: ID {}", batchId);
        BatchDTO updatedBatch = batchManagementService.updateBatch(batchId, modifiedData);
        log.info("Batch {} successfully updated", updatedBatch.getBatchNumber());
        return ResponseEntity.ok(
            ApiResponse.success("Batch information updated", updatedBatch)
        );
    }

    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove batch from inventory",
               description = "Deletes batch record from tracking system")
    public ResponseEntity<ApiResponse<Void>> removeBatchFromInventory(
            @PathVariable("id") Long batchId) {
        log.warn("Deleting batch from inventory: ID {}", batchId);
        batchManagementService.deleteBatch(batchId);
        log.info("Batch successfully removed from system");
        return ResponseEntity.ok(
            ApiResponse.success("Batch removed from inventory", null)
        );
    }
}
