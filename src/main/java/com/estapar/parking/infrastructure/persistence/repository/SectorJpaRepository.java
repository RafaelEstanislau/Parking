package com.estapar.parking.infrastructure.persistence.repository;

import com.estapar.parking.infrastructure.persistence.entity.SectorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectorJpaRepository extends JpaRepository<SectorJpaEntity, Long> {

    Optional<SectorJpaEntity> findBySectorCode(String sectorCode);
}
