package com.estapar.parking.domain.repository;

import com.estapar.parking.domain.entity.RevenueRecord;

import java.time.LocalDate;
import java.util.List;

public interface RevenueRecordRepository {

    List<RevenueRecord> findBySectorCodeAndRevenueDate(String sectorCode, LocalDate revenueDate);

    RevenueRecord save(RevenueRecord revenueRecord);
}
