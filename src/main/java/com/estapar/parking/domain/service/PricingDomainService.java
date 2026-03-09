package com.estapar.parking.domain.service;

import com.estapar.parking.domain.valueobject.OccupancyRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PricingDomainService {

    private static final int FREE_PARKING_MINUTES = 30;
    private static final BigDecimal DISCOUNT_MULTIPLIER = new BigDecimal("0.90");
    private static final BigDecimal NORMAL_MULTIPLIER = BigDecimal.ONE;
    private static final BigDecimal LOW_SURGE_MULTIPLIER = new BigDecimal("1.10");
    private static final BigDecimal HIGH_SURGE_MULTIPLIER = new BigDecimal("1.25");

    public BigDecimal calculateDynamicPrice(BigDecimal basePrice, OccupancyRate occupancyRate) {
        BigDecimal multiplier = resolveMultiplierForOccupancy(occupancyRate);
        return basePrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateSessionAmount(LocalDateTime entryTime, LocalDateTime exitTime, BigDecimal pricePerHour) {
        long totalMinutes = Duration.between(entryTime, exitTime).toMinutes();

        if (totalMinutes <= FREE_PARKING_MINUTES) {
            return BigDecimal.ZERO;
        }

        long billableHours = (long) Math.ceil((double) totalMinutes / 60);
        return pricePerHour.multiply(BigDecimal.valueOf(billableHours)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal resolveMultiplierForOccupancy(OccupancyRate occupancyRate) {
        if (occupancyRate.isBelow25Percent()) {
            return DISCOUNT_MULTIPLIER;
        }
        if (occupancyRate.isBelow50Percent()) {
            return NORMAL_MULTIPLIER;
        }
        if (occupancyRate.isBelow75Percent()) {
            return LOW_SURGE_MULTIPLIER;
        }
        return HIGH_SURGE_MULTIPLIER;
    }
}
