package com.estapar.parking.domain.repository;

import com.estapar.parking.domain.entity.ParkingSession;

import java.util.Optional;

public interface ParkingSessionRepository {

    Optional<ParkingSession> findActiveSessionByLicensePlate(String licensePlate);

    ParkingSession save(ParkingSession parkingSession);
}
