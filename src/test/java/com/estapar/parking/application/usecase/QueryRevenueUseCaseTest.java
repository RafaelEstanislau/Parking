package com.estapar.parking.application.usecase;

import com.estapar.parking.application.dto.RevenueQueryDto;
import com.estapar.parking.application.dto.RevenueResponseDto;
import com.estapar.parking.domain.entity.RevenueRecord;
import com.estapar.parking.domain.repository.RevenueRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryRevenueUseCaseTest {

    @Mock
    private RevenueRecordRepository revenueRecordRepository;

    private QueryRevenueUseCase queryRevenueUseCase;

    @BeforeEach
    void setUp() {
        queryRevenueUseCase = new QueryRevenueUseCase(revenueRecordRepository);
    }

    @Test
    void shouldReturnTotalRevenueForSectorAndDate() {
        LocalDate queryDate = LocalDate.of(2025, 1, 1);
        List<RevenueRecord> records = List.of(
                new RevenueRecord(1L, "A", queryDate, new BigDecimal("20.00")),
                new RevenueRecord(2L, "A", queryDate, new BigDecimal("10.00")),
                new RevenueRecord(3L, "A", queryDate, new BigDecimal("15.00"))
        );

        when(revenueRecordRepository.findBySectorCodeAndRevenueDate("A", queryDate)).thenReturn(records);

        RevenueQueryDto query = new RevenueQueryDto(queryDate, "A");
        RevenueResponseDto response = queryRevenueUseCase.execute(query);

        assertThat(response.amount()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(response.currency()).isEqualTo("BRL");
    }

    @Test
    void shouldReturnZeroWhenNoRecordsExistForSectorAndDate() {
        LocalDate queryDate = LocalDate.of(2025, 1, 1);
        when(revenueRecordRepository.findBySectorCodeAndRevenueDate("A", queryDate)).thenReturn(List.of());

        RevenueQueryDto query = new RevenueQueryDto(queryDate, "A");
        RevenueResponseDto response = queryRevenueUseCase.execute(query);

        assertThat(response.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
