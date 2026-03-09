package com.estapar.parking.infrastructure.persistence.adapter;

import com.estapar.parking.domain.entity.ParkingSpot;
import com.estapar.parking.domain.repository.ParkingSpotRepository;
import com.estapar.parking.infrastructure.persistence.entity.ParkingSpotJpaEntity;
import com.estapar.parking.infrastructure.persistence.repository.ParkingSpotJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ParkingSpotRepositoryAdapter implements ParkingSpotRepository {

    private final ParkingSpotJpaRepository parkingSpotJpaRepository;

    public ParkingSpotRepositoryAdapter(ParkingSpotJpaRepository parkingSpotJpaRepository) {
        this.parkingSpotJpaRepository = parkingSpotJpaRepository;
    }

    @Override
    public Optional<ParkingSpot> findByCoordinates(Double latitude, Double longitude) {
        return parkingSpotJpaRepository.findByLatitudeAndLongitude(latitude, longitude).map(this::toDomain);
    }

    @Override
    public Optional<ParkingSpot> findById(Long id) {
        return parkingSpotJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public ParkingSpot save(ParkingSpot parkingSpot) {
        ParkingSpotJpaEntity saved = parkingSpotJpaRepository.save(toJpaEntity(parkingSpot));
        return toDomain(saved);
    }

    private ParkingSpot toDomain(ParkingSpotJpaEntity entity) {
        return new ParkingSpot(entity.getId(), entity.getSpotExternalId(), entity.getSectorCode(),
                entity.getLatitude(), entity.getLongitude(), entity.isOccupied());
    }

    private ParkingSpotJpaEntity toJpaEntity(ParkingSpot spot) {
        return ParkingSpotJpaEntity.builder()
                .id(spot.getId())
                .spotExternalId(spot.getSpotExternalId())
                .sectorCode(spot.getSectorCode())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .occupied(spot.isOccupied())
                .build();
    }
}
