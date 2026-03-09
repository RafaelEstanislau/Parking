package com.estapar.parking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sectors", indexes = @Index(name = "idx_sector_code", columnList = "sector_code", unique = true))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sector_code", nullable = false, unique = true)
    private String sectorCode;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "current_occupancy", nullable = false)
    private int currentOccupancy;
}
