package com.estapar.parking.domain.valueobject;

import java.util.Objects;

public class Coordinates {

    private final Double latitude;
    private final Double longitude;

    private Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinates of(Double latitude, Double longitude) {
        Objects.requireNonNull(latitude, "Latitude cannot be null");
        Objects.requireNonNull(longitude, "Longitude cannot be null");
        return new Coordinates(latitude, longitude);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Coordinates that)) return false;
        return Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
