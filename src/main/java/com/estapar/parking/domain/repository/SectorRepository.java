package com.estapar.parking.domain.repository;

import com.estapar.parking.domain.entity.Sector;

import java.util.List;
import java.util.Optional;

public interface SectorRepository {

    Optional<Sector> findBySectorCode(String sectorCode);

    List<Sector> findAll();

    Sector save(Sector sector);
}
