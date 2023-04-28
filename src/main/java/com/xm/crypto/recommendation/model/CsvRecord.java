package com.xm.crypto.recommendation.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.xm.crypto.recommendation.utils.EpochToLocalDateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CsvRecord {

    @CsvCustomBindByName(converter = EpochToLocalDateTime.class)
    @CsvBindByName
    private LocalDateTime timestamp;

    @CsvBindByName
    private String symbol;

    @CsvBindByName
    private BigDecimal price;

    public CsvRecord() {
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
