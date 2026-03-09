package com.estapar.parking.domain.valueobject;

public class OccupancyRate {

    private final double rate;

    private OccupancyRate(double rate) {
        this.rate = rate;
    }

    public static OccupancyRate of(int currentOccupancy, int maxCapacity) {
        if (maxCapacity == 0) {
            return new OccupancyRate(0.0);
        }
        return new OccupancyRate((double) currentOccupancy / maxCapacity);
    }

    public boolean isBelow25Percent() {
        return rate < 0.25;
    }

    public boolean isBelow50Percent() {
        return rate < 0.50;
    }

    public boolean isBelow75Percent() {
        return rate < 0.75;
    }

    public boolean isBelow100Percent() {
        return rate < 1.0;
    }

    public double getRate() {
        return rate;
    }
}
