package com.estapar.parking.application.dto;

public record SpotConfigDto(
        Integer id,
        String sector,
        Double lat,
        Double lng
) {}
