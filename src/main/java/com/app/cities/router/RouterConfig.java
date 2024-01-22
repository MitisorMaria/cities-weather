package com.app.cities.router;

import com.app.cities.handler.ForecastAverageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterConfig {

    @Bean
    RouterFunction<ServerResponse> routes(ForecastAverageHandler handler) {
        return route(GET("/api/weather").and(
                accept(MediaType.APPLICATION_JSON)), handler::getForecastAverages);
    }
}