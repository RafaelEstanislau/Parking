package com.estapar.parking.infrastructure.persistence.adapter;

import com.estapar.parking.domain.entity.RevenueRecord;
import com.estapar.parking.domain.repository.RevenueRecordRepository;
import com.estapar.parking.infrastructure.persistence.entity.RevenueRecordJpaEntity;
import com.estapar.parking.infrastructure.persistence.repository.RevenueRecordJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RevenueRecordRepositoryAdapter implements RevenueRecordRepository {

    private final RevenueRecordJpaRepository revenueRecordJpaRepository;

    public RevenueRecordRepositoryAdapter(RevenueRecordJpaRepository revenueRecordJpaRepository) {
        this.revenueRecordJpaRepository = revenueRecordJpaRepository;
    }

    @Override
    public List<RevenueRecord> findBySectorCodeAndRevenueDate(String sectorCode, LocalDate revenueDate) {
        return revenueRecordJpaRepository.findBySectorCodeAndRevenueDate(sectorCode, revenueDate)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public RevenueRecord save(RevenueRecord revenueRecord) {
        RevenueRecordJpaEntity saved = revenueRecordJpaRepository.save(toJpaEntity(revenueRecord));
        return toDomain(saved);
    }

    private RevenueRecord toDomain(RevenueRecordJpaEntity entity) {
        return new RevenueRecord(entity.getId(), entity.getSectorCode(), entity.getRevenueDate(), entity.getAmount());
    }

    private RevenueRecordJpaEntity toJpaEntity(RevenueRecord record) {
        return RevenueRecordJpaEntity.builder()
                .id(record.getId())
                .sectorCode(record.getSectorCode())
                .revenueDate(record.getRevenueDate())
                .amount(record.getAmount())
                .build();
    }
}
