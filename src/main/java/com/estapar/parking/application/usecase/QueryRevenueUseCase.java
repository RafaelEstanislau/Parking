package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.RevenueQueryDto;
import com.estapar.parking.application.dto.RevenueResponseDto;
import com.estapar.parking.domain.entity.RevenueRecord;
import com.estapar.parking.domain.repository.RevenueRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class QueryRevenueUseCase {

    private final RevenueRecordRepository revenueRecordRepository;

    public QueryRevenueUseCase(RevenueRecordRepository revenueRecordRepository) {
        this.revenueRecordRepository = revenueRecordRepository;
    }

    @Transactional(readOnly = true)
    public RevenueResponseDto execute(RevenueQueryDto query) {
        List<RevenueRecord> records = revenueRecordRepository.findBySectorCodeAndRevenueDate(
                query.sector(), query.date());

        BigDecimal totalRevenue = records.stream()
                .map(RevenueRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RevenueResponseDto.of(totalRevenue);
    }
}
