package com.medtracker.controller;

import com.medtracker.dto.ApiResponse;
import com.medtracker.dto.MedicineDTO;
import com.medtracker.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Tag(name = "Medicine", description = "Medicine management APIs")
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    @Operation(summary = "Get all medicines")
    public ResponseEntity<ApiResponse<List<MedicineDTO>>> getAllMedicines() {
        List<MedicineDTO> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved successfully", medicines));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get medicine by ID")
    public ResponseEntity<ApiResponse<MedicineDTO>> getMedicineById(@PathVariable Long id) {
        MedicineDTO medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", medicine));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get medicine by name")
    public ResponseEntity<ApiResponse<MedicineDTO>> getMedicineByName(@PathVariable String name) {
        MedicineDTO medicine = medicineService.getMedicineByName(name);
        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", medicine));
    }

    @PostMapping
    @Operation(summary = "Create new medicine")
    public ResponseEntity<ApiResponse<MedicineDTO>> createMedicine(@RequestBody MedicineDTO medicineDTO) {
        MedicineDTO createdMedicine = medicineService.createMedicine(medicineDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medicine created successfully", createdMedicine));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update medicine")
    public ResponseEntity<ApiResponse<MedicineDTO>> updateMedicine(
            @PathVariable Long id,
            @RequestBody MedicineDTO medicineDTO) {
        MedicineDTO updatedMedicine = medicineService.updateMedicine(id, medicineDTO);
        return ResponseEntity.ok(ApiResponse.success("Medicine updated successfully", updatedMedicine));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medicine")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok(ApiResponse.success("Medicine deleted successfully", null));
    }
}
