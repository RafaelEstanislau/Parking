package com.estapar.parking.domain.entity;

import com.estapar.parking.domain.exception.GarageCapacityExceededException;
import com.estapar.parking.domain.valueobject.OccupancyRate;

import java.math.BigDecimal;

public class Sector {

    private Long id;
    private String sectorCode;
    private BigDecimal basePrice;
    private int maxCapacity;
    private int currentOccupancy;

    public Sector(Long id, String sectorCode, BigDecimal basePrice, int maxCapacity, int currentOccupancy) {
        this.id = id;
        this.sectorCode = sectorCode;
        this.basePrice = basePrice;
        this.maxCapacity = maxCapacity;
        this.currentOccupancy = currentOccupancy;
    }

    public boolean hasAvailableCapacity() {
        return currentOccupancy < maxCapacity;
    }

    public OccupancyRate getOccupancyRate() {
        return OccupancyRate.of(currentOccupancy, maxCapacity);
    }

    public void incrementOccupancy() {
        if (currentOccupancy >= maxCapacity) {
            throw new GarageCapacityExceededException(sectorCode);
        }
        currentOccupancy++;
    }

    public void decrementOccupancy() {
        if (currentOccupancy > 0) {
            currentOccupancy--;
        }
    }

    public Long getId() { return id; }
    public String getSectorCode() { return sectorCode; }
    public BigDecimal getBasePrice() { return basePrice; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentOccupancy() { return currentOccupancy; }
}
