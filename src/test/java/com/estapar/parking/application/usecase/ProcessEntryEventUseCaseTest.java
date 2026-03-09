package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.WebhookEventDto;
import com.estapar.parking.domain.entity.ParkingSession;
import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.exception.GarageCapacityExceededException;
import com.estapar.parking.domain.repository.ParkingSessionRepository;
import com.estapar.parking.domain.repository.SectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessEntryEventUseCaseTest {

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    private ProcessEntryEventUseCase processEntryEventUseCase;

    @BeforeEach
    void setUp() {
        processEntryEventUseCase = new ProcessEntryEventUseCase(sectorRepository, parkingSessionRepository);
    }

    @Test
    void shouldCreateSessionWhenSectorHasAvailableCapacity() {
        Sector sectorWithAvailability = new Sector(1L, "A", new BigDecimal("10.00"), 100, 50);
        when(sectorRepository.findAll()).thenReturn(List.of(sectorWithAvailability));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "ENTRY",
                LocalDateTime.of(2025, 1, 1, 12, 0, 0), null, null, null);

        processEntryEventUseCase.execute(event);

        ArgumentCaptor<ParkingSession> sessionCaptor = ArgumentCaptor.forClass(ParkingSession.class);
        verify(parkingSessionRepository).save(sessionCaptor.capture());

        ParkingSession savedSession = sessionCaptor.getValue();
        assertThat(savedSession.getLicensePlate()).isEqualTo("ZUL0001");
        assertThat(savedSession.getEntryTime()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0, 0));
    }

    @Test
    void shouldRejectEntryWhenAllSectorsAreAtFullCapacity() {
        Sector fullSector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 100);
        when(sectorRepository.findAll()).thenReturn(List.of(fullSector));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "ENTRY",
                LocalDateTime.of(2025, 1, 1, 12, 0, 0), null, null, null);

        assertThatThrownBy(() -> processEntryEventUseCase.execute(event))
                .isInstanceOf(GarageCapacityExceededException.class);

        verify(parkingSessionRepository, never()).save(any());
    }

    @Test
    void shouldAllowEntryWhenAtLeastOneSectorHasCapacity() {
        Sector fullSector = new Sector(1L, "A", new BigDecimal("10.00"), 100, 100);
        Sector availableSector = new Sector(2L, "B", new BigDecimal("10.00"), 100, 50);
        when(sectorRepository.findAll()).thenReturn(List.of(fullSector, availableSector));
        when(parkingSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        WebhookEventDto event = new WebhookEventDto("ZUL0001", "ENTRY",
                LocalDateTime.of(2025, 1, 1, 12, 0, 0), null, null, null);

        processEntryEventUseCase.execute(event);

        verify(parkingSessionRepository).save(any(ParkingSession.class));
    }
}
