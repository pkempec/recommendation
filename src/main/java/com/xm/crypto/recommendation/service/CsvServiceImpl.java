package com.xm.crypto.recommendation.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.xm.crypto.recommendation.model.CsvRecord;
import com.xm.crypto.recommendation.model.MarketPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CsvServiceImpl implements CsvService {

    private final Logger logger = LoggerFactory.getLogger(CsvServiceImpl.class);

    @Override
    public Map<String, List<MarketPrice>> loadData(Path path) {
        try (var paths = Files.list(path)) {
            return paths
                    .map(this::readCsvFile)
                    .flatMap(List::stream)
                    .toList()
                    .stream()
                    .collect(
                            Collectors.groupingBy(
                                    CsvRecord::getSymbol,
                                    Collectors.mapping(record -> new MarketPrice(record.getTimestamp(), record.getPrice()), Collectors.toList())));
        } catch (IOException e) {
            logger.error("Unable to read directory: {}", path);
        }
        return null;
    }

    /**
     * Reads data from CSV file
     *
     * @param path to the file
     * @return content of file as list of records
     */
    private List<CsvRecord> readCsvFile(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<CsvRecord> csvReader = new CsvToBeanBuilder<CsvRecord>(reader)
                    .withSeparator(',')
                    .withType(CsvRecord.class)
                    .build();
            return csvReader.parse();
        } catch (IOException e) {
            logger.error("Unable to read csv file: {}", path);
        }
        return Collections.emptyList();
    }
}
