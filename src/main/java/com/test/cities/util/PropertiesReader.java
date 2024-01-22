package com.test.cities.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration("propertiesReader")
@PropertySource("classpath:application.properties")
public class PropertiesReader {

    @Value("${csv.path}")
    private String csvPath;

    @Value("${api.url}")
    private String apiUrl;

    public String getCsvPath() {
        return csvPath;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}