package com.medtracker.service;

import com.medtracker.dto.MedicineDTO;
import com.medtracker.entity.MedicineEntity;
import com.medtracker.exception.ResourceNotFoundException;
import com.medtracker.mapper.MedicineMapper;
import com.medtracker.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository productRepository;
    private final MedicineMapper dataTransformer;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<MedicineDTO> getAllMedicines() {
        log.debug("Initiating full catalog retrieval");
        List<MedicineEntity> catalogEntities = productRepository.findAll();
        
        return catalogEntities.stream()
                .map(dataTransformer::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    
    @Transactional(readOnly = true)
    public MedicineDTO getMedicineById(Long productId) {
        log.debug("Attempting to locate product with identifier: {}", productId);
        
        MedicineEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product lookup failed - no record found for ID: {}", productId);
                    return new ResourceNotFoundException(
                        String.format("Pharmaceutical product not found in catalog (ID: %d)", productId)
                    );
                });
        
        return dataTransformer.toDTO(productEntity);
    }

    
    @Transactional(readOnly = true)
    public MedicineDTO getMedicineByName(String productName) {
        log.debug("Executing product search for name: '{}'", productName);
        
        MedicineEntity locatedProduct = productRepository.findByMedicineName(productName)
                .orElseThrow(() -> {
                    log.warn("Name-based search yielded no results for: {}", productName);
                    return new ResourceNotFoundException(
                        String.format("No pharmaceutical product registered with name: '%s'", productName)
                    );
                });
        
        return dataTransformer.toDTO(locatedProduct);
    }

    
    @Transactional
    public MedicineDTO createMedicine(MedicineDTO productData) {
        String productName = productData.getMedicineName();
        log.info("Processing new product registration request: {}", productName);
        
        if (productRepository.existsByMedicineName(productName)) {
            log.error("Registration blocked - product already exists: {}", productName);
            throw new IllegalArgumentException(
                String.format("Cannot register duplicate product. '%s' already exists in catalog", productName)
            );
        }

        MedicineEntity newProduct = dataTransformer.toEntity(productData);
        MedicineEntity persistedProduct = productRepository.save(newProduct);
        
        log.info("Successfully registered new product '{}' with ID: {}", 
                 productName, persistedProduct.getId());
        
        return dataTransformer.toDTO(persistedProduct);
    }

    
    @Transactional
    public MedicineDTO updateMedicine(Long productId, MedicineDTO updatedData) {
        log.info("Initiating product modification for ID: {}", productId);
        
        MedicineEntity targetProduct = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Update failed - product not found: {}", productId);
                    return new ResourceNotFoundException(
                        String.format("Cannot update non-existent product (ID: %d)", productId)
                    );
                });

        
        targetProduct.setMedicineName(updatedData.getMedicineName());
        targetProduct.setType(updatedData.getType());
        targetProduct.setManufacturer(updatedData.getManufacturer());
        targetProduct.setDescription(updatedData.getDescription());
        targetProduct.setPricePerUnit(updatedData.getPricePerUnit());
        targetProduct.setStockQuantity(updatedData.getStockQuantity());
        targetProduct.setReorderLevel(updatedData.getReorderLevel());

        MedicineEntity modifiedProduct = productRepository.save(targetProduct);
        log.info("Product {} successfully updated", modifiedProduct.getMedicineName());
        
        return dataTransformer.toDTO(modifiedProduct);
    }

    
    @Transactional
    public void deleteMedicine(Long productId) {
        log.warn("Product deletion request received for ID: {}", productId);
        
        if (!productRepository.existsById(productId)) {
            log.error("Deletion failed - product not found: {}", productId);
            throw new ResourceNotFoundException(
                String.format("Cannot delete non-existent product (ID: %d)", productId)
            );
        }
        
        productRepository.deleteById(productId);
        log.info("Product successfully removed from catalog: ID {}", productId);
    }
}
