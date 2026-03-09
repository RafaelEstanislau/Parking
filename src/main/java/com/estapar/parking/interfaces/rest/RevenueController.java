package com.estapar.parking.interfaces.rest;

import com.estapar.parking.application.dto.RevenueQueryDto;
import com.estapar.parking.application.dto.RevenueResponseDto;
import com.estapar.parking.application.usecase.QueryRevenueUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    private final QueryRevenueUseCase queryRevenueUseCase;

    public RevenueController(QueryRevenueUseCase queryRevenueUseCase) {
        this.queryRevenueUseCase = queryRevenueUseCase;
    }

    @GetMapping
    public ResponseEntity<RevenueResponseDto> getRevenue(@Valid @RequestBody RevenueQueryDto query) {
        RevenueResponseDto response = queryRevenueUseCase.execute(query);
        return ResponseEntity.ok(response);
    }
}
