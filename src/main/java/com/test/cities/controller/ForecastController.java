package com.test.cities.controller;

import com.test.cities.util.PropertiesReader;
import com.test.cities.entity.ForecastAverage;
import com.test.cities.service.CsvService;
import com.test.cities.service.ForecastAverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;


@RestController
@RequestMapping("/forecast_average")
public class ForecastController {

    @Autowired
    private ForecastAverageService forecastAverageService;
    @Autowired
    private CsvService csvService;
    @Autowired
    private PropertiesReader propertiesReader;


    @GetMapping
    public Flux<ForecastAverage> getForecastAverages(@RequestParam List<String> city) {
        Flux<ForecastAverage> forecastAverageList = forecastAverageService.getForecastAverages(city);
        csvService.writeToCsv(forecastAverageList, propertiesReader.getCsvPath());
        return forecastAverageList;
    }
}