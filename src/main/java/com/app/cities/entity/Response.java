package com.app.cities.entity;

import lombok.Getter;

import java.util.List;


/**
 * Class used for wrapping the contents of a HTTP request's response.
 *
 */
public class Response {

    @Getter
    private List<Forecast> forecastList;
}