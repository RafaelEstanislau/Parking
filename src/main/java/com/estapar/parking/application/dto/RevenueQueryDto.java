package com.estapar.parking.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RevenueQueryDto(
        @NotNull LocalDate date,
        @NotBlank String sector
) {}
