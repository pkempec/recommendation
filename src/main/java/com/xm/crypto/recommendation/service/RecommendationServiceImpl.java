package com.xm.crypto.recommendation.service;

import com.xm.crypto.recommendation.dto.NormalizedDto;
import com.xm.crypto.recommendation.model.Details;
import com.xm.crypto.recommendation.model.MarketPrice;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Read CSV data and analyze.
 * The data are analyzed only on the bean creation to save resources.
 * Provides analyzed data.
 */

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final CsvService csvService;
    private final Environment env;

    private Map<LocalDate, NormalizedDto> normalizedPerDay;
    private List<Details> analyzedData;

    public RecommendationServiceImpl(CsvService csvService, Environment env) {
        this.csvService = csvService;
        this.env = env;
        refreshData();
    }

    /**
     * Reads all csv files from path defined in property 'csv.dir' and analyze the values.
     * If 'csv.dir' is not provided it reads data from 'resources/csv'
     */
    @Override
    public void refreshData() {
        try {
            String csvDir = env.getProperty("csv.dir");

            String dir = StringUtils.isNotBlank(csvDir)
                    ? csvDir
                    : new ClassPathResource("csv").getFile().getPath();
            logger.info("Loading CSV data from: {}", dir);
            Path path = new File(dir).toPath();
            Map<String, List<MarketPrice>> data = csvService.loadData(path);
            normalizedPerDay = highestNormalizedPerDay(data);
            analyzedData = sortData(analyze(data));
        } catch (NullPointerException | InvalidPathException | IOException exception) {
            logger.error("Unable to read CSV data", exception);
        }
    }

    @Override
    public boolean isSupported(String symbol) {
        return analyzedData
                .stream()
                .anyMatch(detail -> detail.symbol().equalsIgnoreCase(symbol));
    }

    /**
     * Takes market prices for each crypto and calculate highest normalized price per day.
     *
     * @param data Map where
     *             Key is Crypto abbreviations
     *             Value is list of market prices
     * @return the highest normalized price per day
     */
    private Map<LocalDate, NormalizedDto> highestNormalizedPerDay(Map<String, List<MarketPrice>> data) {
        Map<LocalDate, NormalizedDto> result = new HashMap<>();
        for (var entry : data.entrySet()) {
            Map<LocalDate, BigDecimal> normalizedPerDay = entry.getValue()
                    .stream()
                    .collect(Collectors.groupingBy(marketPrice -> marketPrice.time().toLocalDate()))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, perDay -> {
                        var min = perDay.getValue().stream().map(MarketPrice::price).min(Comparator.naturalOrder()).orElse(null);
                        var max = perDay.getValue().stream().map(MarketPrice::price).max(Comparator.naturalOrder()).orElse(null);
                        return normalize(min, max);
                    }));
            for (var normalized : normalizedPerDay.entrySet()) {
                var max = result.get(normalized.getKey());
                if (max == null || normalized.getValue().compareTo(max.normalized()) > 0) {
                    result.put(normalized.getKey(), new NormalizedDto(entry.getKey(), normalized.getValue()));
                }
            }
        }
        return result;
    }

    /**
     * Normalize data (max-min)/min
     * Rounding to 10 decimals up
     *
     * @param min minimum
     * @param max maximum
     * @return normalized value
     */
    private BigDecimal normalize(BigDecimal min, BigDecimal max) {
        return min != null && max != null
                ? (max.subtract(min)).divide(min, 10, RoundingMode.HALF_UP)
                : null;
    }

    /**
     * Sort data by normalized value in descending order
     *
     * @param data details
     * @return sorted list
     */
    private List<Details> sortData(List<Details> data) {
        return data.stream()
                .sorted(Comparator
                        .comparing(Details::normalized)
                        .reversed())
                .toList();
    }

    /**
     * Analyze provided data
     *
     * @param data that needs to be analyzed. It should contain Crypto abbreviations and its price in time.
     * @return map where
     * Key is Crypto abbreviations
     * Value contains details like minimum, maximum, the oldest value, the newest value, normalized form.
     * normalized form is rounded to 4 places
     */
    public List<Details> analyze(Map<String, List<MarketPrice>> data) {
        List<Details> result = new ArrayList<>();
        for (Map.Entry<String, List<MarketPrice>> entry : data.entrySet()) {
            var prices = entry.getValue();
            var symbol = entry.getKey();
            var oldest = prices.stream().map(MarketPrice::time).min(LocalDateTime::compareTo).orElse(null);
            var newest = prices.stream().map(MarketPrice::time).max(LocalDateTime::compareTo).orElse(null);
            var min = prices.stream().map(MarketPrice::price).min(Comparator.naturalOrder()).orElse(null);
            var max = prices.stream().map(MarketPrice::price).max(Comparator.naturalOrder()).orElse(null);
            var normalized = normalize(min, max);
            result.add(new Details(symbol, oldest, newest, min, max, normalized));
        }
        return result;
    }

    @Override
    public List<Details> getAnalyzedData() {
        return analyzedData;
    }

    @Override
    public NormalizedDto getHighestNormalizedPrice(LocalDate date) {
        return normalizedPerDay.get(date);
    }
}
