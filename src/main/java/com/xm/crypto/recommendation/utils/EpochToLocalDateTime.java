package com.xm.crypto.recommendation.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;

import java.time.Instant;
import java.time.ZoneId;

/**
 * Converts long to LocalDataTime
 */
public class EpochToLocalDateTime extends AbstractBeanField {

    @Override
    protected Object convert(String value) throws CsvConstraintViolationException {
        try {
            return Instant.ofEpochMilli(Long.parseLong(value))
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (NumberFormatException exception) {
            throw new CsvConstraintViolationException("Unable to parse timestamp");
        }
    }
}
