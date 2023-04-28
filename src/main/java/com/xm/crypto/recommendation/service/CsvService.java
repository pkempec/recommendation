package com.xm.crypto.recommendation.service;

import com.xm.crypto.recommendation.model.MarketPrice;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface CsvService {

    /**
     * Loads data (timestamp, Crypto abbreviations, price)
     * from specified folder
     *
     * @param path to folder where are located csv files
     * @return map of MarketPrice where key represents Crypto abbreviations and value keeps MarketPrice
     */
    Map<String, List<MarketPrice>> loadData(Path path);
}
