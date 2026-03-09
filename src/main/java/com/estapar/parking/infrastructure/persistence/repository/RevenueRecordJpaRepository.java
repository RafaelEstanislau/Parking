package com.estapar.parking.infrastructure.persistence.repository;

import com.estapar.parking.infrastructure.persistence.entity.RevenueRecordJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RevenueRecordJpaRepository extends JpaRepository<RevenueRecordJpaEntity, Long> {

    List<RevenueRecordJpaEntity> findBySectorCodeAndRevenueDate(String sectorCode, LocalDate revenueDate);
}
