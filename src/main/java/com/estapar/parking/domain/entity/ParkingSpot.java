package com.estapar.parking.domain.entity;

public class ParkingSpot {

    private Long id;
    private Integer spotExternalId;
    private String sectorCode;
    private Double latitude;
    private Double longitude;
    private boolean occupied;

    public ParkingSpot(Long id, Integer spotExternalId, String sectorCode, Double latitude, Double longitude, boolean occupied) {
        this.id = id;
        this.spotExternalId = spotExternalId;
        this.sectorCode = sectorCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.occupied = occupied;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void release() {
        this.occupied = false;
    }

    public Long getId() { return id; }
    public Integer getSpotExternalId() { return spotExternalId; }
    public String getSectorCode() { return sectorCode; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public boolean isOccupied() { return occupied; }
}
