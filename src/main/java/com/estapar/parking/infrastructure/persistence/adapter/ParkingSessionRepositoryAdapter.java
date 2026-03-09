package com.estapar.parking.infrastructure.persistence.adapter;

import com.estapar.parking.domain.entity.ParkingSession;
import com.estapar.parking.domain.entity.SessionStatus;
import com.estapar.parking.domain.repository.ParkingSessionRepository;
import com.estapar.parking.infrastructure.persistence.entity.ParkingSessionJpaEntity;
import com.estapar.parking.infrastructure.persistence.entity.ParkingSessionJpaEntity.SessionStatusJpa;
import com.estapar.parking.infrastructure.persistence.repository.ParkingSessionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ParkingSessionRepositoryAdapter implements ParkingSessionRepository {

    private final ParkingSessionJpaRepository parkingSessionJpaRepository;

    public ParkingSessionRepositoryAdapter(ParkingSessionJpaRepository parkingSessionJpaRepository) {
        this.parkingSessionJpaRepository = parkingSessionJpaRepository;
    }

    @Override
    public Optional<ParkingSession> findActiveSessionByLicensePlate(String licensePlate) {
        List<SessionStatusJpa> activeStatuses = List.of(SessionStatusJpa.ACTIVE, SessionStatusJpa.PARKED);
        return parkingSessionJpaRepository
                .findByLicensePlateAndStatusIn(licensePlate, activeStatuses)
                .map(this::toDomain);
    }

    @Override
    public ParkingSession save(ParkingSession session) {
        ParkingSessionJpaEntity saved = parkingSessionJpaRepository.save(toJpaEntity(session));
        return toDomain(saved);
    }

    private ParkingSession toDomain(ParkingSessionJpaEntity entity) {
        return new ParkingSession(
                entity.getId(),
                entity.getLicensePlate(),
                entity.getEntryTime(),
                entity.getExitTime(),
                entity.getSectorCode(),
                entity.getParkingSpotId(),
                entity.getPricePerHour(),
                entity.getTotalAmount(),
                SessionStatus.valueOf(entity.getStatus().name())
        );
    }

    private ParkingSessionJpaEntity toJpaEntity(ParkingSession session) {
        return ParkingSessionJpaEntity.builder()
                .id(session.getId())
                .licensePlate(session.getLicensePlate())
                .entryTime(session.getEntryTime())
                .exitTime(session.getExitTime())
                .sectorCode(session.getSectorCode())
                .parkingSpotId(session.getParkingSpotId())
                .pricePerHour(session.getPricePerHour())
                .totalAmount(session.getTotalAmount())
                .status(SessionStatusJpa.valueOf(session.getStatus().name()))
                .build();
    }
}
