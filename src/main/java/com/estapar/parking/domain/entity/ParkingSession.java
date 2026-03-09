package com.estapar.parking.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingSession {

    private Long id;
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String sectorCode;
    private Long parkingSpotId;
    private BigDecimal pricePerHour;
    private BigDecimal totalAmount;
    private SessionStatus status;

    public ParkingSession(Long id, String licensePlate, LocalDateTime entryTime, SessionStatus status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.status = status;
    }

    public ParkingSession(Long id, String licensePlate, LocalDateTime entryTime, LocalDateTime exitTime,
                          String sectorCode, Long parkingSpotId, BigDecimal pricePerHour,
                          BigDecimal totalAmount, SessionStatus status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.sectorCode = sectorCode;
        this.parkingSpotId = parkingSpotId;
        this.pricePerHour = pricePerHour;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public void assignSpot(Long spotId, String sector, BigDecimal dynamicPricePerHour) {
        this.parkingSpotId = spotId;
        this.sectorCode = sector;
        this.pricePerHour = dynamicPricePerHour;
        this.status = SessionStatus.PARKED;
    }

    public void complete(LocalDateTime sessionExitTime, BigDecimal sessionTotalAmount) {
        this.exitTime = sessionExitTime;
        this.totalAmount = sessionTotalAmount;
        this.status = SessionStatus.COMPLETED;
    }

    public boolean isActive() {
        return status == SessionStatus.ACTIVE || status == SessionStatus.PARKED;
    }

    public Long getId() { return id; }
    public String getLicensePlate() { return licensePlate; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public String getSectorCode() { return sectorCode; }
    public Long getParkingSpotId() { return parkingSpotId; }
    public BigDecimal getPricePerHour() { return pricePerHour; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public SessionStatus getStatus() { return status; }
}
