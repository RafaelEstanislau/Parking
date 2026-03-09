package com.estapar.parking.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OccupancyRateTest {

    @Test
    void shouldBeBelow25PercentWhenOccupancyIsLow() {
        OccupancyRate rate = OccupancyRate.of(20, 100);

        assertThat(rate.isBelow25Percent()).isTrue();
        assertThat(rate.isBelow50Percent()).isTrue();
        assertThat(rate.isBelow75Percent()).isTrue();
        assertThat(rate.isBelow100Percent()).isTrue();
    }

    @Test
    void shouldBeBelow50PercentWhenOccupancyIsModerate() {
        OccupancyRate rate = OccupancyRate.of(40, 100);

        assertThat(rate.isBelow25Percent()).isFalse();
        assertThat(rate.isBelow50Percent()).isTrue();
        assertThat(rate.isBelow75Percent()).isTrue();
        assertThat(rate.isBelow100Percent()).isTrue();
    }

    @Test
    void shouldBeBelow75PercentWhenOccupancyIsHigh() {
        OccupancyRate rate = OccupancyRate.of(60, 100);

        assertThat(rate.isBelow25Percent()).isFalse();
        assertThat(rate.isBelow50Percent()).isFalse();
        assertThat(rate.isBelow75Percent()).isTrue();
        assertThat(rate.isBelow100Percent()).isTrue();
    }

    @Test
    void shouldBeBelow100PercentWhenNearlyFull() {
        OccupancyRate rate = OccupancyRate.of(90, 100);

        assertThat(rate.isBelow25Percent()).isFalse();
        assertThat(rate.isBelow50Percent()).isFalse();
        assertThat(rate.isBelow75Percent()).isFalse();
        assertThat(rate.isBelow100Percent()).isTrue();
    }

    @Test
    void shouldReturnZeroRateWhenMaxCapacityIsZero() {
        OccupancyRate rate = OccupancyRate.of(0, 0);

        assertThat(rate.isBelow25Percent()).isTrue();
    }

    @Test
    void shouldReachFullCapacityAt100Percent() {
        OccupancyRate rate = OccupancyRate.of(100, 100);

        assertThat(rate.isBelow100Percent()).isFalse();
    }
}
