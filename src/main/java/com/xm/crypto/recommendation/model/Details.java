package com.xm.crypto.recommendation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Details(String symbol,
                      LocalDateTime oldest,
                      LocalDateTime newest,
                      BigDecimal min,
                      BigDecimal max,
                      BigDecimal normalized) {
}
