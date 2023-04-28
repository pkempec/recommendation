package com.xm.crypto.recommendation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecommendationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Dummy integration test just to present I am able to write it.
     * Starts the context and hits the /crypto/{symbol} endpoint
     *
     * @throws Exception possible context exception
     */
    @Test
    void registrationWorksThroughAllLayers() throws Exception {
        mockMvc.perform(get("/crypto/{symbol}", "BTC")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("BTC"))
                .andExpect(jsonPath("$.min").value(33276.59))
                .andExpect(jsonPath("$.max").value(47722.66))
                .andExpect(jsonPath("$.oldest").value("2022-01-01T05:00:00"))
                .andExpect(jsonPath("$.newest").value("2022-01-31T21:00:00"));
    }

}
