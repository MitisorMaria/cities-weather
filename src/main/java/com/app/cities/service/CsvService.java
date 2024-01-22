package com.app.cities.service;

import com.app.cities.entity.ForecastAverage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Service that writes to a .csv file the {@code ForecastAverage} objects it receives.
 *
 */
@Component
public class CsvService {

    public static final String HEADER = "Name, temperature, wind";

    /**
     * Writes the objects from the {@code Flux} to a .csv file.
     *
     * @param averageFlux flux containing {@code ForecastAverage} objects, received from the forecast average service.
     * @param path the path used for storing the .csv file.
     */
    public void writeToCsv(Flux<ForecastAverage> averageFlux, String path) {
        PrintWriter writer;
        List<ForecastAverage> averageList = new ArrayList<>();
        try {
            writer = new PrintWriter(path);
            writer.println(HEADER);
            // converted to an iterable in order to be able to use the writer
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