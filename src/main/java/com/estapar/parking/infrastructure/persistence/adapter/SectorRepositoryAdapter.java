package com.estapar.parking.infrastructure.persistence.adapter;

import com.estapar.parking.domain.entity.Sector;
import com.estapar.parking.domain.repository.SectorRepository;
import com.estapar.parking.infrastructure.persistence.entity.SectorJpaEntity;
import com.estapar.parking.infrastructure.persistence.repository.SectorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SectorRepositoryAdapter implements SectorRepository {

    private final SectorJpaRepository sectorJpaRepository;

    public SectorRepositoryAdapter(SectorJpaRepository sectorJpaRepository) {
        this.sectorJpaRepository = sectorJpaRepository;
    }

    @Override
    public Optional<Sector> findBySectorCode(String sectorCode) {
        return sectorJpaRepository.findBySectorCode(sectorCode).map(this::toDomain);
    }

    @Override
    public List<Sector> findAll() {
        return sectorJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Sector save(Sector sector) {
        SectorJpaEntity saved = sectorJpaRepository.save(toJpaEntity(sector));
        return toDomain(saved);
    }

    private Sector toDomain(SectorJpaEntity entity) {
        return new Sector(entity.getId(), entity.getSectorCode(), entity.getBasePrice(),
                entity.getMaxCapacity(), entity.getCurrentOccupancy());
    }

    private SectorJpaEntity toJpaEntity(Sector sector) {
        return SectorJpaEntity.builder()
                .id(sector.getId())
                .sectorCode(sector.getSectorCode())
                .basePrice(sector.getBasePrice())
                .maxCapacity(sector.getMaxCapacity())
                .currentOccupancy(sector.getCurrentOccupancy())
                .build();
    }
}
