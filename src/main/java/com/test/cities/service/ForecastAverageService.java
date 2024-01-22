package com.test.cities.service;

import com.test.cities.util.PropertiesReader;
import com.test.cities.entity.Cities;
import com.test.cities.entity.Forecast;
import com.test.cities.entity.ForecastAverage;
import com.test.cities.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ForecastAverageService {

    @Autowired
    private PropertiesReader propertiesReader;

    public static final String FORECAST_URL = "/forecast";

    public Flux<ForecastAverage> getForecastAverages(List<String> cityList) {
        Map<String, String> parameters = new HashMap<>();
        List<String> enumCitiesNames =
                Arrays.stream(Cities.values()).map(enumCity -> enumCity.name()).collect(Collectors.toList());
        List<String> validCities = cityList.stream()
                .filter(city -> enumCitiesNames.contains(city.toUpperCase(Locale.ROOT).replace('-', '_')))
                .sorted().collect(Collectors.toList());

        Set<String> noDuplicates = new HashSet<>(validCities);
        validCities.clear();
        validCities.addAll(noDuplicates);

        return Flux.fromIterable(validCities.stream().sorted().map(city -> {
            List<Forecast> forecastList = doRequestForCity(parameters, city).getForecastList();
            return getAverageForForecastList(city, forecastList);
        }).collect(Collectors.toList()));
    }

    private ForecastAverage getAverageForForecastList(String name, List<Forecast> forecastList) {
        Integer temperatureSum = 0;
        Integer windSum = 0;
        for (Forecast forecast : forecastList) {
            temperatureSum += forecast.getTemperature();
            windSum += forecast.getWind();
        }
        return new ForecastAverage(name, temperatureSum / forecastList.size(), windSum / forecastList.size());
    }

    private Response doRequestForCity(Map<String, String> parameters, String city) {
        String uriString = propertiesReader.getApiUrl() + FORECAST_URL;
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(uriString));
        builder.path("/" + city);
        final URI uri = builder.build().toUri();

        return new RestTemplate().exchange(uri, HttpMethod.GET, null, Response.class).getBody();
    }
}