package com.estapar.parking.interfaces.rest;

import com.estapar.parking.domain.exception.GarageCapacityExceededException;
import com.estapar.parking.domain.exception.ParkingSessionNotFoundException;
import com.estapar.parking.domain.exception.ParkingSpotNotFoundException;
import com.estapar.parking.domain.exception.SectorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GarageCapacityExceededException.class)
    public ResponseEntity<Map<String, Object>> handleGarageCapacityExceeded(GarageCapacityExceededException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(exception.getMessage()));
    }

    @ExceptionHandler(ParkingSessionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleParkingSessionNotFound(ParkingSessionNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(exception.getMessage()));
    }

    @ExceptionHandler(ParkingSpotNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleParkingSpotNotFound(ParkingSpotNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(exception.getMessage()));
    }

    @ExceptionHandler(SectorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSectorNotFound(SectorNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(exception.getMessage()));
    }

    private Map<String, Object> errorBody(String message) {
        return Map.of(
                "error", message,
                "timestamp", Instant.now().toString()
        );
    }
}
