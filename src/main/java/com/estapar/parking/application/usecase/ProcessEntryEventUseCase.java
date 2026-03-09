package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.WebhookEventDto;
import com.estapar.parking.domain.entity.ParkingSession;
import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.entity.SessionStatus;
import com.estapar.parking.domain.exception.GarageCapacityExceededException;
import com.estapar.parking.domain.repository.ParkingSessionRepository;
import com.estapar.parking.domain.repository.SectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcessEntryEventUseCase {

    private final SectorRepository sectorRepository;
    private final ParkingSessionRepository parkingSessionRepository;

    public ProcessEntryEventUseCase(SectorRepository sectorRepository,
                                    ParkingSessionRepository parkingSessionRepository) {
        this.sectorRepository = sectorRepository;
        this.parkingSessionRepository = parkingSessionRepository;
    }

    @Transactional
    public void execute(WebhookEventDto event) {
        ensureGarageHasCapacity();
        ParkingSession session = new ParkingSession(null, event.licensePlate(), event.entryTime(), SessionStatus.ACTIVE);
        parkingSessionRepository.save(session);
    }

    private void ensureGarageHasCapacity() {
        List<Sector> allSectors = sectorRepository.findAll();
        boolean anyAvailable = allSectors.stream().anyMatch(Sector::hasAvailableCapacity);
        if (!anyAvailable) {
            throw new GarageCapacityExceededException();
        }
    }
}
