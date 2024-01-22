package com.test.cities.service;

import com.test.cities.entity.ForecastAverage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@Service
public class CsvService {

    public static final String HEADER = "Name, temperature, wind";

    public void writeToCsv(Flux<ForecastAverage> averageFlux, String path) {
        PrintWriter writer = null;
        List<ForecastAverage> averageList = new ArrayList<>();
        try {
            writer = new PrintWriter(path);
            writer.println(HEADER);
            // reverted to an iterable in order to be able to use the writer
            averageFlux.toIterable().forEach(forecastAverage -> averageList.add(forecastAverage));
            for (ForecastAverage forecastAverage : averageList) {
                writer.println(forecastAverage.toString());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}