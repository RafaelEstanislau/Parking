package com.estapar.parking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "revenue_records",
        indexes = {
                @Index(name = "idx_revenue_sector_date", columnList = "sector_code, revenue_date")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueRecordJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sector_code", nullable = false)
    private String sectorCode;

    @Column(name = "revenue_date", nullable = false)
    private LocalDate revenueDate;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}
