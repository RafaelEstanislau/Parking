package com.estapar.parking.domain.exception;

public class ParkingSessionNotFoundException extends RuntimeException {

    public ParkingSessionNotFoundException(String licensePlate) {
        super("No active parking session found for license plate: " + licensePlate);
    }
}
