package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.WebhookEventDto;
import com.estapar.parking.domain.entity.ParkingSession;
import com.estapar.parking.domain.entity.ParkingSpot;
import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.entity.SessionStatus;
import com.estapar.parking.domain.exception.ParkingSessionNotFoundException;
import com.estapar.parking.domain.exception.ParkingSpotNotFoundException;
import com.estapar.parking.domain.repository.ParkingSessionRepository;
import com.estapar.parking.domain.repository.ParkingSpotRepository;
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
class ProcessParkedEventUseCaseTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Mock
    private SectorRepository sectorRepository;

    private ProcessParkedEventUseCase processParkedEventUseCase;

    @BeforeEach
    void setUp() {
        processParkedEventUseCase = new ProcessParkedEventUseCase(
                parkingSpotRepository, parkingSessionRepository,
                sectorRepository, new PricingDomainService());
    }

    @Test
    void shouldAssignSpotToSessionAndIncrementOccupancy() {
        ParkingSpot availableSpot = new ParkingSpot(10L, 1, "A", -23.561684, -46.655981, false);
        Sector sector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 10);
        ParkingSession activeSession = new ParkingSession(1L, "ZUL0001",
                LocalDateTime.of(2025, 1, 1, 12, 0, 0), SessionStatus.ACTIVE);

        when(parkingSpotRepository.findByCoordinates(-23.561684, -46.655981)).thenReturn(Optional.of(availableSpot));
        when(sectorRepository.findBySectorCode("A")).thenReturn(Optional.of(sector));
        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.of(activeSession));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(parkingSpotRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(sectorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "PARKED", null, null, -23.561684, -46.655981);

        processParkedEventUseCase.execute(event);

        ArgumentCaptor<Sector> sectorCaptor = ArgumentCaptor.forClass(Sector.class);
        verify(sectorRepository).save(sectorCaptor.capture());
        assertThat(sectorCaptor.getValue().getCurrentOccupancy()).isEqualTo(11);

        ArgumentCaptor<ParkingSpot> spotCaptor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotRepository).save(spotCaptor.capture());
        assertThat(spotCaptor.getValue().isOccupied()).isTrue();
    }

    @Test
    void shouldApplyDiscountPriceWhenOccupancyIsBelow25Percent() {
        ParkingSpot availableSpot = new ParkingSpot(10L, 1, "A", -23.561684, -46.655981, false);
        Sector sector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 10);
        ParkingSession activeSession = new ParkingSession(1L, "ZUL0001",
                LocalDateTime.of(2025, 1, 1, 12, 0, 0), SessionStatus.ACTIVE);

        when(parkingSpotRepository.findByCoordinates(-23.561684, -46.655981)).thenReturn(Optional.of(availableSpot));
        when(sectorRepository.findBySectorCode("A")).thenReturn(Optional.of(sector));
        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.of(activeSession));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(parkingSpotRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(sectorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "PARKED", null, null, -23.561684, -46.655981);

        processParkedEventUseCase.execute(event);

        ArgumentCaptor<ParkingSession> sessionCaptor = ArgumentCaptor.forClass(ParkingSession.class);
        verify(parkingSessionRepository).save(sessionCaptor.capture());

        assertThat(sessionCaptor.getValue().getPricePerHour()).isEqualByComparingTo(new BigDecimal("9.00"));
    }

    @Test
    void shouldApplySurgePriceWhenOccupancyIsAbove75Percent() {
        ParkingSpot availableSpot = new ParkingSpot(10L, 1, "A", -23.561684, -46.655981, false);
        Sector sectorHighOccupancy = new Sector(1L, "A", new BigDecimal("10.00"), 100, 80);
        ParkingSession activeSession = new ParkingSession(1L, "ZUL0001",
                LocalDateTime.of(2025, 1, 1, 12, 0, 0), SessionStatus.ACTIVE);

        when(parkingSpotRepository.findByCoordinates(-23.561684, -46.655981)).thenReturn(Optional.of(availableSpot));
        when(sectorRepository.findBySectorCode("A")).thenReturn(Optional.of(sectorHighOccupancy));
        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.of(activeSession));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(parkingSpotRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(sectorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "PARKED", null, null, -23.561684, -46.655981);

        processParkedEventUseCase.execute(event);

        ArgumentCaptor<ParkingSession> sessionCaptor = ArgumentCaptor.forClass(ParkingSession.class);
        verify(parkingSessionRepository).save(sessionCaptor.capture());

        assertThat(sessionCaptor.getValue().getPricePerHour()).isEqualByComparingTo(new BigDecimal("12.50"));
    }

    @Test
    void shouldThrowExceptionWhenSpotNotFound() {
        when(parkingSpotRepository.findByCoordinates(-23.561684, -46.655981)).thenReturn(Optional.empty());

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "PARKED", null, null, -23.561684, -46.655981);

        assertThatThrownBy(() -> processParkedEventUseCase.execute(event))
                .isInstanceOf(ParkingSpotNotFoundException.class);

        verify(parkingSessionRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSessionExists() {
        ParkingSpot availableSpot = new ParkingSpot(10L, 1, "A", -23.561684, -46.655981, false);
        Sector sector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 10);

        when(parkingSpotRepository.findByCoordinates(-23.561684, -46.655981)).thenReturn(Optional.of(availableSpot));
        when(sectorRepository.findBySectorCode("A")).thenReturn(Optional.of(sector));
        when(parkingSessionRepository.findActiveSessionByLicensePlate("ZUL0001")).thenReturn(Optional.empty());

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "PARKED", null, null, -23.561684, -46.655981);

        assertThatThrownBy(() -> processParkedEventUseCase.execute(event))
                .isInstanceOf(ParkingSessionNotFoundException.class);

        verify(sectorRepository, never()).save(any());
    }
}
