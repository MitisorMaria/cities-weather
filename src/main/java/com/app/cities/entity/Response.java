package com.app.cities.entity;

import lombok.Getter;

import java.util.List;


public class Response {

    @Getter
    private List<Forecast> forecastList;
}