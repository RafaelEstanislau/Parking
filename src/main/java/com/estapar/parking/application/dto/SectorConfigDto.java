package com.estapar.parking.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SectorConfigDto(
        String sector,
        BigDecimal basePrice,
        @JsonProperty("max_capacity") int maxCapacity
) {}
