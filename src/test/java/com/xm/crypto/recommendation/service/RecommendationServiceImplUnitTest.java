package com.xm.crypto.recommendation.service;

import com.xm.crypto.recommendation.model.MarketPrice;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class RecommendationServiceImplUnitTest {

    private final CsvService csvServiceMock = Mockito.mock(CsvService.class);
    private final Environment envMock = Mockito.mock(Environment.class);


    /**
     * Dummy unit test just to present I am able to write it.
     */
    @Test
    public void test() {
        Map<String, List<MarketPrice>> data = Map.of("ADA", Arrays.asList(new MarketPrice(LocalDateTime.now(), BigDecimal.TEN)));
        Mockito.when(csvServiceMock.loadData(any())).thenReturn(data);

        RecommendationServiceImpl service = new RecommendationServiceImpl(csvServiceMock, envMock);

        Mockito.verify(csvServiceMock, times(1)).loadData(any());
        assertTrue(service.isSupported("ADA"), "Cardano should be supported");
    }
}