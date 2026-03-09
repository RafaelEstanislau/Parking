package com.estapar.parking.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record RevenueResponseDto(
        BigDecimal amount,
        String currency,
        Instant timestamp
) {
    public static RevenueResponseDto of(BigDecimal amount) {
        return new RevenueResponseDto(amount, "BRL", Instant.now());
    }
}
