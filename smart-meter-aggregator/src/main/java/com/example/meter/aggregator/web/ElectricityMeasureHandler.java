package com.example.meter.aggregator.web;

import java.util.Map;

import com.example.meter.aggregator.domain.ElectricityMeasure;
import com.example.meter.aggregator.generator.ElectricityMeasureGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
class ElectricityMeasureHandler {

	private final Map<String, Flux<ElectricityMeasure>> content;

	private final Flux<ElectricityMeasure> firehose;

	ElectricityMeasureHandler(ElectricityMeasureGenerator generator) {
		this.content = generator.generateSensorData();
		this.firehose = Flux.merge(content.values()).share();
	}

	public Mono<ServerResponse> firehose(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(this.firehose, ElectricityMeasure.class);
	}

	public Mono<ServerResponse> forZone(ServerRequest request) {
		String id = request.pathVariable("id");
		return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(this.content.get(id), ElectricityMeasure.class);
	}

}
