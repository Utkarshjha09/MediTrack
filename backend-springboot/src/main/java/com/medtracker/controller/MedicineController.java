package com.medtracker.controller;

import com.medtracker.dto.ApiResponse;
import com.medtracker.dto.MedicineDTO;
import com.medtracker.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Tag(name = "Pharmaceutical Products", description = "APIs for managing medicine catalog and product information")
public class MedicineController {

    private final MedicineService pharmaceuticalService;

    @GetMapping
    @Operation(summary = "Fetch complete medicine inventory",
               description = "Returns entire catalog of registered pharmaceutical products with full details")
    public ResponseEntity<ApiResponse<List<MedicineDTO>>> retrieveCompleteCatalog() {
        log.debug("Processing request for complete pharmaceutical catalog");
        List<MedicineDTO> productCatalog = pharmaceuticalService.getAllMedicines();
        log.info("Successfully retrieved {} medicine records", productCatalog.size());
        return ResponseEntity.ok(
            ApiResponse.success("Pharmaceutical catalog loaded successfully", productCatalog)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Locate medicine by unique ID",
               description = "Retrieves detailed information for a specific pharmaceutical product")
    public ResponseEntity<ApiResponse<MedicineDTO>> fetchProductByIdentifier(@PathVariable("id") Long productId) {
        log.debug("Searching for medicine with ID: {}", productId);
        MedicineDTO productDetails = pharmaceuticalService.getMedicineById(productId);
        return ResponseEntity.ok(
            ApiResponse.success("Product details located", productDetails)
        );
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Search by product name",
               description = "Finds medicine using its registered pharmaceutical name")
    public ResponseEntity<ApiResponse<MedicineDTO>> locateByProductName(@PathVariable("name") String productName) {
        log.debug("Executing name-based search for: {}", productName);
        MedicineDTO matchedProduct = pharmaceuticalService.getMedicineByName(productName);
        return ResponseEntity.ok(
            ApiResponse.success("Product match found", matchedProduct)
        );
    }

    @PostMapping
    @Operation(summary = "Register new pharmaceutical product",
               description = "Adds new medicine to inventory catalog with complete specifications")
    public ResponseEntity<ApiResponse<MedicineDTO>> registerNewProduct(
            @Valid @RequestBody MedicineDTO productData) {
        log.info("Initiating registration for new product: {}", productData.getMedicineName());
        MedicineDTO registeredProduct = pharmaceuticalService.createMedicine(productData);
        log.info("Successfully registered product with ID: {}", registeredProduct.getMedicineId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("New pharmaceutical product registered", registeredProduct));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modify pharmaceutical product details",
               description = "Updates existing medicine record with new specifications")
    public ResponseEntity<ApiResponse<MedicineDTO>> modifyProductInformation(
            @PathVariable("id") Long productId,
            @Valid @RequestBody MedicineDTO updatedData) {
        log.info("Processing update request for medicine ID: {}", productId);
        MedicineDTO modifiedProduct = pharmaceuticalService.updateMedicine(productId, updatedData);
        log.info("Successfully updated medicine: {}", modifiedProduct.getMedicineName());
        return ResponseEntity.ok(
            ApiResponse.success("Product information updated", modifiedProduct)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove pharmaceutical product",
               description = "Deletes medicine record from inventory catalog")
    public ResponseEntity<ApiResponse<Void>> removeProductFromCatalog(@PathVariable("id") Long productId) {
        log.warn("Initiating deletion of medicine ID: {}", productId);
        pharmaceuticalService.deleteMedicine(productId);
        log.info("Successfully removed medicine from catalog");
        return ResponseEntity.ok(
            ApiResponse.success("Pharmaceutical product removed from system", null)
        );
    }
}
