package com.xm.crypto.recommendation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketPrice(LocalDateTime time, BigDecimal price) {
}
