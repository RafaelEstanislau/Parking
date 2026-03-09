package com.estapar.parking.domain.exception;

public class SectorNotFoundException extends RuntimeException {

    public SectorNotFoundException(String sectorCode) {
        super("Sector not found: " + sectorCode);
    }
}
