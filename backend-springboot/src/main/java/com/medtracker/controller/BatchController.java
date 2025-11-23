package com.medtracker.controller;

import com.medtracker.dto.ApiResponse;
import com.medtracker.dto.BatchDTO;
import com.medtracker.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
@Tag(name = "Batch", description = "Batch management APIs")
public class BatchController {

    private final BatchService batchService;

    @GetMapping
    @Operation(summary = "Get all batches")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> getAllBatches() {
        List<BatchDTO> batches = batchService.getAllBatches();
        return ResponseEntity.ok(ApiResponse.success("Batches retrieved successfully", batches));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get batch by ID")
    public ResponseEntity<ApiResponse<BatchDTO>> getBatchById(@PathVariable Long id) {
        BatchDTO batch = batchService.getBatchById(id);
        return ResponseEntity.ok(ApiResponse.success("Batch retrieved successfully", batch));
    }

    @GetMapping("/number/{batchNumber}")
    @Operation(summary = "Get batch by batch number")
    public ResponseEntity<ApiResponse<BatchDTO>> getBatchByNumber(@PathVariable String batchNumber) {
        BatchDTO batch = batchService.getBatchByNumber(batchNumber);
        return ResponseEntity.ok(ApiResponse.success("Batch retrieved successfully", batch));
    }

    @GetMapping("/medicine/{medicineId}")
    @Operation(summary = "Get batches by medicine ID")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> getBatchesByMedicine(@PathVariable Long medicineId) {
        List<BatchDTO> batches = batchService.getBatchesByMedicine(medicineId);
        return ResponseEntity.ok(ApiResponse.success("Batches retrieved successfully", batches));
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get expiring batches by date")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> getExpiringBatches(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BatchDTO> batches = batchService.getExpiringBatches(date);
        return ResponseEntity.ok(ApiResponse.success("Expiring batches retrieved successfully", batches));
    }

    @GetMapping("/expiring-between")
    @Operation(summary = "Get batches expiring between dates")
    public ResponseEntity<ApiResponse<List<BatchDTO>>> getBatchesExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BatchDTO> batches = batchService.getBatchesExpiringBetween(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Batches retrieved successfully", batches));
    }

    @PostMapping
    @Operation(summary = "Create new batch")
    public ResponseEntity<ApiResponse<BatchDTO>> createBatch(@RequestBody BatchDTO batchDTO) {
        BatchDTO createdBatch = batchService.createBatch(batchDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Batch created successfully", createdBatch));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update batch")
    public ResponseEntity<ApiResponse<BatchDTO>> updateBatch(
            @PathVariable Long id,
            @RequestBody BatchDTO batchDTO) {
        BatchDTO updatedBatch = batchService.updateBatch(id, batchDTO);
        return ResponseEntity.ok(ApiResponse.success("Batch updated successfully", updatedBatch));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete batch")
    public ResponseEntity<ApiResponse<Void>> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(ApiResponse.success("Batch deleted successfully", null));
    }
}
