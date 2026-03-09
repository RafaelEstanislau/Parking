package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.WebhookEventDto;
import com.estapar.parking.domain.entity.ParkingSession;
import com.estapar.parking.domain.entity.ParkingSpot;
import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.exception.ParkingSessionNotFoundException;
import com.estapar.parking.domain.exception.ParkingSpotNotFoundException;
import com.estapar.parking.domain.exception.SectorNotFoundException;
import com.estapar.parking.domain.repository.ParkingSessionRepository;
import com.estapar.parking.domain.repository.ParkingSpotRepository;
import com.estapar.parking.domain.repository.SectorRepository;
import com.estapar.parking.domain.service.PricingDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProcessParkedEventUseCase {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final SectorRepository sectorRepository;
    private final PricingDomainService pricingDomainService;

    public ProcessParkedEventUseCase(ParkingSpotRepository parkingSpotRepository,
                                     ParkingSessionRepository parkingSessionRepository,
                                     SectorRepository sectorRepository,
                                     PricingDomainService pricingDomainService) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingSessionRepository = parkingSessionRepository;
        this.sectorRepository = sectorRepository;
        this.pricingDomainService = pricingDomainService;
    }

    @Transactional
    public void execute(WebhookEventDto event) {
        ParkingSpot spot = findSpotByCoordinates(event.lat(), event.lng());
        Sector sector = findSector(spot.getSectorCode());
        ParkingSession session = findActiveSession(event.licensePlate());

        BigDecimal dynamicPrice = pricingDomainService.calculateDynamicPrice(
                sector.getBasePrice(), sector.getOccupancyRate());

        session.assignSpot(spot.getId(), sector.getSectorCode(), dynamicPrice);
        spot.occupy();
        sector.incrementOccupancy();

        parkingSessionRepository.save(session);
        parkingSpotRepository.save(spot);
        sectorRepository.save(sector);
    }

    private ParkingSpot findSpotByCoordinates(Double latitude, Double longitude) {
        return parkingSpotRepository.findByCoordinates(latitude, longitude)
                .orElseThrow(() -> new ParkingSpotNotFoundException(latitude, longitude));
    }

    private Sector findSector(String sectorCode) {
        return sectorRepository.findBySectorCode(sectorCode)
                .orElseThrow(() -> new SectorNotFoundException(sectorCode));
    }

    private ParkingSession findActiveSession(String licensePlate) {
        return parkingSessionRepository.findActiveSessionByLicensePlate(licensePlate)
                .orElseThrow(() -> new ParkingSessionNotFoundException(licensePlate));
    }
}
