package com.app.cities.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class ForecastAverage {

    public static final String COMMA = ",";

    @Getter
    private String name;

    @Getter
    private Integer temperature;

    @Getter
    private Integer wind;

    @Override
    public String toString() {
        return name + COMMA + temperature + COMMA + wind;
    }
}