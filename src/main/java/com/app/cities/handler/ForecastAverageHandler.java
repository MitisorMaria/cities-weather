package com.app.cities.handler;

import com.app.cities.entity.ForecastAverage;
import com.app.cities.service.CsvService;
import com.app.cities.service.ForecastAverageService;
import com.app.cities.util.PropertiesReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;


/**
 * Class that holds all handler functions related to forecast averages.
 *
 */
@Component
@RequiredArgsConstructor
public class ForecastAverageHandler {

    @Autowired private ForecastAverageService forecastAverageService;

    @Autowired
    private CsvService csvService;

    @Autowired
    private PropertiesReader propertiesReader;

    public Mono<ServerResponse> getForecastAverages(ServerRequest request) {
        Flux<ForecastAverage> forecastAverageList = forecastAverageService.getForecastAverages(
                Arrays.stream(request.queryParam("city").get().split(",")).toList());
        csvService.writeToCsv(forecastAverageList, propertiesReader.getCsvPath());
        return ServerResponse.ok()
                .body(BodyInserters.fromPublisher(forecastAverageList, ForecastAverage.class));
    }
}