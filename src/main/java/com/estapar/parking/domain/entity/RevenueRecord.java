package com.estapar.parking.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RevenueRecord {

    private Long id;
    private String sectorCode;
    private LocalDate revenueDate;
    private BigDecimal amount;

    public RevenueRecord(Long id, String sectorCode, LocalDate revenueDate, BigDecimal amount) {
        this.id = id;
        this.sectorCode = sectorCode;
        this.revenueDate = revenueDate;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public String getSectorCode() { return sectorCode; }
    public LocalDate getRevenueDate() { return revenueDate; }
    public BigDecimal getAmount() { return amount; }
}
