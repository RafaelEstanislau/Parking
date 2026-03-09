package com.estapar.parking.domain.exception;

public class ParkingSpotNotFoundException extends RuntimeException {

    public ParkingSpotNotFoundException(Double latitude, Double longitude) {
        super("No parking spot found at coordinates: lat=" + latitude + ", lng=" + longitude);
    }

    public ParkingSpotNotFoundException(Long spotId) {
        super("No parking spot found with id: " + spotId);
    }
}
