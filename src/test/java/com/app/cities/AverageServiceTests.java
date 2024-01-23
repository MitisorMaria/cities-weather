package com.app.cities;

import com.app.cities.entity.ForecastAverage;
import com.app.cities.service.ForecastAverageService;
import com.app.cities.util.PropertiesReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;


@SpringBootTest
public class AverageServiceTests {

    @Mock private PropertiesReader propertiesReader;
    @InjectMocks private ForecastAverageService forecastAverageService;

    @Test
    void areAveragesCalculatedCorrectly() {
        when(propertiesReader.getCsvPath()).thenReturn("");
        when(propertiesReader.getApiUrl()).thenReturn("http://localhost:8082/forecastService");

        List<String> testCityList = Arrays.stream("Arad,Dej".split(",")).toList();
        List<ForecastAverage> forecastAverageList =
                forecastAverageService.getForecastAverages(testCityList).toStream().collect(Collectors.toList());
        Assertions.assertEquals(1, forecastAverageList.size());
        Assertions.assertEquals("Arad", forecastAverageList.get(0).getName());
        Assertions.assertEquals(19, forecastAverageList.get(0).getTemperature());
        Assertions.assertEquals(16, forecastAverageList.get(0).getWind());
    }

    @Test
    void emptyResultWhenNoValidCities() {
        when(propertiesReader.getCsvPath()).thenReturn("");
        when(propertiesReader.getApiUrl()).thenReturn("http://localhost:8082/forecastService");

        List<String> testCityList = Arrays.stream("Dej,Brasov".split(",")).toList();
        List<ForecastAverage> forecastAverageList =
                forecastAverageService.getForecastAverages(testCityList).toStream().collect(Collectors.toList());
        Assertions.assertEquals(0, forecastAverageList.size());
    }

    @Test
    void areValidCitiesDetectedCorrectly() {
        when(propertiesReader.getCsvPath()).thenReturn("");
        when(propertiesReader.getApiUrl()).thenReturn("http://localhost:8082/forecastService");

        List<String> testCityList = Arrays.stream(
                "Cluj-Napoca,Bucuresti,Craiova,Timisoara,Dej,Constanta,Cluj-Napoca,Baia-Mare,Arad,Bistrita,Oradea".split(
                        ",")).toList();
        List<ForecastAverage> forecastAverageList =
                forecastAverageService.getForecastAverages(testCityList).toStream().collect(Collectors.toList());
        Assertions.assertEquals(6, forecastAverageList.size());
    }
}