package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.GarageConfigurationDto;
import com.estapar.parking.application.dto.SectorConfigDto;
import com.estapar.parking.application.dto.SpotConfigDto;
import com.estapar.parking.application.port.GarageSimulatorPort;
import com.estapar.parking.domain.entity.ParkingSpot;
import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.repository.ParkingSpotRepository;
import com.estapar.parking.domain.repository.SectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitializeGarageUseCase {

    private final GarageSimulatorPort garageSimulatorPort;
    private final SectorRepository sectorRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public InitializeGarageUseCase(GarageSimulatorPort garageSimulatorPort,
                                   SectorRepository sectorRepository,
                                   ParkingSpotRepository parkingSpotRepository) {
        this.garageSimulatorPort = garageSimulatorPort;
        this.sectorRepository = sectorRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public void execute() {
        GarageConfigurationDto configuration = garageSimulatorPort.fetchGarageConfiguration();
        persistSectors(configuration);
        persistSpots(configuration);
    }

    private void persistSectors(GarageConfigurationDto configuration) {
        for (SectorConfigDto sectorConfig : configuration.garage()) {
            boolean sectorAlreadyExists = sectorRepository.findBySectorCode(sectorConfig.sector()).isPresent();
            if (!sectorAlreadyExists) {
                Sector sector = new Sector(null, sectorConfig.sector(), sectorConfig.basePrice(), sectorConfig.maxCapacity(), 0);
                sectorRepository.save(sector);
            }
        }
    }

    private void persistSpots(GarageConfigurationDto configuration) {
        for (SpotConfigDto spotConfig : configuration.spots()) {
            boolean spotAlreadyExists = parkingSpotRepository.findByCoordinates(spotConfig.lat(), spotConfig.lng()).isPresent();
            if (!spotAlreadyExists) {
                ParkingSpot spot = new ParkingSpot(null, spotConfig.id(), spotConfig.sector(), spotConfig.lat(), spotConfig.lng(), false);
                parkingSpotRepository.save(spot);
            }
        }
    }
}
