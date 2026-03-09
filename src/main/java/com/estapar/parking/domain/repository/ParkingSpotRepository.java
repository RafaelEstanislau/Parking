package com.estapar.parking.domain.repository;

import com.estapar.parking.domain.entity.ParkingSpot;

import java.util.Optional;

public interface ParkingSpotRepository {

    Optional<ParkingSpot> findByCoordinates(Double latitude, Double longitude);

    Optional<ParkingSpot> findById(Long id);

    ParkingSpot save(ParkingSpot parkingSpot);
}
