package com.app.cities;

import com.app.cities.entity.ForecastAverage;
import com.app.cities.handler.ForecastAverageHandler;
import com.app.cities.router.RouterConfig;
import com.app.cities.service.CsvService;
import com.app.cities.service.ForecastAverageService;
import com.app.cities.util.PropertiesReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import java.util.Arrays;

import static org.mockito.Mockito.when;


@SpringBootTest
public class CitiesWeatherRoutingTests {


    @Mock private ForecastAverageService forecastAverageService;
    @Mock private PropertiesReader propertiesReader;
    @Mock private CsvService csvService;

    @InjectMocks private ForecastAverageHandler forecastAverageHandler;
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        RouterFunction<?> routes = new RouterConfig().routes(forecastAverageHandler);
        client = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    void areRequestsRoutedCorrectly() {
        ForecastAverage testAverage = new ForecastAverage("Arad", 19, 16);
        when(forecastAverageService.getForecastAverages(Arrays.stream("Arad,Dej".split(",")).toList())).thenReturn(
                Flux.just(testAverage));

        client.get().uri(uriBuilder -> uriBuilder.path("/api/weather").
                        queryParam("city", "Arad,Dej").build())
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$[0].name").isEqualTo("Arad")
                .jsonPath("$[0].temperature").isEqualTo("19")
                .jsonPath("$[0].wind").isEqualTo("16");
    }
}