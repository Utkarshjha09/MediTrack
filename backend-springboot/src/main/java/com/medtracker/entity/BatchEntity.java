package com.medtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @Column(nullable = false, unique = true)
    private String batchNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private MedicineEntity medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private SlotEntity slot;

    @Column(nullable = false)
    private LocalDate manufacturingDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double costPrice;

    @Column(nullable = false)
    private Double sellingPrice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
