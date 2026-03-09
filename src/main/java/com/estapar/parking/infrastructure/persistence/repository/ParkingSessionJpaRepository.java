package com.estapar.parking.infrastructure.persistence.repository;

import com.estapar.parking.infrastructure.persistence.entity.ParkingSessionJpaEntity;
import com.estapar.parking.infrastructure.persistence.entity.ParkingSessionJpaEntity.SessionStatusJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionJpaRepository extends JpaRepository<ParkingSessionJpaEntity, Long> {

    Optional<ParkingSessionJpaEntity> findByLicensePlateAndStatusIn(String licensePlate, List<SessionStatusJpa> statuses);
}
