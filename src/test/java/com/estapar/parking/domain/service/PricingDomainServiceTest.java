package com.estapar.parking.domain.service;

import com.estapar.parking.domain.valueobject.OccupancyRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PricingDomainServiceTest {

    private PricingDomainService pricingDomainService;

    @BeforeEach
    void setUp() {
        pricingDomainService = new PricingDomainService();
    }

    @Test
    void shouldApply10PercentDiscountWhenOccupancyIsBelow25Percent() {
        BigDecimal basePrice = new BigDecimal("10.00");
        OccupancyRate occupancyRate = OccupancyRate.of(10, 100);

        BigDecimal dynamicPrice = pricingDomainService.calculateDynamicPrice(basePrice, occupancyRate);

        assertThat(dynamicPrice).isEqualByComparingTo(new BigDecimal("9.00"));
    }

    @Test
    void shouldApplyNormalPriceWhenOccupancyIsBetween25And50Percent() {
        BigDecimal basePrice = new BigDecimal("10.00");
        OccupancyRate occupancyRate = OccupancyRate.of(40, 100);

        BigDecimal dynamicPrice = pricingDomainService.calculateDynamicPrice(basePrice, occupancyRate);

        assertThat(dynamicPrice).isEqualByComparingTo(new BigDecimal("10.00"));
    }

    @Test
    void shouldApply10PercentSurchargeWhenOccupancyIsBetween50And75Percent() {
        BigDecimal basePrice = new BigDecimal("10.00");
        OccupancyRate occupancyRate = OccupancyRate.of(60, 100);

        BigDecimal dynamicPrice = pricingDomainService.calculateDynamicPrice(basePrice, occupancyRate);

        assertThat(dynamicPrice).isEqualByComparingTo(new BigDecimal("11.00"));
    }

    @Test
    void shouldApply25PercentSurchargeWhenOccupancyIsBetween75And100Percent() {
        BigDecimal basePrice = new BigDecimal("10.00");
        OccupancyRate occupancyRate = OccupancyRate.of(80, 100);

        BigDecimal dynamicPrice = pricingDomainService.calculateDynamicPrice(basePrice, occupancyRate);

        assertThat(dynamicPrice).isEqualByComparingTo(new BigDecimal("12.50"));
    }

    @Test
    void shouldChargeZeroWhenSessionIsWithin30Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 12, 30, 0);
        BigDecimal pricePerHour = new BigDecimal("10.00");

        BigDecimal amount = pricingDomainService.calculateSessionAmount(entryTime, exitTime, pricePerHour);

        assertThat(amount).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldChargeZeroWhenSessionIsExactly30Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 12, 30, 0);
        BigDecimal pricePerHour = new BigDecimal("10.00");

        BigDecimal amount = pricingDomainService.calculateSessionAmount(entryTime, exitTime, pricePerHour);

        assertThat(amount).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldChargeOneHourWhenSessionIsBetween31And60Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 12, 45, 0);
        BigDecimal pricePerHour = new BigDecimal("10.00");

        BigDecimal amount = pricingDomainService.calculateSessionAmount(entryTime, exitTime, pricePerHour);

        assertThat(amount).isEqualByComparingTo(new BigDecimal("10.00"));
    }

    @Test
    void shouldRoundUpToNextHourWhenSessionExceedsFullHour() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 13, 10, 0);
        BigDecimal pricePerHour = new BigDecimal("10.00");

        BigDecimal amount = pricingDomainService.calculateSessionAmount(entryTime, exitTime, pricePerHour);

        assertThat(amount).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void shouldChargeExactHoursWhenSessionIsExactMultipleOfOneHour() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 14, 0, 0);
        BigDecimal pricePerHour = new BigDecimal("10.00");

        BigDecimal amount = pricingDomainService.calculateSessionAmount(entryTime, exitTime, pricePerHour);

        assertThat(amount).isEqualByComparingTo(new BigDecimal("20.00"));
    }
}
