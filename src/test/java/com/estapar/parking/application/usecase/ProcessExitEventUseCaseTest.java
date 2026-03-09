package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.WebhookEventDto;
import com.estapar.parking.domain.entity.ParkingSession;
import com.estapar.parking.domain.entity.ParkingSpot;
import com.estapar.parking.domain.entity.RevenueRecord;
import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.entity.SessionStatus;
import com.estapar.parking.domain.exception.ParkingSessionNotFoundException;
import com.estapar.parking.domain.repository.ParkingSessionRepository;
import com.estapar.parking.domain.repository.ParkingSpotRepository;
import com.estapar.parking.domain.repository.RevenueRecordRepository;
import com.estapar.parking.domain.repository.SectorRepository;
import com.estapar.parking.domain.service.PricingDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessExitEventUseCaseTest {

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private RevenueRecordRepository revenueRecordRepository;

    private ProcessExitEventUseCase processExitEventUseCase;

    @BeforeEach
    void setUp() {
        processExitEventUseCase = new ProcessExitEventUseCase(
                parkingSessionRepository, parkingSpotRepository,
                sectorRepository, revenueRecordRepository, new PricingDomainService());
    }

    @Test
    void shouldCompleteSessionAndRecordRevenueOnExit() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 13, 10, 0);

        ParkingSession activeSession = new ParkingSession(1L, "ZUL0001", entryTime, exitTime, "A",
                10L, new BigDecimal("10.00"), null, SessionStatus.PARKED);

        ParkingSpot occupiedSpot = new ParkingSpot(10L, 1, "A", -23.561684, -46.655981, true);
        Sector sector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 60);

        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.of(activeSession));
        when(parkingSpotRepository.findById(10L)).thenReturn(Optional.of(occupiedSpot));
        when(sectorRepository.findBySectorCode("A")).thenReturn(Optional.of(sector));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(parkingSpotRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(sectorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(revenueRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "EXIT", null, exitTime, null, null);

        processExitEventUseCase.execute(event);

        ArgumentCaptor<RevenueRecord> revenueCaptor = ArgumentCaptor.forClass(RevenueRecord.class);
        verify(revenueRecordRepository).save(revenueCaptor.capture());

        RevenueRecord savedRevenue = revenueCaptor.getValue();
        assertThat(savedRevenue.getAmount()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(savedRevenue.getSectorCode()).isEqualTo("A");
    }

    @Test
    void shouldRecordZeroRevenueWhenSessionIsWithin30Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 12, 20, 0);

        ParkingSession activeSession = new ParkingSession(1L, "ZUL0001", entryTime, null, "A",
                10L, new BigDecimal("10.00"), null, SessionStatus.PARKED);

        ParkingSpot occupiedSpot = new ParkingSpot(10L, 1, "A", -23.561684, -46.655981, true);
        Sector sector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 60);

        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.of(activeSession));
        when(parkingSpotRepository.findById(10L)).thenReturn(Optional.of(occupiedSpot));
        when(sectorRepository.findBySectorCode("A")).thenReturn(Optional.of(sector));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(parkingSpotRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(sectorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(revenueRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "EXIT", null, exitTime, null, null);

        processExitEventUseCase.execute(event);

        ArgumentCaptor<RevenueRecord> revenueCaptor = ArgumentCaptor.forClass(RevenueRecord.class);
        verify(revenueRecordRepository).save(revenueCaptor.capture());

        assertThat(revenueCaptor.getValue().getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSessionExists() {
        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.empty());

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "EXIT", null,
                LocalDateTime.of(2025, 1, 1, 13, 0, 0), null, null);

        assertThatThrownBy(() -> processExitEventUseCase.execute(event))
                .isInstanceOf(ParkingSessionNotFoundException.class);

        verify(revenueRecordRepository, never()).save(any());
    }

    @Test
    void shouldDecrementSectorOccupancyOnExit() {
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 13, 0, 0);

        ParkingSession activeSession = new ParkingSession(1L, "ZUL0001", entryTime, null, "A",
                10L, new BigDecimal("10.00"), null, SessionStatus.PARKED);

        ParkingSpot occupiedSpot = new ParkingSpot(10L, 1, "A", -23.561684, -46.655981, true);
        Sector sector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 60);

        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.of(activeSession));
        when(parkingSpotRepository.findById(10L)).thenReturn(Optional.of(occupiedSpot));
        when(sectorRepository.findBySectorCode("A")).thenReturn(Optional.of(sector));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(parkingSpotRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(sectorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(revenueRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "EXIT", null, exitTime, null, null);

        processExitEventUseCase.execute(event);

        ArgumentCaptor<Sector> sectorCaptor = ArgumentCaptor.forClass(Sector.class);
        verify(sectorRepository).save(sectorCaptor.capture());

        assertThat(sectorCaptor.getValue().getCurrentOccupancy()).isEqualTo(59);
    }
}
