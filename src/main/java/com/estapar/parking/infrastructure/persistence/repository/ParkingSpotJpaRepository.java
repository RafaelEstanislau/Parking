package com.estapar.parking.infrastructure.persistence.repository;

import com.estapar.parking.infrastructure.persistence.entity.ParkingSpotJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpotJpaRepository extends JpaRepository<ParkingSpotJpaEntity, Long> {

    Optional<ParkingSpotJpaEntity> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
