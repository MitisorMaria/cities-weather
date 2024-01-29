package com.app.cities.service;

import com.app.cities.entity.Cities;
import com.app.cities.entity.ForecastAverage;
import com.app.cities.util.PropertiesReader;
import com.app.cities.entity.Forecast;
import com.app.cities.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service that calculates the forecast averages for a list of cities.
 */
@Component
public class ForecastAverageService {

    @Autowired private PropertiesReader propertiesReader;
    public static final String DASH = "-";
    public static final String SLASH = "/";
    public static final String UNDERSCORE = "_";
    public static final String NO_DATA = "No data for city: ";

    /**
     * Calculates the forecast averages for the valid cities in the list.
     *
     * @param cityList a list of cities containing both valid and invalid values
     * @return a {@code Flux} of forecast averages for the given cities
     */
    public Flux<ForecastAverage> getForecastAverages(List<String> cityList) {
        List<String> enumCitiesNames =
                Arrays.stream(Cities.values()).map(enumCity -> enumCity.name()).collect(Collectors.toList());
        List<String> validCities = cityList.stream()
                .filter(city -> enumCitiesNames.contains(city.toUpperCase(Locale.ROOT).replace(DASH, UNDERSCORE)))
                .sorted().collect(Collectors.toList());

        Set<String> noDuplicates = new HashSet<>(validCities);
        validCities.clear();
        validCities.addAll(noDuplicates);

        ForecastAverage fallbackForecastAverageValue = new ForecastAverage(NO_DATA, 0, 0);

        return Flux.fromIterable(validCities.stream().sorted().map(city -> {
                    List<Forecast> forecastList = doRequestForCity(city).getForecast();
                    return !forecastList.isEmpty() ? getAverageForForecastList(city, forecastList)
                            : new ForecastAverage(NO_DATA + city, 0, 0);
                }).collect(Collectors.toList()))
                .onErrorContinue(Throwable.class, (ex, o) -> Flux.just(fallbackForecastAverageValue));
    }

    /**
     * Calculates the forecast average from a list of forecasts, for a single city.
     *
     * @param cityName the name of the city whose forecasts are being used
     * @param forecastList the list of forecasts
     * @return a {@code ForecastAverage} object containing the name of the city and the average values for temperature
     * and wind speed.
     */
    private ForecastAverage getAverageForForecastList(String cityName, List<Forecast> forecastList) {
        Integer temperatureSum = 0;
        Integer windSum = 0;
        for (Forecast forecast : forecastList) {
            temperatureSum += forecast.getTemperature();
            windSum += forecast.getWind();
        }
        return new ForecastAverage(cityName, temperatureSum / forecastList.size(), windSum / forecastList.size());
    }

    /**
     * Makes a GET request to the cities API and retrieves the list of forecasts for a single city.
     *
     * @param city the name of the city whose forecasts are to be retrieved.
     * @return a {@code Response} object containing the list of forecasts for the given city.
     */
    private Response doRequestForCity(String city) {
        String uriString = propertiesReader.getApiUrl();
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(uriString));
        builder.path(SLASH + city);
        final URI uri = builder.build().toUri();
        ResponseEntity<Response> apiResponse;
        try {
            apiResponse = new RestTemplate().exchange(uri, HttpMethod.GET, null, Response.class);
        } catch (HttpClientErrorException exception) {
            return new Response(0, 0, "", new ArrayList<>());
        }
        return apiResponse.getBody();
    }
}