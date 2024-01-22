package com.app.cities.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class Forecast {


    @Getter
    private Integer day;

    @Getter
    private Integer temperature;

    @Getter
    private Integer wind;
}