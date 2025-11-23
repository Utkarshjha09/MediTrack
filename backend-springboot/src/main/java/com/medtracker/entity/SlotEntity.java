package com.medtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @Column(nullable = false, unique = true)
    private String slotNumber;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer currentOccupancy;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BatchEntity> batches;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isAvailable == null) {
            isAvailable = true;
        }
        if (currentOccupancy == null) {
            currentOccupancy = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
