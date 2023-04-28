package com.xm.crypto.recommendation.service;

import com.xm.crypto.recommendation.dto.NormalizedDto;
import com.xm.crypto.recommendation.model.Details;

import java.time.LocalDate;
import java.util.List;

public interface RecommendationService {


    /**
     * Provides data that was already analyzed on bean creation
     *
     * @return list where
     * value contains details like symbol, minimum, maximum, the oldest value, the newest value, normalized form
     * list is sorted by normalized value in descending order.
     */
    List<Details> getAnalyzedData();

    /**
     * @param date LocalDate
     * @return the highest normalized price for specific day from all cryptos
     */
    NormalizedDto getHighestNormalizedPrice(LocalDate date);

    /**
     * Reloads data from source
     */
    void refreshData();

    /**
     * @param symbol Crypto abbreviations
     * @return true if crypto is supported
     */
    boolean isSupported(String symbol);

}
