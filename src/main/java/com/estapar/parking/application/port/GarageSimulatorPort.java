package com.estapar.parking.application.port;

import com.estapar.parking.application.dto.GarageConfigurationDto;

public interface GarageSimulatorPort {

    GarageConfigurationDto fetchGarageConfiguration();
}
