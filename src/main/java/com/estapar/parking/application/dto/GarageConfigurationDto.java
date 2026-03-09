package com.estapar.parking.application.dto;

import java.util.List;

public record GarageConfigurationDto(
        List<SectorConfigDto> garage,
        List<SpotConfigDto> spots
) {}
