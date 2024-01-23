package com.app.cities.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Class used for wrapping the contents of a HTTP request's response.
 *
 */
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    @Getter
    private Integer temperature;
    @Getter
    private Integer wind;
    @Getter
    private String description;
    @Getter
    private List<Forecast> forecast;
}