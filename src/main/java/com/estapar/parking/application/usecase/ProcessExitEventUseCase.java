package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.WebhookEventDto;
import com.estapar.parking.domain.entity.ParkingSession;
import com.estapar.parking.domain.entity.ParkingSpot;
import com.estapar.parking.domain.entity.RevenueRecord;
import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.exception.ParkingSessionNotFoundException;
import com.estapar.parking.domain.exception.ParkingSpotNotFoundException;
import com.estapar.parking.domain.exception.SectorNotFoundException;
import com.estapar.parking.domain.repository.ParkingSessionRepository;
import com.estapar.parking.domain.repository.ParkingSpotRepository;
import com.estapar.parking.domain.repository.RevenueRecordRepository;
import com.estapar.parking.domain.repository.SectorRepository;
import com.estapar.parking.domain.service.PricingDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProcessExitEventUseCase {

    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final SectorRepository sectorRepository;
    private final RevenueRecordRepository revenueRecordRepository;
    private final PricingDomainService pricingDomainService;

    public ProcessExitEventUseCase(ParkingSessionRepository parkingSessionRepository,
                                   ParkingSpotRepository parkingSpotRepository,
                                   SectorRepository sectorRepository,
                                   RevenueRecordRepository revenueRecordRepository,
                                   PricingDomainService pricingDomainService) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.sectorRepository = sectorRepository;
        this.revenueRecordRepository = revenueRecordRepository;
        this.pricingDomainService = pricingDomainService;
    }

    @Transactional
    public void execute(WebhookEventDto event) {
        ParkingSession session = findActiveSession(event.licensePlate());
        BigDecimal totalAmount = pricingDomainService.calculateSessionAmount(
                session.getEntryTime(), event.exitTime(), session.getPricePerHour());

        session.complete(event.exitTime(), totalAmount);
        parkingSessionRepository.save(session);

        releaseOccupiedSpot(session);
        decrementSectorOccupancy(session.getSectorCode());
        recordRevenue(session.getSectorCode(), event, totalAmount);
    }

    private ParkingSession findActiveSession(String licensePlate) {
        return parkingSessionRepository.findActiveSessionByLicensePlate(licensePlate)
                .orElseThrow(() -> new ParkingSessionNotFoundException(licensePlate));
    }

    private void releaseOccupiedSpot(ParkingSession session) {
        if (session.getParkingSpotId() == null) {
            return;
        }
        ParkingSpot spot = parkingSpotRepository.findById(session.getParkingSpotId())
                .orElseThrow(() -> new ParkingSpotNotFoundException(session.getParkingSpotId()));
        spot.release();
        parkingSpotRepository.save(spot);
    }

    private void decrementSectorOccupancy(String sectorCode) {
        if (sectorCode == null) {
            return;
        }
        Sector sector = sectorRepository.findBySectorCode(sectorCode)
                .orElseThrow(() -> new SectorNotFoundException(sectorCode));
        sector.decrementOccupancy();
        sectorRepository.save(sector);
    }

    private void recordRevenue(String sectorCode, WebhookEventDto event, BigDecimal amount) {
        if (sectorCode == null) {
            return;
        }
        RevenueRecord revenueRecord = new RevenueRecord(null, sectorCode, event.exitTime().toLocalDate(), amount);
        revenueRecordRepository.save(revenueRecord);
    }
}
