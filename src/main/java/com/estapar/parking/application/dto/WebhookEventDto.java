package com.estapar.parking.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record WebhookEventDto(
        @NotBlank @JsonProperty("license_plate") String licensePlate,
        @NotNull @JsonProperty("event_type") String eventType,
        @JsonProperty("entry_time") LocalDateTime entryTime,
        @JsonProperty("exit_time") LocalDateTime exitTime,
        Double lat,
        Double lng
) {}
