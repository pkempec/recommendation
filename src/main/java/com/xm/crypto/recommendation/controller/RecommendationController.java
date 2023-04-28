package com.xm.crypto.recommendation.controller;

import com.xm.crypto.recommendation.dto.CryptoDetailsDto;
import com.xm.crypto.recommendation.dto.NormalizedDto;
import com.xm.crypto.recommendation.service.RecommendationService;
import com.xm.crypto.recommendation.validator.SupportedCrypto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
public class RecommendationController {

    private final RecommendationService service;

    RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @Operation(summary = "Normalized data", description = "Provides normalized data for each crypto")
    @GetMapping("/crypto")
    public List<NormalizedDto> crypto() {
        return service.getAnalyzedData()
                .stream()
                .map(data -> new NormalizedDto(data.symbol(), data.normalized()))
                .toList();
    }

    @Operation(summary = "Details", description = "Provides detailed information - newest, oldest, min, max,")
    @GetMapping("/crypto/{symbol}")
    public CryptoDetailsDto details(@PathVariable @SupportedCrypto @Parameter(description = "Crypto abbreviations") String symbol) {
        var details = service.getAnalyzedData()
                .stream()
                .filter(data -> symbol.equalsIgnoreCase(data.symbol()))
                .findFirst()
                .orElseThrow();
        return new CryptoDetailsDto(details.symbol(), details.oldest(), details.newest(), details.min(), details.max());
    }

    @Operation(summary = "Recommendation", description = "Provides the highest normalized price for specific date of format [yyyy-MM-dd]")
    @GetMapping("/recommend/{date}")
    public NormalizedDto recommend(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") @Parameter(description = "Date in format [yyyy-MM-dd]") LocalDate date) {
        return service.getHighestNormalizedPrice(date);
    }

    @Operation(summary = "Refresh Data", description = "Trigger reload of the data from csv files")
    @GetMapping("/refresh")
    public String refresh() {
        service.refreshData();
        return "refreshed";
    }

}
