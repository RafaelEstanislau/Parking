package com.estapar.parking.domain.exception;

public class GarageCapacityExceededException extends RuntimeException {

    public GarageCapacityExceededException(String sectorCode) {
        super("Sector " + sectorCode + " has reached maximum capacity. Entry rejected.");
    }

    public GarageCapacityExceededException() {
        super("All sectors are at full capacity. Entry rejected.");
    }
}
