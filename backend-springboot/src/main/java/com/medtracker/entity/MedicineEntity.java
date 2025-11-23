package com.medtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

    @Column(nullable = false, unique = true)
    private String medicineName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double pricePerUnit;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Integer reorderLevel;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BatchEntity> batches;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
