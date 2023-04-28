package com.xm.crypto.recommendation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CryptoDetailsDto(String symbol,
                               LocalDateTime oldest,
                               LocalDateTime newest,
                               BigDecimal min,
                               BigDecimal max) {
}
