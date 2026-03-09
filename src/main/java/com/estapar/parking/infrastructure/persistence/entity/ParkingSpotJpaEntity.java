package com.estapar.parking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_spots",
        indexes = {
                @Index(name = "idx_spot_coordinates", columnList = "latitude, longitude"),
                @Index(name = "idx_spot_sector", columnList = "sector_code")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpotJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_external_id", nullable = false)
    private Integer spotExternalId;

    @Column(name = "sector_code", nullable = false)
    private String sectorCode;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "occupied", nullable = false)
    private boolean occupied;
}
