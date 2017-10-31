package com.example.meter.aggregator.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SmartMeterAggregatorRoutes {

	@Bean
	public RouterFunction<ServerResponse> electricityMeasureRouter(
			ElectricityMeasureHandler electricityMeasureHandler) {
		return route(GET("/measures/firehose"), electricityMeasureHandler::firehose)
				.andRoute(GET("/measures/zones/{id}"), electricityMeasureHandler::forZone);
	}

}
